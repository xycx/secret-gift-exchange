package secret_gift_exchange.resources;

import org.testng.annotations.Test;

import java.io.IOException;

import javax.ws.rs.core.Response;

import static org.eclipse.jetty.http.HttpStatus.INTERNAL_SERVER_ERROR_500;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.testng.Assert.assertEquals;
import static secret_gift_exchange.utils.TextUtilTest.getFromFile;

public class ResourceTest {

    private final Resource resource = new Resource();

    @Test
    public void testBuildExchangePairs() throws IOException {
        final String data = getFromFile("data/valid_data.txt");
        final Response response = resource.buildExchangePairs(false, data);
        assertEquals(response.getStatus(), OK_200);
    }

    @Test
    public void testBuildExchangePairsWithException() throws IOException {
        final String data = getFromFile("data/invalid_data.txt");
        final Response response = resource.buildExchangePairs(false, data);
        assertEquals(response.getStatus(), INTERNAL_SERVER_ERROR_500);
    }

}
