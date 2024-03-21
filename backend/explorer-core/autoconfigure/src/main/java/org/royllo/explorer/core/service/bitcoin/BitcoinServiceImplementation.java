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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

/**
 * {@link BitcoinService} implementation.
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
        logger.info("Getting transaction output for txId:vout {}:{}", txId, vout);

        return Optional.ofNullable(bitcoinTransactionOutputRepository.findByTxId(txId)
                .stream()
                .map(BITCOIN_MAPPER::mapToBitcoinTransactionOutputDTO)
                .filter(transaction -> transaction.getVout() == vout)
                .peek(transaction -> logger.info("Transaction output for txId:vout {}:{} found in database", txId, vout))
                .findFirst()
                .orElseGet(() -> {
                            // Transaction is not in the database, we call mempool to see if it exists.
                            final GetTransactionResponse transaction = mempoolTransactionService.getTransaction(txId)
                                    .onErrorResume(throwable -> Mono.empty())
                                    .block();
                            if (transaction == null || vout < 0 || transaction.getVout().size() <= vout) {
                                logger.info("Transaction output for txId:vout {}:{} not found in mempool", txId, vout);
                                return null;
                            }

                            // We save the transaction output in the database.
                            GetTransactionResponse.VOut output = (GetTransactionResponse.VOut) transaction.getVout().toArray()[vout];
                            final BitcoinTransactionOutput bto = BITCOIN_MAPPER.mapToBitcoinTransactionOutput(
                                    BitcoinTransactionOutputDTO.builder()
                                            .blockHeight(transaction.getStatus().getBlockHeight())
                                            .blockTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(transaction.getStatus().getBlockTime()),
                                                    TimeZone.getDefault().toZoneId()))
                                            .txId(txId)
                                            .vout(vout)
                                            .scriptPubKey(output.getScriptPubKey())
                                            .scriptPubKeyAsm(output.getScriptPubKeyAsm())
                                            .scriptPubKeyType(output.getScriptPubKeyType())
                                            .scriptPubKeyAddress(output.getScriptPubKeyAddress())
                                            .value(output.getValue())
                                            .build());
                            BitcoinTransactionOutput transactionOutputCreated = bitcoinTransactionOutputRepository.save(bto);
                            logger.info("Transaction output for txId:vout {}:{} found in mempool and saved in database", txId, vout);
                            return BITCOIN_MAPPER.mapToBitcoinTransactionOutputDTO(transactionOutputCreated);
                        }
                ));
    }

}
