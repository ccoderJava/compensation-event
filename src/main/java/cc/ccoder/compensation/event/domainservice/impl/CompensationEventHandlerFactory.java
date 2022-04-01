package cc.ccoder.compensation.event.domainservice.impl;

import cc.ccoder.compensation.event.common.factory.CodeServiceFactory;
import cc.ccoder.compensation.event.service.CompensationEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 补偿事件处理器工厂
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 18:01
 */
@Slf4j
@Service
public class CompensationEventHandlerFactory
    implements CodeServiceFactory<CompensationEventHandler>, ApplicationListener<ContextRefreshedEvent> {

    private final Map<String, CompensationEventHandler> EVENT_HANDLER_MAP = new HashMap<>();

    @Override
    public CompensationEventHandler getService(String serviceCode) {
        return EVENT_HANDLER_MAP.get(serviceCode);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, CompensationEventHandler> providers = context.getBeansOfType(CompensationEventHandler.class);
        log.info("初始化工厂服务：补偿事件处理器工厂");
        if (providers.size() == 0) {
            return;
        }
        for (Map.Entry<String, CompensationEventHandler> provider : providers.entrySet()) {
            String serviceCode = provider.getValue().getServiceCode();
            if (serviceCode == null) {
                throw new IllegalArgumentException(String.format("服务注册编码serviceCode不可为空: %s", provider.getClass()));
            }
            if (!EVENT_HANDLER_MAP.containsKey(serviceCode)) {
                EVENT_HANDLER_MAP.put(serviceCode, provider.getValue());
                log.info("已注册服务:{}, {}", serviceCode, provider.getClass());
            } else {
                throw new IllegalArgumentException(String.format("注册服务重复: %s, %s, %s", serviceCode,
                    EVENT_HANDLER_MAP.get(serviceCode).getClass(), provider.getClass()));
            }
        }
    }
}
