import org.apache.commons.lang3.StringUtils;

/**
 * Created by Bajtas on 27.11.2016.
 */
public class Util {

    public static boolean isStringValid(String toCheck) {
        return StringUtils.isNotBlank(toCheck) && StringUtils.isNotEmpty(toCheck);
    }
}
