package org.royllo.explorer.batch.batch.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.request.ClaimOwnershipRequestDTO;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Batch treating {@link ClaimOwnershipRequestDTO}.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class ClaimOwnershipRequestBatch extends BaseBatch {

    /** Start delay in milliseconds (1 000 ms = 1 second). */
    private static final int START_DELAY_IN_MILLISECONDS = 1_000;

    /** Delay between two calls to process requests (1 000 ms = 1 second). */
    private static final int DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS = 1_000;

    /** TapdService service. */
    private final TapdService tapdService;

    /** Asset repository. */
    private final AssetRepository assetRepository;

    /** Request repository. */
    private final RequestRepository requestRepository;

    /** Request service. */
    private final RequestService requestService;

    /** Asset service. */
    private final AssetService assetService;

    /** Asset state service. */
    private final AssetStateService assetStateService;

    /**
     * Recurrent calls to process requests.
     */
    @Scheduled(initialDelay = START_DELAY_IN_MILLISECONDS, fixedDelay = DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS)
    public void processRequests() {
        logger.info("Processing claim ownership requests");
        if (enabled.get()) {
            requestService.getOpenedRequests()
                    .stream()
                    .filter(request -> request instanceof ClaimOwnershipRequestDTO)
                    .map(requestDTO -> (ClaimOwnershipRequestDTO) requestDTO)
                    .forEach(request -> {
                        logger.info("Processing request {}", request.getId());
                        // TODO Use a pattern to simplify this code.

                        // Calling the decode method on the provided proof to retrieve the values.
                        final var decodedProof = tapdService.decode(request.getProofWithWitness()).block();
                        if (decodedProof == null) {
                            logger.info("Decoded proof for request {} is null", request.getId());
                            request.failure("Decoded proof is null");
                        } else {

                            // We check if there is an error code returned by the server when calling decode().
                            if (decodedProof.getErrorCode() != null) {
                                logger.info("Request {} proof cannot be decoded because of this error: {}",
                                        request.getId(),
                                        decodedProof.getErrorMessage());
                                request.failure(decodedProof.getErrorMessage());
                            } else {

                                // The proof is valid, we check if the proof is about an asset we have in database.
                                final String assetId = decodedProof.getDecodedProof().getAsset().getAssetGenesis().getAssetId();
                                Optional<AssetDTO> existingAsset = assetService.getAssetByAssetId(assetId);
                                if (existingAsset.isEmpty()) {
                                    logger.info("Request {} proof is not about an asset we have in database : {}", request.getId(), assetId);
                                    request.failure("This asset is not in our database, use 'Add proof' menu first");
                                } else {

                                    // The asset exists, we check if this is about an issuance or a transfer.
                                    if (!decodedProof.getDecodedProof().getAsset().isIssuance()) {
                                        logger.info("The proof does not concern an issuance state {}",
                                                request.getId());
                                        request.failure("This proof does not concern the asset issuance");
                                    } else {

                                        // =============================================================================
                                        // We now call the ownership method to check if the proof is valid.
                                        final var ownership = tapdService.verifyOwnership(request.getProofWithWitness()).block();
                                        if (ownership == null) {
                                            logger.info("Ownership proof for request {} is null", request.getId());
                                            request.failure("Ownership proof is null");
                                        } else {

                                            // We check if there is an error code returned by the server when calling verifyOwnership().
                                            if (ownership.getErrorCode() != null || ownership.getValidProof() == null) {
                                                logger.info("Request {} ownership proof cannot be verified because of this error: {}",
                                                        request.getId(),
                                                        ownership.getErrorMessage());
                                                request.failure(ownership.getErrorMessage());
                                            } else {

                                                // Validating the ownership proof.
                                                if (!ownership.getValidProof()) {
                                                    logger.info("Request {} ownership proof is not valid", request.getId());
                                                    request.failure("Ownership proof is not valid");
                                                } else {
                                                    // The proof is valid, we update the asset user and destroy the proof in the request.
                                                    assetRepository.findByAssetId(existingAsset.get().getAssetId())
                                                            .ifPresent(asset -> {
                                                                asset.setCreator(USER_MAPPER.mapToUser(request.getCreator()));
                                                                assetRepository.save(asset);
                                                            });
                                                    request.success();
                                                }

                                                // The proof is valid, we update the asset user and destroy the proof in the request.
                                                // assetStateService.updateAssetUser(assetId, ownership.getDecodedOwnership().getNewUser());

                                                //request.success();

                                            }
                                        }

                                    }

                                }

                            }

                        }

                        // Saving the request status.
                        logger.info("Request status {} updated to {}  ", request.getId(), request.getStatus());
                        // TODO EMPTY WITNESS PROOF
                        requestRepository.save(REQUEST_MAPPER.mapToClaimOwnershipRequest(request));
                    });
        }
    }

}
