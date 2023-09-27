package org.royllo.test.mempool;

import lombok.Getter;

/**
 * Transaction value.
 */
@Getter
@SuppressWarnings("checkstyle:VisibilityModifier")
public class TransactionValue {

    /** Bitcoin transaction id. */
    String txId;

    /** Response. */
    GetTransactionValueResponse response;

    /**
     * Constructor.
     *
     * @param newResponse response
     */
    public TransactionValue(final GetTransactionValueResponse newResponse) {
        this.txId = newResponse.getTxId();
        this.response = newResponse;
    }

}
