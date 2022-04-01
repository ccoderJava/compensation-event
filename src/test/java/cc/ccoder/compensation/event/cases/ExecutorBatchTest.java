package cc.ccoder.compensation.event.cases;

import cc.ccoder.compensation.event.ApplicationTestBase;
import cc.ccoder.compensation.event.CompensationEventTestApplication;
import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.ExecuteResult;
import cc.ccoder.compensation.event.domain.enums.ExecuteStatus;
import cc.ccoder.compensation.event.domainservice.CompensationEventExecutor;
import cc.ccoder.compensation.event.domainservice.CompensationEventRetryService;
import cc.ccoder.compensation.event.service.CompensationEventService;
import cc.ccoder.compensation.event.test.domain.MockResult;
import cc.ccoder.compensation.event.test.eventhandler.TestEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: congcong
 * @Email: congccoder@gmail.com
 * @Date: 2022/3/31 19:47
 */
@Slf4j
@SpringBootTest(classes = CompensationEventTestApplication.class)
public class ExecutorBatchTest extends ApplicationTestBase {

    @Autowired
    private CompensationEventService compensationEventService;

    @Autowired
    private CompensationEventExecutor compensationEventExecutor;

    @Autowired
    private CompensationEventRetryService compensationEventRetryService;

    @BeforeAll
    public void clear() {
        testEventRepository.deleteAllEvent();
    }

    @Test
    public void testRetryToFail() {
        CompensationEvent event = TestEventHandler.templateEvent().build();

        // Compensation.event.retryIntervals 配置间隔时间次数 下面执行次数 异常E转变为F

        String[] exceptionArrays = {"mock runtimeException", "mock illegalArgumentException", "mock othersException"};
        // 下面mock再次执行 并且再次都是失败
        testEventHandler.mockResult(event.getEventKey(), MockResult.exception(exceptionArrays[0]),
            MockResult.exception(exceptionArrays[1]), MockResult.exception(exceptionArrays[2]));

        // 落库补偿事件
        compensationEventService.saveEvent(event);
        // 初始化执行状态及执行次数校验
        testEventRepository.getEventChecker(event.getEventId()).exist().status(ExecuteStatus.INIT).executeCount(0);

        log.info("===1.第一次执行===");
        ExecuteResult result = compensationEventExecutor.execute(event);
        assertExecuteResult(result, ExecuteStatus.ERROR, exceptionArrays[0]);
        testEventRepository.getEventChecker(event.getEventId()).exist(event).status(ExecuteStatus.ERROR)
            .executeCount(1);

        log.info("===2.第二次执行重试,不满足允许执行时间===");
        compensationEventRetryService.retry(0, 1, null);
        testEventRepository.getEventChecker(event.getEventId()).exist(event).status(ExecuteStatus.ERROR).executeCount(1)
            .errorMessage(exceptionArrays[0]);

        log.info("===3.第三次执行重试,等待5秒重试===");
        sleep(5);
        compensationEventRetryService.retry(0, 1, null);
        testEventRepository.getEventChecker(event.getEventId()).exist(event).status(ExecuteStatus.ERROR).executeCount(2)
            .errorMessage(exceptionArrays[1]);

        log.info("===4.第四次执行重试,不满足允许执行时间===");
        compensationEventRetryService.retry(0, 1, null);
        testEventRepository.getEventChecker(event.getEventId()).exist(event).status(ExecuteStatus.ERROR).executeCount(2)
            .errorMessage(exceptionArrays[1]);

        log.info("===5.第五次执行重试,等待10秒重试,超过次数修改为失败===");
        sleep(10);
        compensationEventRetryService.retry(0, 1, null);
        testEventRepository.getEventChecker(event.getEventId()).exist(event).status(ExecuteStatus.FAIL).executeCount(3)
            .errorMessage(exceptionArrays[2]);

    }

    @Test
    public void testRetryToSuccess() {
        CompensationEvent event = TestEventHandler.templateEvent().build();
        String exception = "mock exception";

        // mock 第一次执行异常 第二次执行成功
        testEventHandler.mockResult(event.getEventKey(), MockResult.exception(exception), MockResult.success());

        // 落库
        compensationEventService.saveEvent(event);
        testEventRepository.getEventChecker(event.getEventId()).exist(event).status(ExecuteStatus.INIT).executeCount(0);

        log.info("===1.第一次执行异常===");
        ExecuteResult result = compensationEventExecutor.execute(event);
        assertExecuteResult(result, ExecuteStatus.ERROR, exception);
        testEventRepository.getEventChecker(event.getEventId()).exist(event).status(ExecuteStatus.ERROR).executeCount(1)
            .errorMessage(exception);

        log.info("===2.第二次执行成功,等五秒重试成功");
        sleep(5);
        compensationEventRetryService.retry(0, 1, null);
        testEventRepository.getEventChecker(event.getEventId()).notExist();
    }

    private void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignored) {
        }
    }

}
