package cc.ccoder.compensation.event.common.factory;

import cc.ccoder.compensation.event.common.CodeService;

/**
 * <p>
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 18:33
 */
public interface CodeServiceFactory<Provider extends CodeService> {

    Provider getService(String serviceCode);
}
