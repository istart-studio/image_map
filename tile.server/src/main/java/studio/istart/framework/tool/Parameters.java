package studio.istart.framework.tool;

/**
 * @author DongYan
 * @version 1.0.0
 * @since 1.8
 */
public class Parameters {
    private Parameters() {
    }

    public static boolean nullable(Object... params) {
        if (params == null) return true;
        for (Object param : params) {
            if (param == null) return true;
        }
        return false;
    }
}
