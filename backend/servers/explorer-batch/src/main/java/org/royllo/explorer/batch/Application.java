package org.royllo.explorer.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Batch application.
 */
@SpringBootApplication(scanBasePackages = "org.royllo.explorer")
@SuppressWarnings({"checkstyle:FinalClass", "checkstyle:HideUtilityClassConstructor"})
public class Application {

    /**
     * Main.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
