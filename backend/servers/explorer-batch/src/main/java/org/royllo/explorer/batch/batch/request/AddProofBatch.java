package org.royllo.explorer.batch.batch.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.repository.request.RequestRepository;
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

                        try {
                            // =========================================================================================
                            // First step, we call decode to see how many proofs are inside the file.
                            long numberOfProofs = 0;
                            DecodedProofResponse response = tapdService.decode(request.getProof()).block();
                            if (response == null) {
                                logger.info("Decoded proof for request {} is null", request.getId());
                                request.failure("Decoded proof is null");
                            } else {
                                if (response.getErrorCode() != null) {
                                    logger.info("Request {} proof cannot be decoded because of this error: {}",
                                            request.getId(),
                                            response.getErrorMessage());
                                    request.failure(response.getErrorMessage());
                                } else {
                                    numberOfProofs = response.getDecodedProof().getNumberOfProofs();
                                    logger.info("Request {} has {} proofs", request.getId(), numberOfProofs);
                                }
                            }

                            // =========================================================================================
                            // Now, we decode all proofs, one by one, starting by the oldest (issuance proof).
                            boolean proofAdded = false;
                            for (long i = numberOfProofs; i > 0; i--) {

                                // We check if it's an issuance proof, if so, we will ask for meta reveal.
                                response = tapdService.decode(request.getProof(), i - 1, true).block();
                                if (response == null || response.getErrorCode() != null) {
                                    response = tapdService.decode(request.getProof(), i - 1, false).block();
                                }

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
                                        // Note that addAssetState() should create the asset and the asset group if they don't exist.
                                        Optional<AssetStateDTO> assetStateCreated = assetStateService.getAssetStateByAssetStateId(assetStateToCreate.getAssetStateId());
                                        if (assetStateCreated.isEmpty()) {
                                            // If it doesn't exist, we create it
                                            logger.info("Request {}, adding asset state {}", request.getId(), assetStateToCreate.getAssetStateId());
                                            assetStateCreated = Optional.of(assetStateService.addAssetState(assetStateToCreate));
                                        } else {
                                            logger.info("For request {}, asset state {} already exists", request.getId(), assetStateToCreate.getAssetStateId());
                                        }

                                        // We update the data and the file if we have meta-data.
                                        if (response.getDecodedProof().getMetaReveal() != null) {
                                            logger.info("Creating meta data file");
                                            assetService.updateAsset(response.getDecodedProof().getAsset().getAssetGenesis().getAssetId(),
                                                    response.getDecodedProof().getMetaReveal().getData());
                                        } else {
                                            logger.info("No meta data file to create");
                                        }

                                        // If not already added, we add the proof.
                                        if (!proofAdded) {
                                            proofService.addProof(request.getProof(), request.getProofType(), response);
                                            request.setAsset(assetStateCreated.get().getAsset());
                                            proofAdded = true;
                                        }
                                        request.success();
                                    }
                                }
                            }

                        } catch (ProofCreationException exception) {
                            logger.error("Request {} encountered an error:  {}", request.getId(), exception.getMessage());
                            request.failure(exception.getMessage());
                        } catch (Throwable tapdError) {
                            // We failed on calling tapd, but it's an exception; not a "valid" error.
                            logger.error("Request {} encountered an error with Tapd: {}", request.getId(), tapdError.getMessage());
                            request.failure("Error: " + tapdError.getMessage());
                        }

                        // We save the request.
                        logger.info("Proof {} added: {} ", request.getProof(), request);
                        requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(request));
                    });
        }
    }

}
