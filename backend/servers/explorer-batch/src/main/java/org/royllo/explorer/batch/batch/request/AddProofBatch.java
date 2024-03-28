package org.royllo.explorer.batch.batch.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.asset.AssetDTOIssuanceUpdate;
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

import java.time.ZoneId;
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

    /** TapdService service. */
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
        logger.info("Processing add proof requests");
        if (enabled.get()) {
            requestService.getOpenedRequests()
                    .stream()
                    .filter(request -> request instanceof AddProofRequestDTO)
                    .map(requestDTO -> (AddProofRequestDTO) requestDTO)
                    .forEach(request -> {
                        logger.info("Processing request {}: {}", request.getId(), request);
                        try {
                            // If we already have this proof in database, we don't process it.
                            proofService.getProofByProofId(sha256(request.getProof())).ifPresent(proof -> {
                                throw new ProofCreationException("Proof already exists in royllo");
                            });

                            // =========================================================================================
                            // We decode all proofs, one by one, starting by the oldest (issuance proof).
                            boolean proofAdded = false;
                            for (long i = getNumberOfProofs(request.getProof()); i > 0; i--) {

                                // -------------------------------------------------------------------------------------
                                // We start by calling the Tapd service to decode the proof and get the meta reveal.
                                // We start with the one at the end of the proof file, and we go back to top.
                                DecodedProofResponse response = tapdService.decode(request.getProof(), i - 1, true).block();
                                if (response == null || response.getErrorCode() != null) {
                                    // If we get an error, we do it without meta reveal.
                                    response = tapdService.decode(request.getProof(), i - 1, false).block();
                                }

                                // -------------------------------------------------------------------------------------
                                // We now check if the response is correct.
                                if (response == null) {
                                    // Response is null.
                                    throw new ProofCreationException("Decoded proof is null");
                                } else {
                                    // Error code is present in response.
                                    if (response.getErrorCode() != null) {
                                        throw new ProofCreationException("Proof cannot be decoded because of this error: " + response.getErrorMessage());
                                    }
                                }

                                // -------------------------------------------------------------------------------------
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

                                // -------------------------------------------------------------------------------------
                                // We added the asset, asset group and asset states, if not already added, we add the proof.
                                if (!proofAdded) {
                                    proofService.addProof(request.getProof(), request.getType(), response);
                                    request.setAsset(assetStateCreated.get().getAsset());
                                    proofAdded = true;
                                }
                                request.success();

                                // -------------------------------------------------------------------------------------
                                // Bonus process.
                                // We are on the issuance proof, so, it not yet done, we update the issuance data.
                                if (assetStateCreated.get().getAsset().getMetaDataFileName() == null && response.getDecodedProof().getMetaReveal() != null) {
                                    logger.info("Updating issuance data for asset {}", assetStateCreated.get().getAsset().getAssetId());
                                    assetService.updateAssetIssuanceData(response.getDecodedProof().getAsset().getAssetGenesis().getAssetId(),
                                            AssetDTOIssuanceUpdate.builder()
                                                    .metadata(response.getDecodedProof().getMetaReveal().getData())
                                                    .amount(response.getDecodedProof().getAsset().getAmount())
                                                    .issuanceDate(assetStateCreated.get().getAnchorOutpoint().getBlockTime().atZone(ZoneId.of("Europe/Paris")))
                                                    .build());
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

                        // We save the request status.
                        logger.info("Proof {} added: {} ", request.getProof(), request);
                        requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(request));
                    });
        }
    }

    /**
     * Returns the number of proofs in a proof file.
     *
     * @param proof the proof file
     * @return the number of proofs
     */
    private long getNumberOfProofs(final String proof) {
        DecodedProofResponse response = tapdService.decode(proof).block();
        if (response == null) {
            logger.info("Decoded proof is null for proof {}", proof);
            throw new ProofCreationException("Decoded proof is null");
        } else {
            if (response.getErrorCode() != null) {
                logger.info("roof cannot be decoded because of this error: {}", response.getErrorMessage());
                throw new ProofCreationException(response.getErrorMessage());
            } else {
                return response.getDecodedProof().getNumberOfProofs();
            }
        }
    }

}
