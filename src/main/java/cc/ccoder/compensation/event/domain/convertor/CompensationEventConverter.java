package cc.ccoder.compensation.event.domain.convertor;

import cc.ccoder.compensation.event.dal.domain.CompensationEventDO;
import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.domain.enums.ExecuteStatus;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 19:05
 */
@Component
public class CompensationEventConverter implements DomainConverter<CompensationEvent, CompensationEventDO> {

    static final int ERROR_MESSAGE_LENGTH = 128;

    @Override
    public CompensationEventDO convertToDO(CompensationEvent domain) {
        if (domain == null) {
            return null;
        }
        CompensationEventDO result = new CompensationEventDO();
        result.setEventId(domain.getEventId());
        result.setEventKey(domain.getEventKey());
        result.setMainType(domain.getMainType());
        result.setSubType(domain.getSubType());
        result.setExecuteCount(domain.getExecuteCount());
        result.setExecuteStatus(domain.getExecuteStatus().getCode());
        result.setErrorMessage(StringUtils.abbreviate(domain.getErrorMessage(), ERROR_MESSAGE_LENGTH));
        result.setExtension(JSON.toJSONString(domain.getExtension()));
        return result;

    }

    @Override
    public CompensationEvent convertToBO(CompensationEventDO domain) {
        if (domain == null) {
            return null;
        }
        CompensationEvent result = new CompensationEvent();
        result.setEventId(domain.getEventId());
        result.setEventKey(domain.getEventKey());
        result.setMainType(domain.getMainType());
        result.setSubType(domain.getSubType());
        result.setExecuteCount(domain.getExecuteCount());
        result.setExecuteStatus(ExecuteStatus.getByCode(domain.getExecuteStatus()));
        result.setErrorMessage(domain.getErrorMessage());
        result.setExtension(JSON.parseObject(domain.getExtension(), Map.class));
        return result;
    }
}
