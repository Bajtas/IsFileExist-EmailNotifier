import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * Created by Bajtas on 27.11.2016.
 */
public class Init {
    private static final Logger LOG = Logger.getLogger(Init.class);
    public static void main(String[] args) {
        LOG.info("Program stared with args: " + Arrays.toString(args));

        if (args != null && args.length != 0) {
            FileChecker checker = new FileChecker(args);
            checker.prepare();
            if (checker.isArgsValid()) {
                checker.start();
            }
        } else {
            LOG.info("Please specify directory as first arg, and file as second arg.");
        }
    }
}
