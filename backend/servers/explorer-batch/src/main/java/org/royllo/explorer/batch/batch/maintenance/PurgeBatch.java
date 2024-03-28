package org.royllo.explorer.batch.batch.maintenance;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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
        logger.info("Checking if failed request should be purged");
        if (requestRepository.countByStatusOrderById(FAILURE) > MAXIMUM_FAILED_REQUESTS_STORE) {
            // We purge.
            final List<Request> toDelete = requestRepository.findByStatusOrderById(FAILURE, PageRequest.of(1, MAXIMUM_FAILED_REQUESTS_STORE));
            requestRepository.deleteAll(toDelete);
            logger.info("{} failed requests purged", toDelete.size());
        } else {
            // We don't purge.
            logger.info("No purge needed");
        }
    }

}
