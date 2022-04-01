package cc.ccoder.compensation.event.domain.convertor;

/**
 * <p>
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 19:11
 */
public interface DomainConverter<BO, DO> {

    DO convertToDO(BO domain);

    BO convertToBO(DO domain);

}
