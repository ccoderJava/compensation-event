package cc.ccoder.compensation.event.test.tool;

import cc.ccoder.compensation.event.dal.domain.CompensationEventDO;
import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.convertor.CompensationEventConverter;
import cc.ccoder.compensation.event.domain.enums.ExecuteStatus;
import cc.ccoder.compensation.event.test.domain.CompensationEventTestDO;
import org.junit.jupiter.api.Assertions;

/**
 * @Author: congcong
 * @Email: congccoder@gmail.com
 * @Date: 2022/3/31 17:21
 */
public class EventChecker {
    private static final CompensationEventConverter CONVERTER = new CompensationEventConverter();

    public EventChecker(CompensationEventTestDO event) {
        this.event = event;
    }

    private CompensationEventTestDO event;

    public EventChecker exist() {
        Assertions.assertNotNull(event);
        return this;
    }

    public EventChecker notExist() {
        Assertions.assertNull(event);
        return this;
    }

    public EventChecker exist(CompensationEvent out) {
        exist();
        CompensationEventDO expect = CONVERTER.convertToDO(out);
        Assertions.assertEquals(expect.getEventId(), event.getEventId());
        Assertions.assertEquals(expect.getEventKey(), event.getEventKey());
        Assertions.assertEquals(expect.getMainType(), event.getMainType());
        Assertions.assertEquals(expect.getSubType(), event.getSubType());
        Assertions.assertEquals(expect.getExtension(), event.getExtension());
        return this;
    }

    public EventChecker status(ExecuteStatus expectExecuteStatus) {
        Assertions.assertEquals(expectExecuteStatus.getCode(), event.getExecuteStatus());
        return this;
    }

    public EventChecker status(String expectExecuteStatus) {
        Assertions.assertEquals(expectExecuteStatus, event.getExecuteStatus());
        return this;
    }

    public EventChecker executeCount(int expectExecuteCount) {
        Assertions.assertEquals(expectExecuteCount, event.getExecuteCount().intValue());
        return this;
    }

    public EventChecker errorMessage(String expectErrorMessage) {
        Assertions.assertEquals(expectErrorMessage, event.getErrorMessage());
        return this;
    }
}
