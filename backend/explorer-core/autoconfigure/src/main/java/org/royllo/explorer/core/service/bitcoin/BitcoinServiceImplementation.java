package org.royllo.explorer.core.service.bitcoin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.provider.mempool.GetTransactionResponse;
import org.royllo.explorer.core.provider.mempool.MempoolTransactionService;
import org.royllo.explorer.core.repository.bitcoin.BitcoinTransactionOutputRepository;
import org.royllo.explorer.core.util.base.BaseService;
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
    private final BitcoinTransactionOutputRepository bitcoinTransactionOutputRepository;

    /** Mempool transaction service. */
    private final MempoolTransactionService mempoolTransactionService;

    @Override
    public Optional<BitcoinTransactionOutputDTO> getBitcoinTransactionOutput(@NonNull final String txId,
                                                                             final int vout) {
        logger.info("Getting transaction output with txId:vout {}:{}", txId, vout);

        // =============================================================================================================
        // We check if we have the transaction in our database.
        // We retrieve the transaction outputs of the corresponding txId.
        List<BitcoinTransactionOutput> bitcoinTransactionOutputs = bitcoinTransactionOutputRepository.findByTxId(txId);
        if (!bitcoinTransactionOutputs.isEmpty()) {
            // The txid is in our database so now, we search for the output asked (vout):
            // - If found, we return it.
            // - If NOT found, the output don't exist and will never exist, so we return null.
            logger.info("The transaction {} is already in our database", txId);
            final Optional<BitcoinTransactionOutputDTO> output = bitcoinTransactionOutputs.stream()
                    .filter(bto -> bto.getVout() == vout)
                    .map(BITCOIN_MAPPER::mapToBitcoinTransactionOutputDTO)
                    .findFirst();
            if (output.isPresent()) {
                logger.info("Output {} of the transaction {} is already in our database", vout, txId);
                return output;
            } else {
                logger.info("Output {} of the transaction {} is NOT in our database", vout, txId);
            }
        }

        // =============================================================================================================
        // The transaction is NOT in the database.
        // We are using the mempool service to check if we can find it in the Bitcoin blockchain.
        logger.info("The transaction {} is NOT in our database, we call mempool", txId);
        final GetTransactionResponse transaction = mempoolTransactionService.getTransaction(txId)
                .onErrorResume(throwable -> Mono.empty())
                .block();

        if (transaction == null) {
            // Transaction is null, it means we did not retrieve the transaction (doesn't exist or server error).
            logger.info("Transaction {} is NOT in the blockchain", txId);
            return Optional.empty();
        } else {
            // The transaction is in the database, now we have to know if the requested output exists.
            // Imagine we have 3 vout ==> vout.size = 3. The vout we are searching for must be inferior to vout size.
            if (vout >= transaction.getVout().size()) {
                logger.info("Transaction {} is in the blockchain but output {} doesn't exist", txId, vout);
                return Optional.empty();
            } else {
                // The transaction and the output exist and have been retrieved from the mempool service.
                // Now, we save it in database and return it.
                logger.info("The transaction and it's output {}/{} is in the blockchain, we save it", txId, vout);
                GetTransactionResponse.VOut output = (GetTransactionResponse.VOut) transaction.getVout().toArray()[vout];
                final BitcoinTransactionOutput bto = BITCOIN_MAPPER.mapToBitcoinTransactionOutput(
                        BitcoinTransactionOutputDTO.builder()
                                .blockHeight(transaction.getStatus().getBlockHeight())
                                .txId(txId)
                                .vout(vout)
                                .scriptPubKey(output.getScriptPubKey())
                                .scriptPubKeyAsm(output.getScriptPubKeyAsm())
                                .scriptPubKeyType(output.getScriptPubKeyType())
                                .scriptPubKeyAddress(output.getScriptPubKeyAddress())
                                .value(output.getValue())
                                .build());
                return Optional.of(BITCOIN_MAPPER.mapToBitcoinTransactionOutputDTO(bitcoinTransactionOutputRepository.save(bto)));
            }
        }
    }

}
