package cc.ccoder.compensation.event.domain;

import cc.ccoder.compensation.event.domain.enums.ExecuteStatus;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>
 * 补偿事件执行结果
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 17:55
 */
public class ExecuteResult {

    private final ExecuteStatus status;

    private final String errorMessage;

    public ExecuteResult(ExecuteStatus status, String errorMessage) {
        super();
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static ExecuteResult success() {
        return new ExecuteResult(ExecuteStatus.SUCCESS, null);
    }

    public static ExecuteResult fail(String errorMessage) {
        return new ExecuteResult(ExecuteStatus.FAIL, errorMessage);
    }

    public static ExecuteResult error(String errorMessage) {
        return new ExecuteResult(ExecuteStatus.ERROR, errorMessage);
    }

    public static ExecuteResult ignore(String errorMessage) {
        return new ExecuteResult(ExecuteStatus.IGNORE, errorMessage);
    }

    public ExecuteStatus getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
