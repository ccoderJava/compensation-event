package cc.ccoder.compensation.event.service;

import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.ExecuteResult;

/**
 * <p>
 * 补偿事件处理接口，提供给外部对于事件的操作接口。
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 16:54
 */
public interface CompensationEventService {

    /**
     * 保存补偿事件
     *
     * @param event
     *            待保存事件
     */
    void saveEvent(CompensationEvent event);

    /**
     * 异步执行事件
     *
     * @param event
     *            待执行事件
     */
    void asyncExecute(CompensationEvent event);

    /**
     * 同步执行事件
     *
     * @param event
     *            待执行事件
     * @return 执行结果
     */
    ExecuteResult syncExecute(CompensationEvent event);

    /**
     * 查询事件
     *
     * @param eventKey
     *            事件标志
     * @param mainType
     *            事件主业务类型
     * @param subType
     *            事件子业务类型
     * @return 如果不存在则返回null
     */
    CompensationEvent queryEvent(String eventKey, String mainType, String subType);

}
