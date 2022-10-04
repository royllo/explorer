package org.royllo.explorer.api.service.bitcoin;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.domain.bitcoin.TransactionOutput;
import org.royllo.explorer.api.dto.bitcoin.TransactionOutputDTO;
import org.royllo.explorer.api.provider.mempool.GetTransactionResponse;
import org.royllo.explorer.api.provider.mempool.MempoolTransactionService;
import org.royllo.explorer.api.repository.bitcoin.TransactionOutputRepository;
import org.royllo.explorer.api.util.base.BaseService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * Bitcoin service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class BitcoinServiceImplementation extends BaseService implements BitcoinService {

    /** Bitcoin transaction output repository. */
    private final TransactionOutputRepository transactionOutputRepository;

    /** Mempool transaction service. */
    private final MempoolTransactionService mempoolTransactionService;

    @Override
    public Optional<TransactionOutputDTO> getBitcoinTransactionOutput(final String txId, final int vOut) {

        // =============================================================================================================
        // We check if we have the transaction in our database.
        // We retrieve the transaction outputs of the corresponding txId.
        List<TransactionOutput> transactionOutputs = transactionOutputRepository.findByTxId(txId);
        if (!transactionOutputs.isEmpty()) {
            // The txid is in our database so now, we search for the output asked (vOut):
            // - If found, we return it.
            // - If NOT found, the output don't exist and will never exist, so we return null.
            logger.debug("The transaction {} is already in our database", txId);
            final Optional<TransactionOutputDTO> output = transactionOutputs.stream()
                    .filter(bto -> bto.getVout() == vOut)
                    .map(BITCOIN_MAPPER::mapToBitcoinTransactionOutputDTO)
                    .findFirst();
            if (output.isPresent()) {
                logger.debug("Output {} of the transaction {} is already in our database", vOut, txId);
                return output;
            } else {
                logger.debug("Output {} of the transaction {} is NOT in our database", vOut, txId);
            }
        }

        // =============================================================================================================
        // The transaction is NOT in the database.
        // We are using the mempool service to check if we can find it in the Bitcoin blockchain.
        logger.debug("The transaction {} is NOT in our database, we call mempool", txId);
        final GetTransactionResponse transaction = mempoolTransactionService.getTransaction(txId)
                .onErrorResume(throwable -> Mono.empty())
                .block();

        if (transaction == null) {
            // Transaction is null, it means we did not retrieve the transaction (doesn't exist or server error).
            logger.debug("Transaction {} is NOT in the blockchain", txId);
            return Optional.empty();
        } else {
            // The transaction is in the database, now we have to know if the requested output exists.
            // Imagine we have 3 vout ==> vout.size = 3. The vOut we are searching for must be inferior to vout size.
            if (vOut >= transaction.getVout().size()) {
                logger.debug("Transaction {} is in the blockchain but output {} doesn't exist", txId, vOut);
                return Optional.empty();
            } else {
                // The transaction and the output exist and have been retrieved from the mempool service.
                // Now, we save it in database and return it.
                logger.debug("The transaction and it's output {}/{} is in the blockchain, we save it", txId, vOut);
                GetTransactionResponse.VOut output = (GetTransactionResponse.VOut) transaction.getVout().toArray()[vOut];
                final TransactionOutput bto = BITCOIN_MAPPER.mapToBitcoinTransactionOutput(
                    TransactionOutputDTO.builder()
                        .blockHeight(transaction.getStatus().getBlockHeight())
                        .txId(txId)
                        .vout(vOut)
                        .scriptPubKey(output.getScriptPubKey())
                        .scriptPubKeyAsm(output.getScriptPubKeyAsm())
                        .scriptPubKeyType(output.getScriptPubKeyType())
                        .scriptPubKeyAddress(output.getScriptPubKeyAddress())
                        .value(output.getValue())
                        .build());
                return Optional.of(BITCOIN_MAPPER.mapToBitcoinTransactionOutputDTO(transactionOutputRepository.save(bto)));
            }
        }
    }

}
