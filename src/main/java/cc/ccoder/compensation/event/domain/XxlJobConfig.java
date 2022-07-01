package cc.ccoder.compensation.event.domain;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @Author: congcong
 * @Email: congccoder@gmail.com
 * @Date: 2022/4/21 22:18
 */
@Slf4j
@Configuration
@ConditionalOnMissingBean(XxlJobSpringExecutor.class)
@ConditionalOnProperty(value = "xxl.job.enabled", havingValue = "true")
public class XxlJobConfig {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.appname:#{spring.application.name}}")
    private String appname;

    @Value("${xxl.job.executor.address}")
    private String address;

    @Value("${xxl.job.executor.ip:127.0.0.1}")
    private String ip;

    @Value("${xxl.job.executor.port:9999}")
    private int port;

    @Value("${xxl.job.executor.logpath:/data/weblog/java/#{spring.application.name}/xxl-job}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays:15}")
    private int logRetentionDays;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(InetUtils inetUtils) {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        //重要：获取可用IP而非网卡的第一个IP，具体参考最下方注释内容
        if (StringUtils.isEmpty(ip)) {
            ip = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        }
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        log.info("xxl-job config init.{}", xxlJobSpringExecutor);
        return xxlJobSpringExecutor;
    }
}
