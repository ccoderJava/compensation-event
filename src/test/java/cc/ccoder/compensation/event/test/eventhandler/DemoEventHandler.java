package cc.ccoder.compensation.event.test.eventhandler;

import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.ExecuteResult;
import cc.ccoder.compensation.event.service.CompensationEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: congcong
 * @Email: congccoder@gmail.com
 * @Date: 2022/3/31 17:23
 */
@Slf4j
@Service
public class DemoEventHandler implements CompensationEventHandler {

    private static final String MAIN_TYPE = "DEMO_EVENT_MAIN_TYPE";

    protected static final AtomicInteger COUNTER = new AtomicInteger();

    /**
     * 创建一个mock模板的补偿事件
     *
     * @return 补偿事件
     */
    public static CompensationEvent.Builder templateEvent() {
        String eventKey = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "_"
            + COUNTER.incrementAndGet();
        // 一般subType不使用时，用占位符就行了
        return CompensationEvent.newBuilder(eventKey, MAIN_TYPE, "-");
    }

    @Override
    public String getServiceCode() {
        return MAIN_TYPE;
    }

    @Override
    public ExecuteResult handleEvent(CompensationEvent compensationEvent) {
        // 补偿事件处理业务
        log.info("补偿事件处理业务,待执行补偿事件:{}", compensationEvent);
        return ExecuteResult.success();
    }
}
