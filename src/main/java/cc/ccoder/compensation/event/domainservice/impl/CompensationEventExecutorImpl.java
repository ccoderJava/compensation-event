package cc.ccoder.compensation.event.domainservice.impl;

import cc.ccoder.compensation.event.dal.CompensationEventMapper;
import cc.ccoder.compensation.event.domain.BatchExecuteResult;
import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.CompensationProperties;
import cc.ccoder.compensation.event.domain.ExecuteResult;
import cc.ccoder.compensation.event.domain.convertor.CompensationEventConverter;
import cc.ccoder.compensation.event.domainservice.CompensationEventExecutor;
import cc.ccoder.compensation.event.service.CompensationEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 补偿事件执行器
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 18:01
 */
@Slf4j
@Service
public class CompensationEventExecutorImpl implements CompensationEventExecutor {

    public static final int ERROR_MESSAGE_MAX_WIDTH = 128;

    private static final String THREAD_NAME_PREFIX = "COMPENSATION_EVENT";

    private final CompensationProperties compensationProperties;

    private final CompensationEventHandlerFactory compensationEventHandlerFactory;

    private final CompensationEventMapper compensationEventMapper;

    private final CompensationEventConverter compensationEventConverter;

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public CompensationEventExecutorImpl(CompensationProperties compensationProperties,
        CompensationEventHandlerFactory compensationEventHandlerFactory,
        CompensationEventMapper compensationEventMapper, CompensationEventConverter compensationEventConverter) {
        this.compensationProperties = compensationProperties;
        this.compensationEventHandlerFactory = compensationEventHandlerFactory;
        this.compensationEventMapper = compensationEventMapper;
        this.compensationEventConverter = compensationEventConverter;
    }

    /**
     * 初始化线程池参数
     */
    @PostConstruct
    public void initialize() {
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        CompensationProperties.PoolConfig poolConfig = compensationProperties.getPoolConfig();
        threadPoolTaskExecutor.setCorePoolSize(poolConfig.getCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(poolConfig.getMaxPoolSize());
        threadPoolTaskExecutor.setQueueCapacity(poolConfig.getQueueCapacity());
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        threadPoolTaskExecutor.setKeepAliveSeconds(poolConfig.getKeepAliveSeconds());
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        threadPoolTaskExecutor.initialize();
    }

    @Override
    public ExecuteResult execute(CompensationEvent event) {
        log.info("执行补偿事件：eventId:{}, eventKey:{}, mainType:{}, subType:{}", event.getEventId(), event.getEventKey(),
            event.getMainType(), event.getSubType());
        ExecuteResult result = null;
        try {
            // 校验原始补偿事件参数,如果无法校验通过则不进行执行
            if (event.getEventId() == null || event.getExecuteStatus() == null) {
                result = ExecuteResult.error("事件字段有误:" + event);
                return result;
            }
            // 判断执行前是否查询数据库,判断是否跳过执行
            if (!event.isSkipCheckBeforeExecute()) {
                CompensationEvent existEvent = compensationEventConverter
                    .convertToBO(compensationEventMapper.getAllowByEventId(event.getEventId()));
                if (existEvent == null) {
                    return null;
                }
                event = existEvent;
            }
            // 根据业务类型进行处理器执行补偿事件
            CompensationEventHandler eventHandler = compensationEventHandlerFactory.getService(event.getMainType());
            if (eventHandler == null) {
                result = ExecuteResult.fail("没有匹配的补偿事件处理器:" + event.getMainType());
            } else {
                result = eventHandler.handleEvent(event);
            }
        } catch (Throwable e) {
            log.error("执行补偿事件处理器异常:", e);
            result = ExecuteResult.error(e.getMessage());
        } finally {
            log.info("执行补偿事件结果:{}", result);
        }
        // 更新执行补偿事件结果
        return updateExecuteResult(event, result);
    }

    /**
     * 更新执行结果
     *
     * @param event
     *            补偿事件
     * @param result
     *            执行结果
     * @return 更新结果
     */
    private ExecuteResult updateExecuteResult(CompensationEvent event, ExecuteResult result) {
        if (result == null) {
            return null;
        }
        try {
            switch (result.getStatus()) {
                case SUCCESS:
                    updateExecuteSuccess(event);
                    break;
                case FAIL:
                    updateExecuteFail(event, result.getErrorMessage());
                    break;
                case ERROR:
                    updateExecuteError(event, result.getErrorMessage());
                    break;
                case IGNORE:
                    log.info("事件忽略执行:{}", result.getErrorMessage());
                    break;
                default:
                    result = ExecuteResult.error("执行补偿事件结果状态错误:" + result.getStatus());
                    updateExecuteError(event, result.getErrorMessage());
                    break;
            }
            return result;
        } catch (Throwable e) {
            log.error("更新执行结果异常:", e);
            return ExecuteResult.error(e.getMessage());
        }
    }

    /**
     * 更新补偿事件为异常,校验重试次数
     *
     * @param event
     *            待更新补偿事件
     * @param errorMessage
     *            错误消息
     */
    private void updateExecuteError(CompensationEvent event, String errorMessage) {
        if (compensationProperties.getRetryIntervals() == null
            || event.getExecuteCount() >= compensationProperties.getRetryIntervals().size()) {
            // 超过允许重试次数 直接更新为失败
            updateExecuteFail(event, errorMessage);
        } else {
            errorMessage = StringUtils.abbreviate(errorMessage, ERROR_MESSAGE_MAX_WIDTH);
            // 延迟通知
            Duration allowDelayInterval = compensationProperties.getRetryIntervals().get(event.getExecuteCount());
            int count = compensationEventMapper.updateError(event.getEventId(), errorMessage,
                allowDelayInterval.getSeconds(), event.getExecuteStatus().getCode());
            if (count != 1) {
                log.warn("更新补偿事件为异常出现异常:{}", event);
            }
        }
    }

    /**
     * 更新补偿事件为失败
     *
     * @param event
     *            待更新补偿事件
     * @param errorMessage
     *            错误消息
     */
    private void updateExecuteFail(CompensationEvent event, String errorMessage) {
        errorMessage = StringUtils.abbreviate(errorMessage, ERROR_MESSAGE_MAX_WIDTH);
        int count =
            compensationEventMapper.updateFail(event.getEventId(), errorMessage, event.getExecuteStatus().getCode());
        if (count != 1) {
            log.error("更新补偿事件为失败出现异常:{}", event);
        } else {
            log.info("更新补偿事件为失败");
        }
    }

    /**
     * 更新执行成功状态,对于执行成功补偿事件进行删除
     *
     * @param event
     *            补偿事件
     */
    private void updateExecuteSuccess(CompensationEvent event) {
        int count = compensationEventMapper.deleteByEventId(event.getEventId());
        if (count != 1) {
            log.error("删除补偿事件异常:{}", event);
        }
    }

    @Override
    public void asyncExecute(CompensationEvent event) {
        threadPoolTaskExecutor.execute(() -> execute(event));
    }

    @Override
    public BatchExecuteResult batchExecute(List<CompensationEvent> events) {
        List<Future<ExecuteResult>> futures = new ArrayList<>();
        for (CompensationEvent event : events) {
            Future<ExecuteResult> future = threadPoolTaskExecutor.submit(() -> execute(event));
            futures.add(future);
        }
        BatchExecuteResult result = new BatchExecuteResult();
        for (Future<ExecuteResult> future : futures) {
            try {
                ExecuteResult itemResult = future.get();
                if (itemResult == null) {
                    result.increaseSkipCount();
                } else {
                    result.increaseCount(itemResult.getStatus());
                }
            } catch (Throwable e) {
                result.increaseErrorCount();
            }
        }
        return result;
    }
}
