package org.royllo.explorer.web.configuration;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.util.base.BaseConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Scheduler configuration.
 */
@Profile("!scheduler-disabled")
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfiguration extends BaseConfiguration {

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
        scheduler.setThreadNamePrefix("royllo-process-");
        scheduler.setErrorHandler(throwable -> {
            try {
                logger.error("Error while processing requests: {}", throwable.getMessage());
            } catch (Exception exception) {
                logger.error("Error while processing requests: {}", exception.getMessage());
            }
        });
        return scheduler;
    }

}
