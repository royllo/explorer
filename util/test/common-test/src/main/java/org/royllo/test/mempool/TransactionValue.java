package org.royllo.test.mempool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    /**
     * Returns the response in JSON.
     *
     * @return json response
     */
    public String getJSONResponse() {
        try {
            return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Impossible to transform to JSON" + e);
        }
    }

}
