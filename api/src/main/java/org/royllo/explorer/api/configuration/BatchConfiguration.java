package org.royllo.explorer.api.configuration;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Batch configuration.
 */
@Profile("!schedule-disabled")
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BatchConfiguration extends BaseConfiguration {

    /** Scheduler pool size. */
    private static final int SCHEDULER_POOL_SIZE = 1;

    /** Start delay in milliseconds (1 000 ms = 1 second). */
    private static final int START_DELAY_IN_MILLISECONDS = 1_000;

    /** Termination delay in milliseconds (10 000 ms = 10 seconds). */
    private static final int TERMINATION_DELAY_IN_MILLISECONDS = 10_000;

    /**
     * Configure the task scheduler.
     *
     * @return task scheduler
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationMillis(TERMINATION_DELAY_IN_MILLISECONDS);
        scheduler.setThreadNamePrefix("cassandre-flux-");
        scheduler.setPoolSize(SCHEDULER_POOL_SIZE);
        scheduler.setErrorHandler(throwable -> {
            try {
                logger.error("Error in scheduled tasks: {}", throwable.getMessage());
            } catch (Exception exception) {
                logger.error("Error in scheduled tasks: {}", exception.getMessage());
            }
        });
        return scheduler;
    }

}
