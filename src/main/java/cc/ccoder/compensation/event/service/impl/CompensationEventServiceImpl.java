package cc.ccoder.compensation.event.service.impl;

import cc.ccoder.compensation.event.dal.CompensationEventMapper;
import cc.ccoder.compensation.event.dal.domain.CompensationEventDO;
import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.CompensationProperties;
import cc.ccoder.compensation.event.domain.ExecuteResult;
import cc.ccoder.compensation.event.domain.convertor.CompensationEventConverter;
import cc.ccoder.compensation.event.domainservice.CompensationEventExecutor;
import cc.ccoder.compensation.event.service.CompensationEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 补偿事件处理逻辑
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 16:55
 */
@Slf4j
@Service
public class CompensationEventServiceImpl implements CompensationEventService {

    private final CompensationEventConverter compensationEventConverter;

    private final CompensationEventMapper compensationEventMapper;

    private final CompensationProperties compensationProperties;

    private final CompensationEventExecutor compensationEventExecutor;

    public CompensationEventServiceImpl(CompensationEventConverter compensationEventConverter,
        CompensationEventMapper compensationEventMapper, CompensationProperties compensationProperties,
        CompensationEventExecutor compensationEventExecutor) {
        this.compensationEventConverter = compensationEventConverter;
        this.compensationEventMapper = compensationEventMapper;
        this.compensationProperties = compensationProperties;
        this.compensationEventExecutor = compensationEventExecutor;
    }

    @Override
    public void saveEvent(CompensationEvent event) {
        CompensationEventDO eventDO = compensationEventConverter.convertToDO(event);
        try {
            compensationEventMapper.insert(eventDO, compensationProperties.getAllowJobRetryDelay().getSeconds());
            event.setEventId(eventDO.getEventId());
        } catch (DuplicateKeyException e) {
            Long existId = compensationEventMapper.getEventIdByBizKey(eventDO.getEventKey(), eventDO.getMainType(),
                eventDO.getSubType());
            log.info("补偿事件保存重复:{},{},{},{}", eventDO.getEventKey(), eventDO.getMainType(), eventDO.getSubType(),
                existId);
            event.setEventId(existId);
        }

    }

    @Override
    public void asyncExecute(CompensationEvent event) {
        compensationEventExecutor.asyncExecute(event);

    }

    @Override
    public ExecuteResult syncExecute(CompensationEvent event) {
        return compensationEventExecutor.execute(event);
    }

    @Override
    public CompensationEvent queryEvent(String eventKey, String mainType, String subType) {
        return compensationEventConverter
            .convertToBO(compensationEventMapper.getEventByBizKey(eventKey, mainType, subType));
    }
}
