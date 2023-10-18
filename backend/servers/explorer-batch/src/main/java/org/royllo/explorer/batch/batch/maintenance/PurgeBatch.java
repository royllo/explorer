package org.royllo.explorer.batch.batch.maintenance;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;

/**
 * Purge batch.
 * - Purge failed requests - Leave only the last 10 000 failed requests.
 * - Purge recoverable requests - Remove all recoverable requests older than one month.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class PurgeBatch extends BaseBatch {

    /** Maximum number of failed requests to store. */
    public static final int MAXIMUM_FAILED_REQUESTS_STORE = 10_000;

    /** Start delay in milliseconds (1 000 ms = 1 second). */
    private static final int START_DELAY_IN_MILLISECONDS = 10_000;

    /** Delay between two calls to process requests (1 000 ms = 1 second). */
    private static final int DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS = 60_000;

    /** Request repository. */
    private final RequestRepository requestRepository;

    /**
     * Purge data.
     */
    @Scheduled(initialDelay = START_DELAY_IN_MILLISECONDS, fixedDelay = DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS)
    public void purge() {
        AtomicLong numberOfPurgeRequests = new AtomicLong(0);

        // =============================================================================================================
        // Purge failed requests.
        logger.info("Checking if failed request should be purged");
        final long allFailedRequestsCount = requestRepository.findByStatusOrderById(FAILURE).size();

        if (allFailedRequestsCount > MAXIMUM_FAILED_REQUESTS_STORE) {
            logger.info("{} failed requests need to be purged", allFailedRequestsCount - MAXIMUM_FAILED_REQUESTS_STORE);
            requestRepository.findByStatusOrderById(FAILURE, PageRequest.of(1, MAXIMUM_FAILED_REQUESTS_STORE))
                    .forEach(request -> {
                        logger.info("Purging request {}", request);
                        requestRepository.delete(request);
                        numberOfPurgeRequests.getAndIncrement();
                    });
            logger.info("{} failed requests purged", numberOfPurgeRequests.get());
        } else {
            logger.info("{} existing failed requests - No purge needed", allFailedRequestsCount);
        }
    }

}
