package org.royllo.explorer.batch.batch.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.request.ClaimAssetOwnershipRequestDTO;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Batch treating {@link ClaimAssetOwnershipRequestDTO}.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class ClaimAssetOwnershipRequestBatch extends BaseBatch {

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
                    .filter(request -> request instanceof ClaimAssetOwnershipRequestDTO)
                    .map(requestDTO -> (ClaimAssetOwnershipRequestDTO) requestDTO)
                    .forEach(request -> {
                        logger.info("Processing request {}", request.getId());

                        try {
                            // We decode.
                            final var response = tapdService.decode(request.getProofWithWitness()).block();

                            // -----------------------------------------------------------------------------------------
                            // We check if the response can be exploited.
                            if (response == null) {
                                // Response is null.
                                throw new RuntimeException("Decoded proof is null");
                            } else {
                                // Error code is present in response.
                                if (response.getErrorCode() != null) {
                                    throw new RuntimeException("Proof cannot be decoded because of this error: " + response.getErrorMessage());
                                }
                            }

                            // -----------------------------------------------------------------------------------------
                            // The proof is valid, we check if the proof is about an asset we have in database.
                            final String assetId = response.getDecodedProof().getAsset().getAssetGenesis().getAssetId();
                            assetService.getAssetByAssetIdOrAlias(assetId)
                                    .orElseThrow(() -> new RuntimeException("This asset is not in our database"));

                            // -----------------------------------------------------------------------------------------
                            // The asset exists, we check if this is about an issuance or a transfer.
                            if (!response.getDecodedProof().getAsset().isIssuance()) {
                                throw new RuntimeException("This proof does not concern the asset issuance");
                            }

                            // -----------------------------------------------------------------------------------------
                            // We now call the ownership method to check if the proof is valid.
                            final var ownershipResponse = tapdService.verifyOwnership(request.getProofWithWitness()).block();

                            // -----------------------------------------------------------------------------------------
                            // We check if the response can be exploited.
                            if (ownershipResponse == null) {
                                throw new RuntimeException("Ownership proof is null");
                            } else {
                                // We check if there is an error code returned by the server when calling verifyOwnership().
                                if (ownershipResponse.getErrorCode() != null || ownershipResponse.getValidProof() == null) {
                                    throw new RuntimeException("Ownership proof cannot be verified because of this error: " + ownershipResponse.getErrorMessage());
                                }
                            }

                            // -----------------------------------------------------------------------------------------
                            // Validating the ownership proof.
                            if (!ownershipResponse.getValidProof()) {
                                throw new RuntimeException("Ownership proof is not valid");
                            } else {
                                // The proof is valid, we update the asset user and destroy the proof in the request.
                                assetRepository.findByAssetId(assetId)
                                        .ifPresent(asset -> {
                                            asset.setCreator(USER_MAPPER.mapToUser(request.getCreator()));
                                            assetRepository.save(asset);
                                        });
                                request.success();
                            }

                        } catch (RuntimeException e) {
                            final String errorMessage = """
                                    Ownership request %s has an error : %s"""
                                    .formatted(request.getId(), e.getMessage());
                            logger.info(errorMessage);
                            request.failure(errorMessage);
                        }

                        // Saving the request status.
                        logger.info("Request status {} updated to {}  ", request.getId(), request.getStatus());
                        requestRepository.save(REQUEST_MAPPER.mapToClaimAssetOwnershipRequest(request));
                    });
        }
    }

}
