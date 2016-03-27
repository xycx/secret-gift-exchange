package secret_gift_exchange.utils;


import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import secret_gift_exchange.exceptions.SecretGiftExchangeException;
import secret_gift_exchange.models.Participant;

public class TextUtil {

    public static List<Participant> parseTextIntoPersons(final String text) {

        final List<Participant> participants = new ArrayList<>();

        final String[] lines = text.split("\\n");
        for (final String line : lines) {
            final String[] data = line.split(" ");
            if (data.length != 3) {
                throw new SecretGiftExchangeException("Invalid data at line: " + line);
            }
            if (!isValidEmailAddress(data[2])) {
                throw new SecretGiftExchangeException("Invalid email at line: " + line);
            }
            participants.add(new Participant.Builder().data(data).build());
        }

        return participants;
    }

    public static boolean isValidEmailAddress(final String email) {
        boolean result = true;
        try {
            final InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException e) {
            result = false;
        }
        return result;
    }
}
