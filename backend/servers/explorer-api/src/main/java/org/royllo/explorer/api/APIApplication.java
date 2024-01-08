package org.royllo.explorer.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API application.
 */
@SpringBootApplication(scanBasePackages = "org.royllo.explorer")
@SuppressWarnings({"checkstyle:FinalClass", "checkstyle:HideUtilityClassConstructor"})
public class APIApplication {

    /**
     * Main.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(APIApplication.class, args);
    }

}
