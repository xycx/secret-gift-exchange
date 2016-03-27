package secret_gift_exchange;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class App {

    public static void main(String [] args) throws Exception {

        final ResourceConfig config = new ResourceConfig();
        config.packages("secret_gift_exchange");
        final ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        final Server server = new Server(3333);
        final ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}
