package org.royllo.explorer.api;

import org.royllo.explorer.api.util.parameters.MempoolParameters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Application.
 */
@SpringBootApplication
@EnableConfigurationProperties({MempoolParameters.class, MempoolParameters.Api.class})
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
