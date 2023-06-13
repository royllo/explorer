package org.royllo.explorer.batch.batch.universe;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.service.universe.UniverseServerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Batch retrieving data from know universe servers.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class UniverseExplorerBatch extends BaseBatch {

    /** Start delay in milliseconds (1 000 ms = 1 second). */
    private static final int START_DELAY_IN_MILLISECONDS = 1_000;

    /** Delay between two calls to process requests (1 000 ms = 1 second). */
    private static final int DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS = 10_000;

    /** Request service. */
    private final RequestService requestService;

    /** Universe server service. */
    private final UniverseServerService universeServerService;

    /**
     * Retrieving all universe servers data.
     */
    @Scheduled(initialDelay = START_DELAY_IN_MILLISECONDS, fixedDelay = DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS)
    public void processUniverseServers() {
        if (enabled.get()) {
            universeServerService.getAllUniverseServers()
                    .forEach(universeServer -> {
                        logger.info("Processing universe server {}: {}", universeServer.getId(), universeServer);
                    });
        }
    }

}
