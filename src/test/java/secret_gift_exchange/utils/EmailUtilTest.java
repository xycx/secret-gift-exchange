package secret_gift_exchange.utils;

import org.testng.annotations.Test;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import secret_gift_exchange.models.ExchangePair;
import secret_gift_exchange.models.Participant;

import static org.testng.Assert.assertEquals;
import static secret_gift_exchange.Config.EMAIL_FROM;

public class EmailUtilTest {

    @Test
    public void testBuildGifterNotificationEmail() throws MessagingException, IOException {
        final Participant p1 = new Participant.Builder()
            .data(new String[]{"Bob", "B", "<bob@example.com>"}).build();
        final Participant p2 = new Participant.Builder()
            .data(new String[]{"Alex", "A", "<alex@example.com>"}).build();
        final ExchangePair pair = new ExchangePair.Builder()
            .gifter(p1).receiver(p2).build();
        final MimeMessage message = EmailUtil.getInstance().buildGifterNotificationEmail(pair);
        assertEquals(message.getFrom()[0].toString(), EMAIL_FROM);
        assertEquals(message.getAllRecipients()[0].toString(), "bob@example.com");
        assertEquals(message.getContent().toString(), "Hi Bob, you have selected to send a gift to Alex A.");
    }

}
