package org.royllo.explorer.batch.service;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.BaseProcessor;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.provider.tarod.TarodProofService;
import org.royllo.explorer.core.repository.bitcoin.BitcoinTransactionOutputRepository;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.asset.AssetService;
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
            tarodProofService.decode(addProofRequestDTO.getRawProof(), 0).subscribe(decodedProofResponse -> {
                //

            });
        } catch (Throwable tarodError) {
            // We failed on calling tarod.
            addProofRequestDTO.failed(tarodError.getMessage());
        }

        // We save the request.
        requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(addProofRequestDTO));


        // From the request AddProofRequestDTO, we retrieve the raw proof.

        // We use TarodProofService.decode() with raw proof and '1' as proof index.
        // If (decode() returns an error) : ERROR -> "Invalid proof".
        // If SUCCESS -> We retrieve the DecodedProof.

        // Calling assetService.getAssetByAssetId() with DecodedProof.assetId.
        // If the asset doesn't already exist, we create the asset.
        // We retrieve the asset ID.

        // With DecodedProof, we call createProof() from ProofService with a link to the existing asset.

        return null;

        // =============================================================================================================
        // We check if the transaction can be found in the blockchain or in our database.
//        final Optional<BitcoinTransactionOutput> transactionOutput = bitcoinTransactionOutputRepository.findByTxIdAndVout(addProofDTO.getTxId(), addProofDTO.getVout());
//        if (transactionOutput.isEmpty()) {
//            // Transaction not found in database.
////            logger.info("processAddAssetRequest {} - Error - Transaction not found: {}", addProofDTO.getId(),
////                    addProofDTO.getGenesisPoint());
////            addProofDTO.failed("Transaction not found (" + addProofDTO.getGenesisPoint() + ")");
////            requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(addProofDTO));
//            return addProofDTO;
//        }

//        // =============================================================================================================
//        // We check the provided proof.
//        if (!"VALID_PROOF".equals(addProofDTO.getRawProof())) {
//            logger.info("processAddAssetRequest {} - Error - Invalid proof: {}", addProofDTO.getId(),
//                    addProofDTO.getRawProof());
//            addProofDTO.failed("Invalid proof");
//            requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(addProofDTO));
//            return addProofDTO;
//        }
//
//        // =============================================================================================================
//        // Everything is ok, we now create the asset, update the request and return it.
//        final AssetDTO assetDTOCreated = assetService.addAsset(
//                AssetDTO.builder()
//                        .genesisPoint(BITCOIN_MAPPER.mapToBitcoinTransactionOutputDTO(transactionOutput.get()))
//                        .creator(ANONYMOUS_USER)
//                        .build()
//        );
//        addProofDTO.setAsset(assetDTOCreated);
//        addProofDTO.succeed();
//        requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(addProofDTO));
//        logger.info("processAddAssetRequest {} - Success - Request {} has updated asset {}", addProofDTO.getId(),
//                addProofDTO,
//                assetDTOCreated);
//        return addProofDTO;
    }

}
