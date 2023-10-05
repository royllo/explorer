package org.royllo.explorer.web.test.util;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.http.HttpHeaders;

import java.util.stream.Stream;

import static org.royllo.explorer.web.util.base.BaseController.HTMX_REQUEST;

/**
 * Utility classes for tests.
 */
public class BaseTest {

    /**
     * Method used by tests to test htmx and non htmx methods.
     *
     * @return headers to test
     */
    protected static Stream<Arguments> headers() {
        // HTMX request header.
        final HttpHeaders htmxHeaders = new HttpHeaders();
        htmxHeaders.add(HTMX_REQUEST, "true");

        return Stream.of(
                // Normal call (no HTMX).
                Arguments.of(new HttpHeaders()),
                // HTMX call.
                Arguments.of(htmxHeaders)
        );
    }

}
