import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Bajtas on 27.11.2016.
 */

public class Checker implements Runnable {
    private static final Logger LOG = Logger.getLogger(Checker.class);

    private final String filePath;
    private final Properties config;
    private boolean fileAlreadyFound = false;
    public Checker(String filePath, Properties config) {
        this.filePath = filePath;
        this.config = config;
    }

    @Override
    public void run() {
        LOG.info("Checking if file exist: " + filePath);
        if (!fileAlreadyFound && Files.exists(Paths.get(filePath))) {
            try {
                Session session = Session.getDefaultInstance(config, new BAuthenticator(config));
                LOG.info("Sending email to: " + config.getProperty("email"));

                String body = prepareEmailBody();

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(config.getProperty(Globals.USER_KEY)));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(config.getProperty(Globals.EMAIL_RECEIVER)));
                message.setSubject(config.getProperty(Globals.EMAIL_SUBJECT));
                message.setContent(body, "text/html");
                Transport.send(message);

                LOG.info("Done.");
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            FileChecker.removeFromList(filePath);

            if (FileChecker.isLastFileToCheck())
                FileChecker.stopAll();

             fileAlreadyFound = true;
        }
    }

    private String prepareEmailBody() {
        String body = config.getProperty(Globals.EMAIL_BODY);
        if (Boolean.parseBoolean(config.getProperty(Globals.FILENAME_INCLUDED_IN_EMAIL)))
            body += "<br/>-- > " + "<html><body><b>" + filePath.substring(filePath.lastIndexOf('\\') + 1, filePath.length()) + "</b></body></html>";

        return body;
    }
}
