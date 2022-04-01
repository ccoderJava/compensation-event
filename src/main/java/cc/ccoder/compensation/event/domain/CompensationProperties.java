package cc.ccoder.compensation.event.domain;

import lombok.Data;
import org.apache.commons.lang3.Validate;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;

/**
 * <p>
 * 补偿机制配置信息
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/29 22:22
 */
@Data
@ConfigurationProperties(prefix = "compensation.event")
public class CompensationProperties {

    /**
     * 数据源名称
     */
    private String datasourceBeanName;

    /**
     * 补偿事件落库后允许定时任务获取并且执行的延迟，可以一定程度的避免异步处理和定时任务的同时执行。
     */
    private Duration allowJobRetryDelay;

    /**
     * 执行事件线程池配置
     */
    private PoolConfig poolConfig;

    /**
     * 事件重试执行批次大小
     */
    private int retryBatchSize;

    /**
     * 事件重试执行间隔时间
     */
    private List<Duration> retryIntervals;

    /**
     * 校验信息
     */
    @PostConstruct
    public void check() {
        Validate.notNull(allowJobRetryDelay, "配置allowJobRetryDelay不可为空");
        Validate.notNull(poolConfig, "配置事件执行线程池poolConfig不可为空");
        Validate.isTrue(poolConfig.corePoolSize > 0, "线程池配置corePoolSize不可小于0");
        Validate.isTrue(poolConfig.maxPoolSize > 0, "线程池配置corePoolSize不可小于0");
        Validate.isTrue(poolConfig.queueCapacity > 0, "线程池配置queueCapacity不可小于0");
        Validate.isTrue(retryBatchSize > 0, "配置事件执行重试批次retryBatchSize有误");

    }

    /**
     * 连接池配置
     */
    public static class PoolConfig {
        private int corePoolSize;
        private int maxPoolSize;
        private int queueCapacity;
        private int keepAliveSeconds;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

        public int getKeepAliveSeconds() {
            return keepAliveSeconds;
        }

        public void setKeepAliveSeconds(int keepAliveSeconds) {
            this.keepAliveSeconds = keepAliveSeconds;
        }
    }

    public String getDatasourceBeanName() {
        return datasourceBeanName;
    }

    public void setDatasourceBeanName(String datasourceBeanName) {
        this.datasourceBeanName = datasourceBeanName;
    }

    public Duration getAllowJobRetryDelay() {
        return allowJobRetryDelay;
    }

    public void setAllowJobRetryDelay(Duration allowJobRetryDelay) {
        this.allowJobRetryDelay = allowJobRetryDelay;
    }

    public PoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(PoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public int getRetryBatchSize() {
        return retryBatchSize;
    }

    public void setRetryBatchSize(int retryBatchSize) {
        this.retryBatchSize = retryBatchSize;
    }

    public List<Duration> getRetryIntervals() {
        return retryIntervals;
    }

    public void setRetryIntervals(List<Duration> retryIntervals) {
        this.retryIntervals = retryIntervals;
    }
}
