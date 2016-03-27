package secret_gift_exchange.utils;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import secret_gift_exchange.exceptions.SecretGiftExchangeException;
import secret_gift_exchange.models.Participant;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static secret_gift_exchange.utils.TextUtil.isValidEmailAddress;
import static secret_gift_exchange.utils.TextUtil.parseTextIntoPersons;

public class TextUtilTest {

    @Test
    public void testParseTextIntoPersonsWithValidData() throws Exception {
        final String data = getFromFile("data/valid_data.txt");
        final List<Participant> parseResult = parseTextIntoPersons(data);
        assertEquals(parseResult.size(), 3);
        assertEquals(parseResult.get(0).getFullName(), "Bob A");
    }

    @Test(expectedExceptions = {SecretGiftExchangeException.class},
        expectedExceptionsMessageRegExp = "Invalid data at line: Hi this is wrong")
    public void testParseTextIntoPersonsWithInvalidData() throws Exception {
        final String data = getFromFile("data/invalid_data.txt");
        final List<Participant> parseResult = parseTextIntoPersons(data);
    }

    @Test(expectedExceptions = {SecretGiftExchangeException.class},
        expectedExceptionsMessageRegExp = "Invalid email at line: Bob A <111.com>")
    public void testParseTextIntoPersonsWithInvalidEmail() throws Exception {
        final String data = getFromFile("data/invalid_email.txt");
        final List<Participant> parseResult = parseTextIntoPersons(data);
    }

    @Test
    public void testIsValidEmailAddress() throws Exception {
        assertTrue(isValidEmailAddress("bob@example.com"));
        assertTrue(isValidEmailAddress("<bob@example.com>"));
        assertFalse(isValidEmailAddress("111.com"));
        assertFalse(isValidEmailAddress("<>"));
    }

    public String getFromFile(final String filename) throws IOException {
        final InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(in, "UTF-8");
    }
}
