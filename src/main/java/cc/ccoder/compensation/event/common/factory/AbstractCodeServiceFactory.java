package cc.ccoder.compensation.event.common.factory;

import cc.ccoder.compensation.event.common.CodeService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 抽象工厂方法，通过serviceCode获取到相应的服务工厂
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 18:34
 */
@Slf4j
public abstract class AbstractCodeServiceFactory<Provider extends CodeService> implements CodeServiceFactory<Provider> {

    private final Map<String, Provider> serviceProviderMap = new HashMap<>();

    public AbstractCodeServiceFactory(List<Provider> providerList) {
        initializeProviderMap(providerList);
    }

    private void initializeProviderMap(List<Provider> providerList) {
        log.info("initialized factory service:{}", getFactoryName());
        if (providerList == null) {
            return;
        }
        for (Provider provider : providerList) {
            String serviceCode = provider.getServiceCode();
            if (serviceCode == null) {
                throw new IllegalArgumentException(
                    String.format("Registration service code cannot be empty: %s", provider.getClass()));
            }
            if (!serviceProviderMap.containsKey(serviceCode)) {
                serviceProviderMap.put(serviceCode, provider);
                log.info("Registered service:{},{}", serviceCode, provider.getClass());
            } else {
                throw new IllegalArgumentException(String.format("Duplicate registration service: %s, %s, %s",
                    serviceCode, serviceProviderMap.get(serviceCode).getClass(), provider.getClass()));
            }
        }

    }

    /**
     * 获取服务 服务接口不存在时返回null
     *
     * @param serviceCode
     *            服务编码
     * @return 服务接口
     */
    @Override
    public Provider getService(String serviceCode) {
        return serviceProviderMap.get(serviceCode);
    }

    /**
     * 服务工厂名称
     *
     * @return 工厂名称用于日志
     */
    protected abstract String getFactoryName();

}
