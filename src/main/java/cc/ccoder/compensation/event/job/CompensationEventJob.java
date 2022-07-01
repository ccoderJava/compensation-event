package cc.ccoder.compensation.event.job;

import cc.ccoder.compensation.event.domainservice.CompensationEventRetryService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: chencong
 * @Date: 2022/3/31
 * @Email: congccoder@gmail.com
 */
@Slf4j
@Component
public class CompensationEventJob {

    private final CompensationEventRetryService compensationEventRetryService;

    @Value("${compensation.event.job.handlerCodeWhiteList:}")
    private List<String> handlerCodeWhiteList;

    public CompensationEventJob(CompensationEventRetryService compensationEventRetryService) {
        this.compensationEventRetryService = compensationEventRetryService;
    }

    @XxlJob("compensationEventHandlerJob")
    public ReturnT<String> compensationEventHandler(String params) {
        log.info("重试处理器参数:{}", params);
        ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
        if (null == shardingVO) {
            shardingVO = new ShardingUtil.ShardingVO(0, 1);
            log.warn("分片参数为空,默认使用单片场景处理");
        }
        compensationEventRetryService.retry(shardingVO.getIndex(), shardingVO.getTotal(), handlerCodeWhiteList);
        return ReturnT.SUCCESS;
    }
}
