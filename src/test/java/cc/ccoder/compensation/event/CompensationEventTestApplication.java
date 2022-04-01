package cc.ccoder.compensation.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @Author: congcong
 * @Date: 2022/3/31
 * @Email: congccoder@gmail.com
 */
@Import(CompensationEventAutoConfiguration.class)
@SpringBootApplication()
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CompensationEventTestApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CompensationEventTestApplication.class);
        application.run(args);
    }

}
