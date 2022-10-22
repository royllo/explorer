package org.royllo.explorer.database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application.
 */
@SpringBootApplication
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
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
