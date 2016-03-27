package secret_gift_exchange.utils;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import secret_gift_exchange.exceptions.ImpossibleMatchingException;
import secret_gift_exchange.exceptions.SecretGiftExchangeException;
import secret_gift_exchange.models.ExchangePair;
import secret_gift_exchange.models.Participant;

import static org.testng.Assert.assertNotEquals;
import static secret_gift_exchange.utils.RandomPickUtil.randomPick;
import static secret_gift_exchange.utils.RandomPickUtil.randomPickWithRetry;

public class RandomPickUtilTest {

    @Test
    public void testRandomPickAllowSameFamily() throws ImpossibleMatchingException {
        final List<Participant> participants = buildTestPersons("1", "a", "1", "b");
        final List<ExchangePair> exchangePairs = randomPick(participants, true);
        for (final ExchangePair pair : exchangePairs) {
            assertNotEquals(pair.getGifter(), pair.getReceiver());
        }
    }

    @Test
    public void testRandomPickNotAllowSameFamily() throws ImpossibleMatchingException {
        final List<Participant> participants = buildTestPersons(
            "1", "a", "2", "a", "1", "c", "2", "c");
        final List<ExchangePair> exchangePairs = randomPick(participants, false);
        for (final ExchangePair pair : exchangePairs) {
            assertNotEquals(pair.getGifter().getLastName(), pair.getReceiver().getLastName());
        }
    }

    @Test(expectedExceptions = {SecretGiftExchangeException.class},
        expectedExceptionsMessageRegExp = "Not enough participants")
    public void testRandomPickWithNotEnoughPerson() throws ImpossibleMatchingException {
        final List<Participant> participants = buildTestPersons("1", "a");
        final List<ExchangePair> exchangePairs = randomPickWithRetry(participants, true);
    }

    @Test(expectedExceptions = {ImpossibleMatchingException.class})
    public void testRandomPickWithNotEnoughFamily() throws ImpossibleMatchingException {
        final List<Participant> participants = buildTestPersons("1", "a", "2", "a", "1", "b");
        final List<ExchangePair> exchangePairs = randomPick(participants, false);
    }

    private List<Participant> buildTestPersons(String... data) {
        final List<Participant> participants = new ArrayList<>();
        for (int i = 0; i < data.length; i += 2) {
            participants.add(new Participant.Builder()
                            .firstName(data[i])
                            .lastName(data[i + 1])
                            .email(data[i] + data[i + 1] + "@example.com")
                            .build());

        }
        return participants;
    }

}