package cc.ccoder.compensation.event.domain.convertor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author congcong
 * @email congccoder@gmail.com
 * @date 2022/3/30 19:12
 */
public class DomainConverterUtil {

    public static <BO, DO> List<DO> convertToBatchDO(List<BO> boList, DomainConverter<BO, DO> converter) {
        if (boList == null) {
            return null;
        }
        List<DO> results = new ArrayList<>();
        for (BO item : boList) {
            DO result = converter.convertToDO(item);
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }

    public static <BO, DO> List<BO> convertToBatchBO(List<DO> doList, DomainConverter<BO, DO> converter) {
        if (doList == null) {
            return null;
        }
        List<BO> results = new ArrayList<>();
        for (DO item : doList) {
            BO result = converter.convertToBO(item);
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }
}
