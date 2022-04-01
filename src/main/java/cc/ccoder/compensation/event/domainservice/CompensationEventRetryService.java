package cc.ccoder.compensation.event.domainservice;

import java.util.List;

/**
 * <p>
 * 补偿事件重试执行服务
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 18:00
 */
public interface CompensationEventRetryService {

    /**
     * 重试
     * 
     * @param shardingIndex
     *            当前分片,从0开始
     * @param shardingCount
     *            分片数
     * @param handlerCodeWhiteList
     *            处理服务mainType白名单,如果不为空则只执行白名单中任务
     */
    void retry(int shardingIndex, int shardingCount, List<String> handlerCodeWhiteList);

}
