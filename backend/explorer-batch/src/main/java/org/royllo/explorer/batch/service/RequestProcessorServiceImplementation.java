package org.royllo.explorer.batch.service;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.BaseProcessor;
import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.repository.bitcoin.BitcoinTransactionOutputRepository;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;

/**
 * Request processor service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class RequestProcessorServiceImplementation extends BaseProcessor implements RequestProcessorService {

    /** Bitcoin transaction output repository. */
    private final BitcoinTransactionOutputRepository bitcoinTransactionOutputRepository;

    /** Request repository. */
    private final RequestRepository requestRepository;

    /** Asset service. */
    private final AssetService assetService;

    @Override
    public RequestDTO processRequest(final RequestDTO requestDTO) {
        assert !requestDTO.getStatus().isFinalStatus() : "This request has already been treated";

        // Add asset request.
        // TODO Simplify when pattern Matching for switch will be available.
        if (requestDTO.getClass().equals(AddAssetRequestDTO.class)) {
            return processAddAssetRequest((AddAssetRequestDTO) requestDTO);
        }
        return null;
    }

    /**
     * Process add asset request.
     *
     * @param addAssetRequestDTO request to process
     * @return process result
     */
    private RequestDTO processAddAssetRequest(final AddAssetRequestDTO addAssetRequestDTO) {
        // =============================================================================================================
        // We check if the transaction can be found in the blockchain or in our database.
        final Optional<BitcoinTransactionOutput> transactionOutput = bitcoinTransactionOutputRepository.findByTxIdAndVout(addAssetRequestDTO.getTxId(), addAssetRequestDTO.getVout());
        if (transactionOutput.isEmpty()) {
            // Transaction not found in database.
            addAssetRequestDTO.failed("Transaction not found (" + addAssetRequestDTO.getGenesisPoint() + ")");
            requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(addAssetRequestDTO));
            return addAssetRequestDTO;
        }

        // =============================================================================================================
        // We check the provided proof.
        if (!"VALID_PROOF".equals(addAssetRequestDTO.getProof())) {
            addAssetRequestDTO.failed("Invalid proof");
            requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(addAssetRequestDTO));
            return addAssetRequestDTO;
        }

        // =============================================================================================================
        // Everything is ok, we now create the asset, update the request and return it.
        final AssetDTO assetCreated = assetService.addAsset(
                AssetDTO.builder()
                        .genesisPoint(BITCOIN_MAPPER.mapToBitcoinTransactionOutputDTO(transactionOutput.get()))
                        .creator(ANONYMOUS_USER)
                        .name(addAssetRequestDTO.getName())
                        .metaData(addAssetRequestDTO.getMetaData())
                        .assetId(addAssetRequestDTO.getAssetId())
                        .outputIndex(addAssetRequestDTO.getOutputIndex())
                        .build()
        );
        addAssetRequestDTO.setAsset(assetCreated);
        addAssetRequestDTO.succeed();
        requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(addAssetRequestDTO));
        return addAssetRequestDTO;
    }

}
