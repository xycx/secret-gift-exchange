package secret_gift_exchange.models;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ExchangePairTest {

    @Test
    public void testExchangePair() {
        final Participant p1 = new Participant.Builder().data(new String[]{"me", "", "me@example"}).build();
        final Participant p2 = new Participant.Builder().data(new String[]{"you", "", "you@example"}).build();
        final ExchangePair exchangePair = new ExchangePair.Builder()
            .gifter(p1)
            .receiver(p2)
            .build();
        assertEquals(exchangePair.getGifter().getFirstName(), "me");
        assertEquals(exchangePair.getReceiver().getFirstName(), "you");
    }

}
