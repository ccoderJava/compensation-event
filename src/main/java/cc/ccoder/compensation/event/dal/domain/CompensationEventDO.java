package cc.ccoder.compensation.event.dal.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * <p>
 * 补偿事件消息表记录DO
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 16:38
 */
public class CompensationEventDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 事件消息ID
     */
    private Long eventId;

    /**
     * 事件主标志,与事件主类型(mainType)和子类型(subType)组合唯一索引,一般该字段与业务单号进行关联
     */
    private String eventKey;
    /**
     * 事件主类型
     */
    private String mainType;
    /**
     * 事件子类型
     */
    private String subType;

    /**
     * 执行状态
     */
    private String executeStatus;

    /**
     * 执行次数
     */
    private Integer executeCount;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 扩展参数
     */
    private String extension;

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

    public String getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(String executeStatus) {
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
