package secret_gift_exchange.utils;

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

    public void sendRecipientEmailToGifter(final List<ExchangePair> pairs) {
        for (ExchangePair pair : pairs) {
            EmailUtil.getInstance().sendRecipientEmailToGifter(pair);
        }
    }

    public void sendRecipientEmailToGifter(final ExchangePair pair) {

        try{
            final MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.addRecipient(TO, new InternetAddress(pair.getGifter().getEmail()));

            message.setSubject(EMAIL_SUBJECT);
            final String content =
                String.format(EMAIL_TEMPLATE, pair.getGifter().getFirstName(),
                              pair.getReceiver().getFullName());
            message.setText(content);

            logger.log(INFO, "Senting email from {0} to {1}, content: {2}",
                       new String[]{EMAIL_FROM,
                                    pair.getGifter().getEmail(),
                                    content});
            Transport.send(message);
            logger.log(INFO, "Sent email to {0} successfully", pair.getGifter().getEmail());
        } catch (MessagingException e) {
            logger.log(WARNING, "Failed to send email to {0}", pair.getGifter().getEmail());
        }
    }

}
