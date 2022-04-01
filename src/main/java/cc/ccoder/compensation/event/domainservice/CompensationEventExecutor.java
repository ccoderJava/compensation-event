package cc.ccoder.compensation.event.domainservice;

import cc.ccoder.compensation.event.domain.BatchExecuteResult;
import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.ExecuteResult;

import java.util.List;

/**
 * <p>
 * 补偿事件执行器
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 18:00
 */
public interface CompensationEventExecutor {

    /**
     * 同步执行
     *
     * @param event
     *            事件
     * @return 执行结果
     */
    ExecuteResult execute(CompensationEvent event);

    /**
     * 异步执行，如果提交异常，不报错
     *
     * @param event
     *            事件
     */
    void asyncExecute(CompensationEvent event);

    /**
     * 批量执行，全部执行结束后返回
     *
     * @param events
     *            事件列表
     * @return 批次执行结果
     */
    BatchExecuteResult batchExecute(List<CompensationEvent> events);

}
