package cc.ccoder.compensation.event.domain.enums;

/**
 * <p>
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 17:45
 */
public enum ExecuteStatus implements CodeEnum {
    /**
     * 初始
     */
    INIT("I"),

    /**
     * 通知成功
     */
    SUCCESS("S"),

    /**
     * 通知异常
     */
    ERROR("E"),

    /**
     * 通知失败
     */
    FAIL("F"),

    /**
     * 忽略，数据库不会更新为此状态，可以使用在长时间任务，未取得互斥锁的场景
     */
    IGNORE(""),

    ;

    private final String code;

    ExecuteStatus(String code) {
        this.code = code;
    }

    public static ExecuteStatus getByCode(String code) {
        for (ExecuteStatus resultCode : ExecuteStatus.values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

}
