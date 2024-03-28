package org.royllo.explorer.core.test.util;

import jakarta.validation.ConstraintViolationException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.royllo.explorer.core.util.base.Base;

import java.io.IOException;

import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_HOST;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_PORT;

/**
 * Base test class.
 */
public class BaseTest extends Base {

    /**
     * Check if the property path is in the constraint violations.
     *
     * @param e            ConstraintViolationException
     * @param propertyPath Property path
     * @return true if found
     */
    protected boolean isPropertyInConstraintViolations(ConstraintViolationException e, String propertyPath) {
        return e.getConstraintViolations()
                .stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().contains(propertyPath));
    }

    /**
     * Get a file from the content server.
     *
     * @param filename filename
     * @return Response or null if an error occurred
     */
    protected Response getFileFromContentServer(String filename) {
        var client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + filename)
                .build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            logger.error("Error while retrieving the file" + e.getMessage());
            return null;
        }
    }

}
