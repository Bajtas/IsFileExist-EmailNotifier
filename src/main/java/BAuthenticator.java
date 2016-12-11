import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

/**
 * Created by Bajtas on 27.11.2016.
 */
public class BAuthenticator extends Authenticator {

    private static Properties mailProperties;

    public BAuthenticator(Properties mailProperties) {
        this.mailProperties = mailProperties;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mailProperties.getProperty(Globals.USER_KEY), mailProperties.getProperty(Globals.USER_PASSWORD_KEY));
    }
}
