import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bajtas on 27.11.2016.
 */

public class FileChecker {
    private static final Logger LOG = Logger.getLogger(FileChecker.class);

    private static Properties config = new Properties();
    private static String directoryPath;
    private static List<String> files = new ArrayList<>();
    private static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> checkerHandler;

    public FileChecker(String[] args) {
        LOG.info("Checking if directory exist.");

        directoryPath = args[0];
        files.addAll(Arrays.asList(args).subList(1, args.length));
    }

    public void prepare() {
        try {
            config = new Properties();
            config.load(new FileInputStream("./" + Globals.CONFIG_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
            config = null;
        }
    }

    public boolean isArgsValid() {
        if (!Util.isStringValid(directoryPath) && !Files.isDirectory(Paths.get(directoryPath)) && config != null) {
            LOG.error("Specified directory not exist!");
            return false;
        }

        return true;
    }

    public void start() {
        for (String file : files) {
            String filePath = directoryPath + '\\' + file;
            checkerHandler = exec.scheduleAtFixedRate(new Checker(filePath, config), 0, 5, TimeUnit.SECONDS);
        }
    }

    public static boolean isLastFileToCheck() {
        if (files.isEmpty())
            return true;
        return false;
    }

    public static void stopAll() {
        exec.shutdown();
    }

    public static void removeFromList(String filePath) {
        Path path = Paths.get(filePath);
        files.remove(path.getFileName().toString());
    }
}
