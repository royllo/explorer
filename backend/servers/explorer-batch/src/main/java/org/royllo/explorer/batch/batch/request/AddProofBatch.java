package org.royllo.explorer.batch.batch.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
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

    /** Asset group service. */
    private final AssetGroupService assetGroupService;

    /** Asset service. */
    private final AssetService assetService;

    /** Asset state service. */
    private final AssetStateService assetStateService;

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
                            final DecodedProofResponse response = tapdService.decode(request.getRawProof()).block();

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
                                    // Now we have the decoded proof.
                                    AssetStateDTO assetStateToCreate = ASSET_STATE_MAPPER.mapToAssetStateDTO(response.getDecodedProof());

                                    // We check if the asset state already exists - If not, we create it.
                                    // Not that addAssetState() should create the asset and the asset group if they don't exists.
                                    Optional<AssetStateDTO> assetStateCreated = assetStateService.getAssetStateByAssetStateId(assetStateToCreate.getAssetStateId());
                                    if (assetStateCreated.isEmpty()) {
                                        // If it doesn't exist, we create it
                                        logger.info("Request {}, adding asset state {}", request.getId(), assetStateToCreate.getAssetStateId());
                                        assetStateCreated = Optional.of(assetStateService.addAssetState(assetStateToCreate));
                                    } else {
                                        logger.info("For request {}, asset state {} already exists", request.getId(), assetStateToCreate.getAssetStateId());
                                    }

                                    // We can now add the proof.
                                    proofService.addProof(request.getRawProof(), response);
                                    request.setAsset(assetStateCreated.get().getAsset());
                                    request.success();
                                }
                            }
                        } catch (ProofCreationException exception) {
                            logger.error("Request {} encountered an error:  {}", request.getId(), exception.getMessage());
                            request.failure(exception.getMessage());
                        } catch (Throwable tapdError) {
                            // We failed on calling tapd, but it's an exception; not a "valid" error.
                            tapdError.printStackTrace();
                            logger.error("Request {} encountered an error with Tapd: {}", request.getId(), tapdError.getMessage());
                            request.failure("Error: " + tapdError.getMessage());
                        }

                        // We save the request.
                        logger.info("Proof {} added: {} ", request.getRawProof(), request);
                        requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(request));
                    });
        }
    }

}
