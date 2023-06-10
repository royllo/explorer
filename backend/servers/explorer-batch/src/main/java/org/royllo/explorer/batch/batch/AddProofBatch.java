package org.royllo.explorer.batch.batch;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.util.exceptions.proof.ProofCreationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    /** Taproot proof service. */
    private final TapdService tapdService;

    /** Request repository. */
    private final RequestRepository requestRepository;

    /** Request service. */
    private final RequestService requestService;

    /** Asset service. */
    private final AssetService assetService;

    /** Proof service. */
    private final ProofService proofService;

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
                        logger.info("Processing request {}: {}", request.getId(), request);

                        // We try to decode the proof.
                        try {
                            final DecodedProofResponse response = tapdService.decode(request.getRawProof(), 0).block();

                            // We check if we have a decoded proof response.
                            if (response == null) {
                                logger.info("Decoded proof for request {} is null", request.getId());
                                request.failure("Decoded proof is null");
                            } else {
                                // We check if we had an error deciding the response.
                                if (response.getErrorCode() != null) {
                                    logger.info("Request {} proof cannot be decoded because of this error: {}",
                                            request.getId(),
                                            response.getErrorMessage());
                                    request.failure(response.getErrorMessage());
                                } else {
                                    // We have the decoded proof, we check if the asset exists.
                                    final String assetId = response.getDecodedProof().getAsset().getAssetGenesis().getAssetId();
                                    Optional<AssetDTO> assetDTO = assetService.getAssetByAssetId(assetId);
                                    if (assetDTO.isEmpty()) {
                                        // If it doesn't exist, we create it
                                        logger.info("Because of request {}, adding asset {}", request.getId(), assetId);
                                        assetDTO = Optional.of(assetService.addAsset(ASSET_MAPPER.mapToAssetDTO(response.getDecodedProof())));
                                    } else {
                                        logger.info("For request {}, asset {} already exists", request.getId(), assetId);
                                    }
                                    // We can now add the proof.
                                    proofService.addProof(request.getRawProof(), response);
                                    request.setAsset(assetDTO.get());
                                    request.success();
                                }
                            }
                        } catch (ProofCreationException exception) {
                            logger.error("Request {} has error: {}", request.getId(), exception.getMessage());
                            request.failure(exception.getMessage());
                        } catch (Throwable tapdError) {
                            // We failed on calling tapd, but it's an exception; not a "valid" error.
                            logger.error("Request {} has error: {}", request.getId(), tapdError.getMessage());
                            request.recoverableFailure("Recoverable error: " + tapdError.getMessage());
                        }

                        // We save the request.
                        logger.info("Proof {} added: {} ", request.getRawProof(), request);
                        requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(request));
                    });
        }
    }

}
