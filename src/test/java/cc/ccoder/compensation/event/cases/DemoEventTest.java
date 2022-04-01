package cc.ccoder.compensation.event.cases;

import cc.ccoder.compensation.event.ApplicationTestBase;
import cc.ccoder.compensation.event.CompensationEventTestApplication;
import cc.ccoder.compensation.event.domain.CompensationEvent;
import cc.ccoder.compensation.event.service.CompensationEventService;
import cc.ccoder.compensation.event.test.eventhandler.DemoEventHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @Author: congcong
 * @Email: congccoder@gmail.com
 * @Date: 2022/3/31 17:59
 */
@SpringBootTest(classes = CompensationEventTestApplication.class)
public class DemoEventTest extends ApplicationTestBase {

    @Autowired
    private CompensationEventService compensationEventService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void testBusiness() {
        CompensationEvent event = DemoEventHandler.templateEvent().build();
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                // 执行业务订单表操作:新增或者更新操作

                // 同步保存本地补偿事件
                compensationEventService.saveEvent(event);
            }
        });
        //同步执行本地补偿事件(也可选择异步执行)
        compensationEventService.asyncExecute(event);
    }
}
