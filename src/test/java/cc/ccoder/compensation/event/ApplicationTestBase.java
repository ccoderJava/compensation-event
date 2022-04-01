package cc.ccoder.compensation.event;

import cc.ccoder.compensation.event.domain.ExecuteResult;
import cc.ccoder.compensation.event.domain.convertor.CompensationEventConverter;
import cc.ccoder.compensation.event.domain.enums.ExecuteStatus;
import cc.ccoder.compensation.event.test.eventhandler.TestEventHandler;
import cc.ccoder.compensation.event.test.repository.TestEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @Author: chencong
 * @Date: 2022/3/31
 * @Email: congccoder@gmail.com
 */
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApplicationTestBase {

    @Autowired
    protected TestEventRepository testEventRepository;

    @Autowired
    protected TestEventHandler testEventHandler;

    @Autowired
    protected CompensationEventConverter compensationEventConverter;

    /**
     * 断言判断执行结果
     * 
     * @param result
     *            执行结果
     * @param expectExecuteStatus
     *            校验执行状态
     * @param expectErrorMessage
     *            校验执行错误消息
     */
    protected void assertExecuteResult(ExecuteResult result, ExecuteStatus expectExecuteStatus,
        String expectErrorMessage) {
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectExecuteStatus, result.getStatus());
        Assertions.assertEquals(expectErrorMessage, result.getErrorMessage());
    }
}
