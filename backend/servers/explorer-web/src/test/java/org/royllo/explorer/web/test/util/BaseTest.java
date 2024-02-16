package org.royllo.explorer.web.test.util;

import org.junit.jupiter.params.provider.Arguments;
import org.royllo.explorer.core.util.base.Base;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;

import java.util.stream.Stream;

import static org.royllo.explorer.web.util.base.BaseController.HTMX_REQUEST;

/**
 * Utility classes for tests.
 */
public class BaseTest extends Base {

    /** Straumat user used for tests. */
    protected final String STRAUMAT_USER_ID = "22222222-2222-2222-2222-222222222222";
    protected final String STRAUMAT_USER_USERNAME = "straumat";
    protected final String STRAUMAT_USER_FULL_NAME = "Stéphane Traumat";
    protected final String STRAUMAT_USER_BIOGRAPHY = "I am a developer";
    protected final String STRAUMAT_USER_WEBSITE = "https://github.com/straumat";

    /** NewUser user used for tests. */
    protected final String NEW_USER_ID = "33333333-3333-3333-3333-333333333333";
    protected final String NEW_USER_USERNAME = "newuser";

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
                // Normal call.
                Arguments.of(new HttpHeaders()),
                // HTMX call.
                Arguments.of(htmxHeaders)
        );
    }

    /**
     * Method used by tests to test htmx and non htmx methods.
     *
     * @return headers to test
     */
    protected static HttpHeaders getHeaders() {
        final HttpHeaders htmxHeaders = new HttpHeaders();
        htmxHeaders.add(HTMX_REQUEST, "true");
        return htmxHeaders;
    }

    /**
     * Returns the message for the given key.
     *
     * @param messageSource message source
     * @param key           key
     * @return message
     */
    protected String getMessage(MessageSource messageSource, String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

}
