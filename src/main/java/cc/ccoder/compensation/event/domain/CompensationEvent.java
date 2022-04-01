package cc.ccoder.compensation.event.domain;

import cc.ccoder.compensation.event.domain.enums.ExecuteStatus;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 补偿事件
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 17:44
 */
public class CompensationEvent {

    public static CompensationEvent.Builder newBuilder(String eventKey, String mainType, String subType) {
        return new CompensationEvent.Builder(eventKey, mainType, subType);
    }

    /**
     * 事件ID
     */
    private Long eventId;

    /**
     * 事件主体标识，与事件类型和子类型组合唯一
     */
    private String eventKey;

    /**
     * 事件主类型，非空
     */
    private String mainType;

    /**
     * 事件子类型，非空
     */
    private String subType;

    /**
     * 扩展信息
     */
    private Map<String, String> extension;

    /**
     * 执行状态
     */
    private ExecuteStatus executeStatus;

    /**
     * 执行次数
     */
    private Integer executeCount;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 执行前不需要检查数据库
     */
    private boolean skipCheckBeforeExecute;

    public static class Builder {
        public Builder(String eventKey, String mainType, String subType) {
            result.setEventKey(eventKey);
            result.setMainType(mainType);
            result.setSubType(subType);
            result.setSkipCheckBeforeExecute(true);
            result.setExecuteStatus(ExecuteStatus.INIT);
            result.setExecuteCount(0);
        }

        CompensationEvent result = new CompensationEvent();

        public Builder checkBeforeExecute() {
            result.setSkipCheckBeforeExecute(false);
            return this;
        }

        public Builder fail(String errorMessage) {
            result.setExecuteStatus(ExecuteStatus.FAIL);
            result.setErrorMessage(errorMessage);
            return this;
        }

        public Builder param(String key, String value) {
            if (result.getExtension() == null) {
                result.setExtension(new HashMap<>());
            }
            result.getExtension().put(key, value);
            return this;
        }

        public CompensationEvent build() {
            return result;
        }
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getMainType() {
        return mainType;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Map<String, String> getExtension() {
        return extension;
    }

    public void setExtension(Map<String, String> extension) {
        this.extension = extension;
    }

    public ExecuteStatus getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(ExecuteStatus executeStatus) {
        this.executeStatus = executeStatus;
    }

    public Integer getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(Integer executeCount) {
        this.executeCount = executeCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSkipCheckBeforeExecute() {
        return skipCheckBeforeExecute;
    }

    public void setSkipCheckBeforeExecute(boolean skipCheckBeforeExecute) {
        this.skipCheckBeforeExecute = skipCheckBeforeExecute;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
