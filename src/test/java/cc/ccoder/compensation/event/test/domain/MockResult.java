package cc.ccoder.compensation.event.test.domain;

import cc.ccoder.compensation.event.test.domain.enums.MockStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: congcong
 * @Email: congccoder@gmail.com
 * @Date: 2022/3/31 17:14
 */
@Data
@AllArgsConstructor
public class MockResult {

    private MockStatus mockStatus;

    private String errorMessage;

    public static MockResult success() {
        return new MockResult(MockStatus.SUCCESS, null);
    }

    public static MockResult fail(String errorMessage) {
        return new MockResult(MockStatus.FAIL, errorMessage);
    }

    public static MockResult error(String errorMessage) {
        return new MockResult(MockStatus.ERROR, errorMessage);
    }

    public static MockResult exception(String errorMessage) {
        return new MockResult(MockStatus.EXCEPTION, errorMessage);
    }

    public static MockResult ignore(String errorMessage) {
        return new MockResult(MockStatus.IGNORE, errorMessage);
    }

}
