package org.royllo.explorer.batch.batch.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Batch treating {@link AddAssetMetaDataBatch}.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class AddAssetMetaDataBatch extends BaseBatch {

    /** Start delay in milliseconds (1 000 ms = 1 second). */
    private static final int START_DELAY_IN_MILLISECONDS = 1_000;

    /** Delay between two calls to process requests (1 000 ms = 1 second). */
    private static final int DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS = 1_000;

    /** Request service. */
    private final RequestService requestService;

    /**
     * Recurrent calls to process requests.
     */
    @Scheduled(initialDelay = START_DELAY_IN_MILLISECONDS, fixedDelay = DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS)
    public void processRequests() {
        if (enabled.get()) {
            requestService.getOpenedRequests()
                    .stream()
                    .filter(request -> request instanceof AddAssetMetaDataRequestDTO)
                    .map(requestDTO -> (AddAssetMetaDataRequestDTO) requestDTO)
                    .forEach(request -> {
                        logger.info("Processing request {}: {}", request.getId(), request);
                    });
        }
    }

}
