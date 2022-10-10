package org.royllo.explorer.api.test.core.web.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.client.generated.DgsConstants;
import org.royllo.explorer.api.client.generated.client.BitcoinTransactionOutputGraphQLQuery;
import org.royllo.explorer.api.client.generated.client.BitcoinTransactionOutputProjectionRoot;
import org.royllo.explorer.api.client.generated.types.BitcoinTransactionOutput;
import org.royllo.explorer.api.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("BitcoinTransactionOutput tests")
public class BitcoinTransactionOutputDataFetcherTest extends BaseTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("getBitcoinTransactionOutputByTxIdAndVout()")
    public void getBitcoinTransactionOutputByTxIdAndVout() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                BitcoinTransactionOutputGraphQLQuery.newRequest().txId(BITCOIN_TRANSACTION_1_TXID).vout(0).build(),
                new BitcoinTransactionOutputProjectionRoot()
                        .id()
                        .blockHeight()
                        .txId()
                        .vout()
                        .scriptPubKey()
                        .scriptPubKeyAsm()
                        .scriptPubKeyType()
                        .scriptPubKeyAddress()
                        .value()
                );

        BitcoinTransactionOutput transactionOutput = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.BitcoinTransactionOutput,
                new TypeRef<>() {
                });

        assertNotNull(transactionOutput);
        assertEquals(720, transactionOutput.getBlockHeight().intValue());
        assertEquals("2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea", transactionOutput.getTxId());
        assertEquals(0, transactionOutput.getVout());
        assertEquals("410415ca91c387efac4ea86f0196b2a1d831f75a488f115055636f0022c51df508197ad4fc31f553d48052b05b0a3b6030a84a0441adae97734129bb8ea0ddfd4004ac", transactionOutput.getScriptPubKey());
        assertEquals("OP_PUSHBYTES_65 0415ca91c387efac4ea86f0196b2a1d831f75a488f115055636f0022c51df508197ad4fc31f553d48052b05b0a3b6030a84a0441adae97734129bb8ea0ddfd4004 OP_CHECKSIG", transactionOutput.getScriptPubKeyAsm());
        assertEquals("p2pk", transactionOutput.getScriptPubKeyType());
        assertNull(transactionOutput.getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("5000000000").compareTo(transactionOutput.getValue()));
    }

}
