package org.royllo.explorer.batch.configuration;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.service.RequestProcessorService;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.util.base.BaseConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Scheduler.
 */
@Profile("!scheduler-disabled")
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler extends BaseConfiguration {

    /** Scheduler pool size. */
    private static final int SCHEDULER_POOL_SIZE = 1;

    /** Start delay in milliseconds (1 000 ms = 1 second). */
    private static final int START_DELAY_IN_MILLISECONDS = 1_000;

    /** Delay between two calls to process requests (1 000 ms = 1 second). */
    private static final int DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS = 1_000;

    /** Termination delay in milliseconds (10 000 ms = 10 seconds). */
    private static final int TERMINATION_DELAY_IN_MILLISECONDS = 10_000;

    /** Batch continues to run as long as enabled is set to true. */
    private final AtomicBoolean enabled = new AtomicBoolean(true);

    /** Request service. */
    private RequestService requestService;

    /** Request processor service. */
    private RequestProcessorService requestProcessorService;

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
        scheduler.setThreadNamePrefix("royllo-request-processor-");
        scheduler.setPoolSize(SCHEDULER_POOL_SIZE);
        scheduler.setErrorHandler(throwable -> {
            try {
                logger.error("Error while processing requests: {}", throwable.getMessage());
            } catch (Exception exception) {
                logger.error("Error while processing requests: {}", exception.getMessage());
            }
        });
        return scheduler;
    }

    /**
     * Recurrent calls to process requests.
     */
    @Scheduled(initialDelay = START_DELAY_IN_MILLISECONDS, fixedDelay = DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS)
    public void processRequests() {
        if (enabled.get()) {
            logger.info("Processing opened requests...");
            requestService.getOpenedRequests()
                    .forEach(requestDTO -> requestProcessorService.processRequest(requestDTO));
        }
    }

    /**
     * This method is called before the application shutdown.
     * We stop calling the flux.
     */
    @PreDestroy
    public void shutdown() {
        logger.info("Closing gracefully Royllo explorer batch...");
        enabled.set(false);
    }

}
