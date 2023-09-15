package org.royllo.mock.mempool;

import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * Hello world!
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class Server {

    /** Server port. */
    public static final int SERVER_PORT = 9000;

    /**
     * Starting the server.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
        System.out.println("Hello World!");
        ClientAndServer.startClientAndServer(SERVER_PORT);
        new MockServerClient("localhost", SERVER_PORT)
                .when(
                        request()
                                .withPath("/hello")
                )
                .respond(
                        response()
                                .withBody(new Server().getFileContentFromJar("/test.json"))
                );

    }

    /**
     * Returns the content from a file in the jar.
     *
     * @param pathInJar path of the file in the jar
     * @return content
     */
    public final String getFileContentFromJar(final String pathInJar) {
        InputStream is = getClass().getResourceAsStream(pathInJar);
        if (is == null) {
            return null; // or handle error: file not found or something else
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            return null; // or handle error: IO exception, etc.
        }
    }

}
