package studio.istart.framework.tool;

import com.google.common.base.Strings;

import java.text.ParseException;

/**
 * Created by Se7en on 2016/5/3.
 * Desc:
 */
public class FormatHelper {

    public static String getStringValue(String originalEntity) throws ParseException {
        return getValue(originalEntity,original -> original);
    }

    public static String getStringValue(String originalEntity,IDefaultValue<String,String> convertFunc) throws ParseException {

        return Strings.isNullOrEmpty(originalEntity) ? "" : convertFunc.getDefault(originalEntity);
    }

    public static <T,R> R getValue(T originalEntity,IDefaultValue<T,R> convertFunc ) throws ParseException {

        return originalEntity == null ? null : convertFunc.getDefault(originalEntity);
    }

    public interface IDefaultValue<T,R>{

        R getDefault(T entity) throws ParseException;
    }
}
