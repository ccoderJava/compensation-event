package cc.ccoder.compensation.event.test.repository;

import cc.ccoder.compensation.event.domain.convertor.CompensationEventConverter;
import cc.ccoder.compensation.event.test.dal.mapper.TestEventMapper;
import cc.ccoder.compensation.event.test.tool.EventChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Author: congcong
 * @Email: congccoder@gmail.com
 * @Date: 2022/3/31 17:18
 */
@Slf4j
@Repository
public class TestEventRepository {

    @Autowired
    private TestEventMapper testEventMapper;

    public void deleteEvent(Long eventId) {
        testEventMapper.deleteByEventId(eventId);
    }

    public EventChecker getEventChecker(Long eventId) {
        return new EventChecker(testEventMapper.getByEventId(eventId));
    }

    public void updateStatusAndAllowTime(Long eventId, String status, long delayNowSeconds) {
        log.info("更新补偿事件状态:{}-{}", eventId, status);
        int count = testEventMapper.updateStatusAndAllowTime(eventId, status, delayNowSeconds);
        if (count != 1) {
            throw new RuntimeException("更新状态异常");
        }
    }

    public void deleteAllEvent() {
        int count = testEventMapper.deleteAllEvent();
        log.info("删除所有补偿事件:{}", count);
    }

}
