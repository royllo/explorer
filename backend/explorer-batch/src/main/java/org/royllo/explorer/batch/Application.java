package org.royllo.explorer.batch;

import org.royllo.explorer.core.util.parameters.MempoolParameters;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Batch application.
 */
@SpringBootApplication(scanBasePackages = {"org.royllo.explorer"})
@EnableConfigurationProperties({MempoolParameters.class, MempoolParameters.Api.class})
@EnableJpaRepositories("org.royllo.explorer.core.repository")
@EntityScan("org.royllo.explorer.core.domain")
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
