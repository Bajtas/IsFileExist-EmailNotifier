import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Bajtas on 27.11.2016.
 */

public class Checker implements Runnable {
    private static final Logger LOG = Logger.getLogger(Checker.class);

    private final String FILE_PATH;
    private final Properties CONFIG;
    private boolean fileAlreadyFound = false;

    public Checker(String filePath, Properties config) {
        this.FILE_PATH = filePath;
        this.CONFIG = config;
    }

    @Override
    public void run() {
        LOG.info("Checking if file exist: " + FILE_PATH);
        if (!fileAlreadyFound && Files.exists(Paths.get(FILE_PATH))) {
            try {
                Session session = Session.getDefaultInstance(CONFIG, new BAuthenticator(CONFIG));
                LOG.info("Sending email to: " + CONFIG.getProperty("email"));

                String body = prepareEmailBody();

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(CONFIG.getProperty(Globals.USER_KEY)));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(CONFIG.getProperty(Globals.EMAIL_RECEIVER)));
                message.setSubject(CONFIG.getProperty(Globals.EMAIL_SUBJECT));
                message.setContent(body, "text/html");
                Transport.send(message);

                LOG.info("Done.");
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            FileChecker.removeFromList(FILE_PATH);

            if (FileChecker.isLastFileToCheck())
                FileChecker.stopAll();

             fileAlreadyFound = true;
        }
    }

    private String prepareEmailBody() {
        String body = CONFIG.getProperty(Globals.EMAIL_BODY);
        if (Boolean.parseBoolean(CONFIG.getProperty(Globals.FILENAME_INCLUDED_IN_EMAIL)))
            body += "<br/>-- > " + "<html><body><b>" + FILE_PATH.substring(FILE_PATH.lastIndexOf('\\') + 1, FILE_PATH.length()) + "</b></body></html>";

        return body;
    }
}
