package org.royllo.explorer.api.batch;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.domain.universe.Update;
import org.royllo.explorer.api.dto.bitcoin.TransactionOutputDTO;
import org.royllo.explorer.api.repository.universe.UpdateRepository;
import org.royllo.explorer.api.service.bitcoin.BitcoinService;
import org.royllo.explorer.api.util.base.BaseBatch;
import org.royllo.explorer.api.util.enums.UpdateRequestStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Update batch - In charge of treating every update.
 */
@Service
@RequiredArgsConstructor
public class UpdateBatch extends BaseBatch {

    /** Update repository. */
    private final UpdateRepository updateRepository;

    /** Bitcoin service. */
    private final BitcoinService bitcoinService;

    /**
     * Process requests.
     */
    public void run() {
        updateRepository.findByStatusNotIn(UpdateRequestStatus.finalStatus())
                .stream()
                .peek(updateRequest -> logger.info("Treating update request n°{}", updateRequest.getId()))
                .forEach(updateRequest -> {
                   switch (updateRequest.getStatus()) {
                       case NEW -> checkTransactionOutput(updateRequest);
                       case TRANSACTION_OUTPUT_CHECKED -> logger.info("TRANSACTION_OUTPUT_CHECKED");
                       default -> logger.info("Update request {} not treated", updateRequest.getId());
                   }
                });
    }

    /**
     * Step 1 : Check transaction output (transaction output exists, and it's a locked with taproot script).
     *
     * @param update update
     * @return the new status for the update request
     */
    public UpdateRequestStatus checkTransactionOutput(final Update update) {
        if (update.getStatus() != UpdateRequestStatus.NEW) {
            return null;
        }

        // Checking the transaction output.
        logger.info("Update {}: Check transaction output", getFormattedId(update.getId()));
        final Optional<TransactionOutputDTO> output = bitcoinService.getBitcoinTransactionOutput(update.getTxId(), update.getVout());
        if (output.isEmpty()) {
            // The transaction output does not exist.
            update.setStatus(UpdateRequestStatus.ERROR);
            update.setFailureStatus(UpdateRequestStatus.TRANSACTION_OUTPUT_CHECKED);
            update.setFailureMessage("Transaction output not found");
            logger.info("Update {}: Transaction output not found", getFormattedId(update.getId()));
        } else if (!output.get().isTaprootType()) {
            // The transaction is not a taproot transaction.
            update.setStatus(UpdateRequestStatus.ERROR);
            update.setFailureStatus(UpdateRequestStatus.TRANSACTION_OUTPUT_CHECKED);
            update.setFailureMessage("Transaction type is not taproot but " + output.get().getScriptPubKeyType());
            logger.info("Update {}: Transaction type is not taproot but {}", getFormattedId(update.getId()), output.get().getScriptPubKeyType());
        } else {
            // It's ok, output exists, and it's a taproot transaction.
            update.setStatus(UpdateRequestStatus.TRANSACTION_OUTPUT_CHECKED);
            logger.info("Update {}: Transaction output successfully checked", getFormattedId(update.getId()));
        }

        // Saving the update request.
        updateRepository.save(update);
        return update.getStatus();
    }

}
