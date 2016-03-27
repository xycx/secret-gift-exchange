package secret_gift_exchange.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import secret_gift_exchange.exceptions.SecretGiftExchangeException;
import secret_gift_exchange.models.ExchangePair;
import secret_gift_exchange.models.Participant;
import secret_gift_exchange.utils.EmailUtil;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.eclipse.jetty.http.HttpStatus.INTERNAL_SERVER_ERROR_500;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static secret_gift_exchange.Config.BUILD_EXCHANGE_PAIRS;
import static secret_gift_exchange.Config.ROOT_PATH;
import static secret_gift_exchange.utils.RandomPickUtil.randomPickWithRetry;
import static secret_gift_exchange.utils.TextUtil.parseTextIntoPersons;

@Path(ROOT_PATH)
public class Resource {

    @POST
    @Path(BUILD_EXCHANGE_PAIRS)
    @Consumes(TEXT_PLAIN)
    @Produces(APPLICATION_JSON)
    public Response buildExchangePairs(@DefaultValue("true") @QueryParam("allowSameFamily") boolean allowSameFamily,
                                       final String text) {
        try {
            final List<Participant> participants = parseTextIntoPersons(text);
            final List<ExchangePair> exchangePairs = randomPickWithRetry(participants, allowSameFamily);
            EmailUtil.getInstance().sendGifterNotificationEmail(exchangePairs);
            return Response.status(OK_200)
                .entity(exchangePairs)
                .build();
        } catch (SecretGiftExchangeException e) {
            return Response.status(INTERNAL_SERVER_ERROR_500)
                .entity(e.getMessage())
                .build();
        }
    }
}
