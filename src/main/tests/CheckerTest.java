import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by Bajtas on 22.04.2017.
 */
public class CheckerTest {

    @Test
    public void fileCheckerMustContainDirectoryPathAndFilesListTest() {
        String []args = new String[] {"directory", "", ""};
        FileChecker checker = new FileChecker(args);
        List<String> files = FileChecker.getFiles();
        String directoryPath = FileChecker.getDirectoryPath();

        assertNotNull(files);
        assertEquals(args[0], directoryPath);
        assertEquals(args.length - 1, files.size());
        assertTrue(files.stream().allMatch(StringUtils.EMPTY::equals));
    }

    @Test
    public void areArgsValidTest() {
        String []args = new String[] {"", "", ""};
        FileChecker checker = new FileChecker(args);
        List<String> files = FileChecker.getFiles();
        String directoryPath = FileChecker.getDirectoryPath();

        assertNotNull(files);
        assertEquals(args[0], directoryPath);
        assertEquals(args.length - 1, files.size());
        assertTrue(files.stream().allMatch(StringUtils.EMPTY::equals));
        assertFalse(checker.isArgsValid());
    }

    @Test
    public void fileCheckerPrepare() {
        String []args = new String[] {"", "", ""};
        FileChecker checker = new FileChecker(args);
        Properties config = FileChecker.getConfig();
        assertNotNull(config);

        checker.prepare();
        assertNotNull(config);
        assertNotNull(Globals.CONFIG_FILENAME);
        assertNotEquals(Globals.CONFIG_FILENAME, StringUtils.EMPTY);
    }

    @Test
    public void appConfigFileTest() {
        String []args = new String[] {"", "", ""};
        FileChecker checker = new FileChecker(args);
        Properties config = FileChecker.getConfig();
        assertNotNull(config);

        checker.prepare();
        assertNotNull(config);
        assertNotNull(Globals.CONFIG_FILENAME);
        assertNotEquals(StringUtils.EMPTY, Globals.CONFIG_FILENAME);

        assertTrue(config.containsKey("email.receiver"));
        assertTrue(config.containsKey("email.subject"));
        assertTrue(config.containsKey("email.body"));
        assertTrue(config.containsKey("filename.included.in.email"));
        assertTrue(config.containsKey("mail.smtp.host"));
        assertTrue(config.containsKey("mail.smtp.socketFactory.port"));
        assertTrue(config.containsKey("mail.smtp.socketFactory.class"));
        assertTrue(config.containsKey("mail.smtp.auth"));
        assertTrue(config.containsKey("mail.smtp.port"));
        assertTrue(config.containsKey("mail.smtp.password"));
        assertTrue(config.containsKey("mail.smtp.user"));

        Enumeration<?> enumeration = config.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String)enumeration.nextElement();
            assertNotNull(key);

            String property = config.getProperty(key);
            assertNotNull(property);
            assertTrue(StringUtils.isNotBlank(property));
        }
    }
}
