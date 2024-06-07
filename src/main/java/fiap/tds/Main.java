package fiap.tds;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";

    // Logger instance
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in fiap.tds.resources package
        final ResourceConfig rc = new ResourceConfig()
                .packages("fiap.tds.resources")
                .register(fiap.tds.infraestructure.CorsFilter.class);

        // Log resource registration
        LOGGER.info("Registering resources in package: fiap.tds.resources");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

        // Log server start
        LOGGER.info("Grizzly HTTP server started at: {}", BASE_URI);

        return server;
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.setProperty("log4j.configurationFile", "src/main/resources/log4j2.xml");
        final HttpServer server = startServer();
        LOGGER.info("Jersey app started with endpoints available at {}", BASE_URI);
        LOGGER.info("Hit Ctrl-C to stop it...");
        System.in.read();
        server.shutdownNow();
        LOGGER.info("Jersey app stopped.");
    }

}

