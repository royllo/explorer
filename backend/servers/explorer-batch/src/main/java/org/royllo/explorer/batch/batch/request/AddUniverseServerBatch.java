package org.royllo.explorer.batch.batch.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.request.AddUniverseServerRequestDTO;
import org.royllo.explorer.core.dto.universe.UniverseServerDTO;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.provider.tapd.UniverseRootsResponse;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.service.universe.UniverseServerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Batch treating {@link AddUniverseServerRequestDTO}.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class AddUniverseServerBatch extends BaseBatch {

    /** Start delay in milliseconds (1 000 ms = 1 second). */
    private static final int START_DELAY_IN_MILLISECONDS = 1_000;

    /** Delay between two calls to process requests (1 000 ms = 1 second). */
    private static final int DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS = 1_000;

    /** TapdService service. */
    private final TapdService tapdService;

    /** Request repository. */
    private final RequestRepository requestRepository;

    /** Request service. */
    private final RequestService requestService;

    /** Universe server service. */
    private final UniverseServerService universeServerService;

    /**
     * Recurrent calls to process requests.
     */
    @Scheduled(initialDelay = START_DELAY_IN_MILLISECONDS, fixedDelay = DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS)
    public void processRequests() {
        logger.info("Processing add universe requests");
        if (enabled.get()) {
            requestService.getOpenedRequests()
                    .stream()
                    .filter(request -> request instanceof AddUniverseServerRequestDTO)
                    .map(requestDTO -> (AddUniverseServerRequestDTO) requestDTO)
                    .forEach(request -> {
                        logger.info("Processing request {}: {}", request.getId(), request);

                        try {
                            // If the universe server already exists, we set the request as failed.
                            if (universeServerService.getUniverseServerByServerAddress(request.getServerAddress()).isPresent()) {
                                throw new RuntimeException("Universe " + request.getServerAddress() + " server already exists");
                            } else {
                                // Calling tapd service to see if the server respond.
                                final UniverseRootsResponse response = tapdService.getUniverseRoots(request.getServerAddress(), 0, 1).block();

                                // if the response is null.
                                if (response == null) {
                                    throw new RuntimeException("Universe roots response null");
                                } else {
                                    // The response is not null, but we check if there is an error.
                                    if (response.getErrorCode() != null) {
                                        throw new RuntimeException("Universe roots response error code: " + response.getErrorMessage());
                                    } else {
                                        // No error -  We create the universe server.
                                        final UniverseServerDTO universeServerCreated = universeServerService.addUniverseServer(request.getServerAddress());
                                        request.setUniverseServer(universeServerCreated);
                                        request.success();
                                    }
                                }

                            }
                        } catch (Throwable tapdError) {
                            // An error occurred in the process of adding a server.
                            logger.error("Request {} has error: {}", request.getId(), tapdError.getMessage());
                            request.failure("Error: " + tapdError.getMessage());
                        }

                        // We save the request result.
                        logger.info("Universe server {} added: {} ", request.getServerAddress(), request);
                        requestRepository.save(REQUEST_MAPPER.mapToAddUniverseServerRequest(request));
                    });
        }
    }

}
