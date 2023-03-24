package org.royllo.explorer.batch.service;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.BaseProcessor;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;
import org.royllo.explorer.core.provider.tarod.TarodProofService;
import org.royllo.explorer.core.repository.bitcoin.BitcoinTransactionOutputRepository;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.springframework.stereotype.Service;

/**
 * Request processor service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class RequestProcessorServiceImplementation extends BaseProcessor implements RequestProcessorService {

    /** Bitcoin transaction output repository. */
    private final BitcoinTransactionOutputRepository bitcoinTransactionOutputRepository;

    /** Tarod proof service. */
    private final TarodProofService tarodProofService;

    /** Request repository. */
    private final RequestRepository requestRepository;

    /** Asset service. */
    private final AssetService assetService;

    /** Proof service. */
    private final ProofService proofService;

    @Override
    public RequestDTO processRequest(final RequestDTO requestDTO) {
        assert !requestDTO.getStatus().isFinalStatus() : "This request has already been treated";

        // Add asset request.
        // TODO Simplify when pattern Matching for switch will be available.
        if (requestDTO.getClass().equals(AddProofRequestDTO.class)) {
            return processAddAssetRequest((AddProofRequestDTO) requestDTO);
        }
        return null;
    }

    /**
     * Process add asset request.
     *
     * @param addProofRequestDTO request to process
     * @return process result
     */
    private RequestDTO processAddAssetRequest(final AddProofRequestDTO addProofRequestDTO) {
        logger.info("processAddAssetRequest {} - Processing request {}", addProofRequestDTO.getId(), addProofRequestDTO);

        // We try to decode the proof.
        try {
            final DecodedProofResponse decodedProofResponse = tarodProofService.decode(addProofRequestDTO.getRawProof(), 0).block();

            // We check if we have a decoded proof response.
            if (decodedProofResponse == null) {
                addProofRequestDTO.failed("Decoded proof is null");
            } else {
                // We check if we had an error deciding the response.
                if (decodedProofResponse.getErrorCode() != null) {
                    addProofRequestDTO.failed(decodedProofResponse.getErrorMessage());
                } else {
                    // We have the decoded proof, we check if the asset exists.
                    final String assetId = decodedProofResponse.getDecodedProof().getAsset().getAssetGenesis().getAssetId();
                    if (assetService.getAssetByAssetId(assetId).isEmpty()) {
                        // If it doesn't exist, we create it
                        logger.info("processAddAssetRequest {} - Adding asset {}", addProofRequestDTO.getId(), assetId);
                        assetService.addAsset(ASSET_MAPPER.mapToAssetDTO(decodedProofResponse.getDecodedProof()));
                    } else {
                        logger.info("processAddAssetRequest {} - Asset {} already exists", addProofRequestDTO.getId(), assetId);
                    }
                    // We can now add the proof.
                    proofService.addProof(addProofRequestDTO.getRawProof(), decodedProofResponse);
                    addProofRequestDTO.succeed();
                }
            }
        } catch (Throwable tarodError) {
            // We failed on calling tarod.
            addProofRequestDTO.failed(tarodError.getMessage());
        }

        // We save the request.
        requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(addProofRequestDTO));
        return addProofRequestDTO;
    }

}
