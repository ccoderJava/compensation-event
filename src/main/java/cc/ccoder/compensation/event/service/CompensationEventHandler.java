package cc.ccoder.compensation.event.service;

import cc.ccoder.compensation.event.common.CodeService;
import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.ExecuteResult;

/**
 * <p>
 * 事件处理器对外接口
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 16:53
 */
public interface CompensationEventHandler extends CodeService {

    /**
     * 事件处理器,业务执行事件后返回执行结果
     * <ul>
     * <li>执行成功，事件会从数据库中删除</li>
     * <li>执行失败，不再进行触发执行</li>
     * <li>执行异常，按照重试策略再次触发，直到超过重试次数变为执行失败</li>
     * </ul>
     *
     * @param compensationEvent
     *            待执行事件
     * @return 执行结果
     */
    ExecuteResult handleEvent(CompensationEvent compensationEvent);
}
