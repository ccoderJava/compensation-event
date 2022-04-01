package cc.ccoder.compensation.event;

import cc.ccoder.compensation.event.dal.CompensationEventMapper;
import cc.ccoder.compensation.event.domain.CompensationProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanExpressionException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * <p>
 * 补偿事件机制自动装配
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/29 22:02
 */
@Configuration
@ConditionalOnProperty(prefix = "compensation.event", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(value = CompensationProperties.class)
@ComponentScan("cc.ccoder.compensation.event")
public class CompensationEventAutoConfiguration {

    private final CompensationProperties compensationProperties;

    private final ApplicationContext applicationContext;

    public CompensationEventAutoConfiguration(CompensationProperties compensationProperties,
        ApplicationContext applicationContext) {
        this.compensationProperties = compensationProperties;
        this.applicationContext = applicationContext;
    }

    @Bean
    public CompensationEventMapper compensationEventMapper() throws Exception {
        DataSource dataSource;
        if (StringUtils.isBlank(compensationProperties.getDatasourceBeanName())) {
            dataSource = applicationContext.getBean(DataSource.class);
        } else {
            dataSource = applicationContext.getBean(compensationProperties.getDatasourceBeanName(), DataSource.class);
        }
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();

        MapperFactoryBean<CompensationEventMapper> mapperFactoryBean = new MapperFactoryBean<>();
        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
        mapperFactoryBean.setMapperInterface(CompensationEventMapper.class);
        mapperFactoryBean.afterPropertiesSet();
        return mapperFactoryBean.getObject();

    }

}
