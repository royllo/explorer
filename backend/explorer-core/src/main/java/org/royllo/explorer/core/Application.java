package org.royllo.explorer.core;

import org.royllo.explorer.core.util.parameters.MempoolParameters;
import org.royllo.explorer.core.util.parameters.TarodParameters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Royllo csore.
 */
@SpringBootApplication
@EnableConfigurationProperties({
        // Mempool configuration.
        MempoolParameters.class,
        MempoolParameters.Api.class,
        // Tarod configuration.
        TarodParameters.class,
        TarodParameters.Api.class
})
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
