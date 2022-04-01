package cc.ccoder.compensation.event.test.eventhandler;

import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.ExecuteResult;
import cc.ccoder.compensation.event.service.CompensationEventHandler;
import cc.ccoder.compensation.event.test.domain.MockResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: congcong
 * @Email: congccoder@gmail.com
 * @Date: 2022/3/31 17:26
 */
@Slf4j
@Service
public class TestEventHandler implements CompensationEventHandler {

    private static final String MAIN_TYPE = "EVENT_UNIT_TYPE";

    protected static final AtomicInteger COUNTER = new AtomicInteger();

    @Override
    public String getServiceCode() {
        return MAIN_TYPE;
    }

    private Map<String, LinkedList<MockResult>> mockResultMap = new HashMap<>();

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
    public ExecuteResult handleEvent(CompensationEvent compensationEvent) {
        // 处理业务逻辑
        log.info("待处理补偿事件:{}", compensationEvent);
        if (!mockResultMap.containsKey(compensationEvent.getEventKey())) {
            return ExecuteResult.fail("暂未mock结果:" + this);
        }
        MockResult mockResult = mockResultMap.get(compensationEvent.getEventKey()).poll();
        if (mockResult == null) {
            return ExecuteResult.fail("无更多mock结果");
        }
        switch (mockResult.getMockStatus()) {
            case SUCCESS:
                log.info("mock结果:SUCCESS");
                return ExecuteResult.success();
            case FAIL:
                log.info("mock结果:FAIL");
                return ExecuteResult.fail(mockResult.getErrorMessage());
            case ERROR:
                log.info("mock结果:ERROR");
                return ExecuteResult.error(mockResult.getErrorMessage());
            case EXCEPTION:
                log.info("mock结果:EXCEPTION");
                return ExecuteResult.error(mockResult.getErrorMessage());
            case IGNORE:
                log.info("mock结果:IGNORE");
                return ExecuteResult.ignore(mockResult.getErrorMessage());
            default:
                return ExecuteResult.fail("mock结果状态未知");
        }
    }

    /**
     * mock构造一个执行结果
     * 
     * @param eventKey
     *            业务标志
     * @param mockResults
     *            mock结果状态
     */
    public void mockResult(String eventKey, MockResult... mockResults) {
        if (!mockResultMap.containsKey(eventKey)) {
            mockResultMap.put(eventKey, new LinkedList<>());
        }
        for (MockResult mockResult : mockResults) {
            log.info("mock处理结果:{},{},{}", eventKey, mockResult, this);
            mockResultMap.get(eventKey).add(mockResult);
        }
    }
}
