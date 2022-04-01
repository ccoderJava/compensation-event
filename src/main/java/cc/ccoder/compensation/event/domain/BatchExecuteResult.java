package cc.ccoder.compensation.event.domain;

import cc.ccoder.compensation.event.domain.enums.ExecuteStatus;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>
 * 补偿事件批次执行结果
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 20:47
 */
public class BatchExecuteResult {

    /**
     * 尝试次数
     */
    private int tryCount = 0;

    /**
     * 成功次数
     */
    private int successCount = 0;

    /**
     * 异常次数
     */
    private int errorCount = 0;

    /**
     * 失败次数
     */
    private int failCount = 0;

    /**
     * 跳过次数
     */
    private int skipCount = 0;

    public void increaseSkipCount() {
        tryCount++;
        skipCount++;
    }

    public void increaseErrorCount() {
        tryCount++;
        errorCount++;
    }

    public void increaseCount(ExecuteStatus status) {
        tryCount++;
        switch (status) {
            case ERROR:
                errorCount++;
                break;
            case FAIL:
                failCount++;
                break;
            case SUCCESS:
                successCount++;
                break;
            default:
                break;
        }
    }

    public int getSuccessCount() {
        return successCount;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
