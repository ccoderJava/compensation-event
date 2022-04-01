package cc.ccoder.compensation.event.test.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * @Author: congcong
 * @Email: congccoder@gmail.com
 * @Date: 2022/3/31 17:13
 */
public class CompensationEventTestDO {
    /** 事件ID */
    private Long eventId;

    /** 事件主体标识，与事件类型和子类型组合唯一 */
    private String eventKey;

    /** 事件主类型，非空 */
    private String mainType;

    /** 事件子类型，非空 */
    private String subType;

    /** 执行状态 */
    private String executeStatus;

    /** 执行次数 */
    private Integer executeCount;

    /** 扩展信息 */
    private String extension;

    /** 允许时间 */
    private Date allowTime;

    /** 错误信息 */
    private String errorMessage;

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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Date getAllowTime() {
        return allowTime;
    }

    public void setAllowTime(Date allowTime) {
        this.allowTime = allowTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
