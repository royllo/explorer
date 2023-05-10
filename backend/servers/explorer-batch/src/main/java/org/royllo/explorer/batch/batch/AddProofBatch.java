package org.royllo.explorer.batch.batch;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;
import org.royllo.explorer.core.provider.tarod.TarodProofService;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.util.exceptions.proof.ProofCreationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Batch treating {@link AddProofRequestDTO}.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class AddProofBatch extends BaseBatch {

    /** Start delay in milliseconds (1 000 ms = 1 second). */
    private static final int START_DELAY_IN_MILLISECONDS = 1_000;

    /** Delay between two calls to process requests (1 000 ms = 1 second). */
    private static final int DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS = 1_000;

    /** Tarod proof service. */
    private final TarodProofService tarodProofService;

    /** Request repository. */
    private final RequestRepository requestRepository;

    /** Request service. */
    private final RequestService requestService;

    /** Asset service. */
    private final AssetService assetService;

    /** Proof service. */
    private final ProofService proofService;

    /** Batch continues to run as long as enabled is set to true. */
    private final AtomicBoolean enabled = new AtomicBoolean(true);

    /**
     * Recurrent calls to process requests.
     */
    @Scheduled(initialDelay = START_DELAY_IN_MILLISECONDS, fixedDelay = DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS)
    public void processRequests() {
        if (enabled.get()) {
            requestService.getOpenedRequests()
                    .stream()
                    .filter(request -> request instanceof AddProofRequestDTO)
                    .map(requestDTO -> (AddProofRequestDTO) requestDTO)
                    .forEach(request -> {
                        logger.info("addProofBatch - Processing request {}: {}", request.getId(), request);

                        // We try to decode the proof.
                        try {
                            final DecodedProofResponse decodedProofResponse = tarodProofService.decode(request.getRawProof(), 0).block();

                            // We check if we have a decoded proof response.
                            if (decodedProofResponse == null) {
                                logger.info("addProofBatch - Decoded proof for request {} is null", request.getId());
                                request.failure("Decoded proof is null");
                            } else {
                                // We check if we had an error deciding the response.
                                if (decodedProofResponse.getErrorCode() != null) {
                                    logger.info("addProofBatch - Request {} proof cannot be decoded because of this error: {}",
                                            request.getId(),
                                            decodedProofResponse.getErrorMessage());
                                    request.failure(decodedProofResponse.getErrorMessage());
                                } else {
                                    // We have the decoded proof, we check if the asset exists.
                                    final String assetId = decodedProofResponse.getDecodedProof().getAsset().getAssetGenesis().getAssetId();
                                    Optional<AssetDTO> assetDTO = assetService.getAssetByAssetId(assetId);
                                    if (assetDTO.isEmpty()) {
                                        // If it doesn't exist, we create it
                                        logger.info("addProofBatch - Because of request {}, adding asset {}", request.getId(), assetId);
                                        assetDTO = Optional.of(assetService.addAsset(ASSET_MAPPER.mapToAssetDTO(decodedProofResponse.getDecodedProof())));
                                    } else {
                                        logger.info("addProofBatch - For request {}, asset {} already exists", request.getId(), assetId);
                                    }
                                    // We can now add the proof.
                                    proofService.addProof(request.getRawProof(), decodedProofResponse);
                                    request.setAsset(assetDTO.get());
                                    request.success();
                                }
                            }
                        } catch (ProofCreationException exception) {
                            logger.error("Request {} has error: {}", request.getId(), exception.getMessage());
                            request.failure(exception.getMessage());
                        } catch (Throwable tarodError) {
                            // We failed on calling tarod, but it's an exception; not a "valid" error.
                            logger.error("Request {} has error: {}", request.getId(), tarodError.getMessage());
                            request.recoverableFailure("Recoverable error: " + tarodError.getMessage());
                        }

                        // We save the request.
                        logger.info("addProofBatch - Proof {} added: {} ", request.getId(), request);
                        requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(request));
                    });
        }
    }

    /**
     * This method is called before the application shutdown.
     * We stop calling the flux.
     */
    @PreDestroy
    public void shutdown() {
        logger.info("addProofBatch - Closing gracefully Royllo addProofBatch...");
        enabled.set(false);
    }

}
