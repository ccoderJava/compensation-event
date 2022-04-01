package cc.ccoder.compensation.event.domainservice.impl;

import cc.ccoder.compensation.event.dal.CompensationEventMapper;
import cc.ccoder.compensation.event.dal.domain.CompensationEventDO;
import cc.ccoder.compensation.event.domain.BatchExecuteResult;
import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.CompensationProperties;
import cc.ccoder.compensation.event.domain.convertor.CompensationEventConverter;
import cc.ccoder.compensation.event.domain.convertor.DomainConverterUtil;
import cc.ccoder.compensation.event.domainservice.CompensationEventExecutor;
import cc.ccoder.compensation.event.domainservice.CompensationEventRetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 18:01
 */
@Slf4j
@Service
public class CompensationEventRetryServiceImpl implements CompensationEventRetryService {

    private final CompensationProperties compensationProperties;

    private final CompensationEventExecutor compensationEventExecutor;

    private final CompensationEventMapper compensationEventMapper;

    private final CompensationEventConverter compensationEventConverter;

    public CompensationEventRetryServiceImpl(CompensationProperties compensationProperties,
        CompensationEventExecutor compensationEventExecutor, CompensationEventMapper compensationEventMapper,
        CompensationEventConverter compensationEventConverter) {
        this.compensationProperties = compensationProperties;
        this.compensationEventExecutor = compensationEventExecutor;
        this.compensationEventMapper = compensationEventMapper;
        this.compensationEventConverter = compensationEventConverter;
    }

    @Override
    public void retry(int shardingIndex, int shardingCount, List<String> handlerCodeWhiteList) {
        int retryBatchSize = compensationProperties.getRetryBatchSize();
        while (true) {
            List<CompensationEvent> events =
                loadRetryList(retryBatchSize, shardingIndex, shardingCount, handlerCodeWhiteList);
            if (events.isEmpty()) {
                log.info("无更多可执行补偿事件");
                return;
            }
            BatchExecuteResult batchExecuteResult = compensationEventExecutor.batchExecute(events);
            log.info("批次执行补偿事件结果:{}", batchExecuteResult);

            // 如果批次执行无成功事件,或查询结果数不足批次大小时，不再进行循环执行
            if (batchExecuteResult.getSuccessCount() == 0 || events.size() != retryBatchSize) {
                return;
            }

        }
    }

    private List<CompensationEvent> loadRetryList(int retryBatchSize, int shardingIndex, int shardingCount,
        List<String> handlerCodeWhiteList) {
        List<CompensationEventDO> events;
        if (handlerCodeWhiteList == null || handlerCodeWhiteList.size() == 0) {
            events = compensationEventMapper.listRetryEvent(retryBatchSize, shardingIndex, shardingCount);
        } else {
            events = compensationEventMapper.listRetryEventWithMainTypeList(retryBatchSize, shardingIndex,
                shardingCount, handlerCodeWhiteList);
        }
        return DomainConverterUtil.convertToBatchBO(events, compensationEventConverter);
    }
}
