package org.royllo.explorer.batch.service;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.BaseProcessor;
import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.core.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.repository.bitcoin.BitcoinTransactionOutputRepository;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public RequestDTO processRequest(final RequestDTO requestDTO) {
        // Add asset request.
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
            addAssetRequestDTO.setErrorMessage("Transaction not found (" + addAssetRequestDTO.getGenesisPoint() + ")");
            requestRepository.save(REQUEST_MAPPER.mapToAddAssetRequest(addAssetRequestDTO));
            return addAssetRequestDTO;
        }

        // =============================================================================================================
        // We check the provided proof.

        // =============================================================================================================
        // Everything is ok, we now create the asset.

        return null;
    }

}
