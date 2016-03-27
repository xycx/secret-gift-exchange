package secret_gift_exchange.models;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ParticipantTest {

    @Test
    public void testPerson() {
        final Participant participant1 = new Participant.Builder()
            .firstName("Bob").lastName("A").email("<boba@example.com>").build();
        assertEquals(participant1.getFirstName(), "Bob");
        assertEquals(participant1.getLastName(), "A");
        assertEquals(participant1.getEmail(), "<boba@example.com>");
        assertEquals(participant1.getFullName(), "Bob A");

        final Participant participant2 = new Participant.Builder()
            .data(new String[]{"Bob", "A", "<boba@example.com>"}).build();
        assertEquals(participant2.getFirstName(), "Bob");
        assertEquals(participant2.getLastName(), "A");
        assertEquals(participant2.getEmail(), "<boba@example.com>");
        assertEquals(participant2.getFullName(), "Bob A");
    }

}
