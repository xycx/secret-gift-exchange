package secret_gift_exchange.utils;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import secret_gift_exchange.models.ExchangePair;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;
import static javax.mail.Message.RecipientType.TO;
import static secret_gift_exchange.Config.EMAIL_FROM;
import static secret_gift_exchange.Config.EMAIL_HOST;
import static secret_gift_exchange.Config.EMAIL_SUBJECT;
import static secret_gift_exchange.Config.EMAIL_TEMPLATE;

public enum EmailUtil {

    INSTANCE;

    private final Logger logger;
    private final Session session;

    EmailUtil() {
        logger = Logger.getLogger(EmailUtil.class.getName());
        final Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", EMAIL_HOST);
        session = Session.getDefaultInstance(properties);
    }

    public static EmailUtil getInstance() {
        return INSTANCE;
    }

    public void sendGifterNotificationEmail(final List<ExchangePair> pairs) {
        for (ExchangePair pair : pairs) {
            EmailUtil.getInstance().sendGifterNotificationEmail(pair);
        }
    }

    public MimeMessage buildGifterNotificationEmail(final ExchangePair pair)
        throws MessagingException {

        final MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.addRecipient(TO, new InternetAddress(pair.getGifter().getEmail()));

        message.setSubject(EMAIL_SUBJECT);
        final String content =
            String.format(EMAIL_TEMPLATE, pair.getGifter().getFirstName(),
                          pair.getReceiver().getFullName());
        message.setText(content);

        return message;
    }

    public void sendGifterNotificationEmail(final ExchangePair pair) {

        try{
            final MimeMessage message = buildGifterNotificationEmail(pair);
            logger.log(INFO, "Senting email from {0} to {1}, content: {2}",
                       new Object[]{EMAIL_FROM,
                                    pair.getGifter().getEmail(),
                                    message.getContent()});
            Transport.send(message);
            logger.log(INFO, "Sent email to {0} successfully", pair.getGifter().getEmail());
        } catch (MessagingException | IOException e) {
            logger.log(WARNING, "Failed to send email to {0}", pair.getGifter().getEmail());
        }
    }

}
