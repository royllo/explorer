package org.royllo.explorer.core.test.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockserver.integration.ClientAndServer;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.util.base.Base;
import org.royllo.test.MempoolData;
import org.royllo.test.TapdData;
import org.royllo.test.mempool.GetTransactionValueResponse;
import org.royllo.test.mempool.TransactionValue;
import org.royllo.test.tapd.asset.AssetValue;
import org.royllo.test.tapd.asset.DecodedProofValueResponse;
import org.springframework.test.annotation.DirtiesContext;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

/**
 * Utility classes for tests.
 * TODO Create an annotation to start required mock servers
 */
@SuppressWarnings("SpellCheckingInspection")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class TestWithMockServers extends Base {

    /** Mempool server port. */
    public static final int MEMPOOL_MOCK_SERVER_PORT = 9091;

    /** Tapd server port. */
    public static final int TAPD_MOCK_SERVER_PORT = 9092;

    /** Active royllo coin asset id. TODO Remove this ? */
    public static String ACTIVE_ROYLLO_COIN_ASSET_ID = "1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413";

    /** Mempool mock server. */
    private ClientAndServer mempoolMockServer;

    /** Tapd mock server. */
    private ClientAndServer tapdMockServer;

    @BeforeEach
    public void startServers() {
        // Mempool mock server.
        mempoolMockServer = startClientAndServer(MEMPOOL_MOCK_SERVER_PORT);
        MempoolData.setMockServerRules(mempoolMockServer);
        // Tapd mock server.
        tapdMockServer = startClientAndServer(TAPD_MOCK_SERVER_PORT);
        TapdData.setMockServerRules(tapdMockServer);
    }

    @AfterEach
    public void stopServers() {
        mempoolMockServer.stop();
        tapdMockServer.stop();
    }

    /**
     * Verify if the bitcoin transaction output DTO is equals to the transaction from test.
     *
     * @param bitcoinTransactionOutputDTO Bitcoin transaction output
     * @param transactionId               Transaction id we have to get from test
     */
    public void verifyTransaction(final BitcoinTransactionOutputDTO bitcoinTransactionOutputDTO,
                                  final String transactionId) {
        // We retrieve the transaction from our test data, and we extract the bitcoin transaction output from the transaction value.
        final TransactionValue transactionValue = MempoolData.findTransactionByTransactionId(transactionId);
        final GetTransactionValueResponse.VOut transactionValueVOut = transactionValue.getResponse().getVout().get(bitcoinTransactionOutputDTO.getVout());

        assertEquals(bitcoinTransactionOutputDTO.getBlockHeight(),
                transactionValue.getResponse().getStatus().getBlockHeight(),
                "Block height are not equals");

        assertEquals(bitcoinTransactionOutputDTO.getTxId(),
                transactionValue.getResponse().getTxId(),
                "Transaction id are not equals");

        assertEquals(bitcoinTransactionOutputDTO.getScriptPubKey(),
                transactionValueVOut.getScriptPubKey(),
                "Script pub key are not equals");

        assertEquals(bitcoinTransactionOutputDTO.getScriptPubKeyAsm(),
                transactionValueVOut.getScriptPubKeyAsm(),
                "Script pub key asm are not equals");

        assertEquals(bitcoinTransactionOutputDTO.getScriptPubKeyType(),
                transactionValueVOut.getScriptPubKeyType(),
                "Script pub key type are not equals");

        assertEquals(bitcoinTransactionOutputDTO.getScriptPubKeyAddress(),
                transactionValueVOut.getScriptPubKeyAddress(),
                "Script pub key address are not equals");

        assertEquals(0, bitcoinTransactionOutputDTO.getValue().compareTo(transactionValueVOut.getValue()),
                "Bitcoin transaction amount are not equals");
    }

    /**
     * Verify if the asset group DTO is equals to the asset group from test.
     *
     * @param assetGroupDTO asset group
     * @param assetId       asset id that has this group.
     */
    public void verifyAssetGroup(final AssetGroupDTO assetGroupDTO,
                                 final String assetId) {
        // Finding asset group of the first decoded proof.
        final DecodedProofValueResponse.DecodedProof.Asset assetFromTest = TapdData.findAssetValueByAssetId(assetId).getDecodedProof(0).getAsset();
        if (assetFromTest.getAssetGroup() != null) {
            assertEquals(assetGroupDTO.getRawGroupKey(),
                    assetFromTest.getAssetGroup().getRawGroupKey(),
                    "Raw group key are not equals");
            assertEquals(assetGroupDTO.getTweakedGroupKey(),
                    assetFromTest.getAssetGroup().getTweakedGroupKey(),
                    "Tweaked group key are not equals");
            // TODO there is a comparaison betwenn null and an empty string.
//            assertEquals(assetGroupDTO.getAssetWitness(),
//                    assetFromTest.getAssetGroup().getAssetWitness(),
//                    "Asset witness are not equals");
        } else {
            fail("Asset group is null");
        }
    }

    /**
     * Verify if the asset DTO is equals to the asset from test.
     *
     * @param assetDTO asset
     * @param assetId  asset id
     */
    public void verifyAsset(final AssetDTO assetDTO,
                            final String assetId) {
        // We retrieve the asset from our test data.
        final AssetValue assetValue = TapdData.findAssetValueByAssetId(assetId);

        // We retrieve an asset value from test data. We should to get the data from the first decioded proof.
        final DecodedProofValueResponse.DecodedProof.Asset assetFromTest = assetValue.getDecodedProofValues().get(0).getResponse().getDecodedProof().getAsset();

        // We check asset group.
        boolean assetGroupEquality = true;
        if (assetDTO.getAssetGroup() != null) {
            // If the asset group is null, we return false.
            if (assetFromTest.getAssetGroup() == null) {
                fail("There should not be a null asset group");
            }
            assetGroupEquality = Objects.equals(assetDTO.getAssetGroup().getAssetWitness(), assetFromTest.getAssetGroup().getAssetWitness()) &&
                    Objects.equals(assetDTO.getAssetGroup().getRawGroupKey(), assetFromTest.getAssetGroup().getRawGroupKey()) &&
                    Objects.equals(assetDTO.getAssetGroup().getTweakedGroupKey(), assetFromTest.getAssetGroup().getTweakedGroupKey());
        }

        // We compare each field.
        assertEquals(assetDTO.getAssetId(),
                assetValue.getAssetId(),
                "Asset id are not equals");

        verifyTransaction(assetDTO.getGenesisPoint(),
                assetDTO.getGenesisPoint().getTxId());

        assertEquals(assetDTO.getMetaDataHash(),
                assetFromTest.getAssetGenesis().getMetaDataHash(),
                "Metadata hash are not equals");

        assertEquals(assetDTO.getName(),
                assetFromTest.getAssetGenesis().getName(),
                "Name are not equals");

        assertEquals(assetDTO.getOutputIndex().longValue(),
                assetFromTest.getAssetGenesis().getOutputIndex(),
                "Output index are not equals");

        assertEquals(assetDTO.getVersion(),
                assetFromTest.getAssetGenesis().getVersion(),
                "Version are not equals");

        assertEquals(assetDTO.getType().toString(),
                assetFromTest.getAssetType(),
                "Type are not equals");

        assertEquals(0, assetDTO.getAmount().compareTo(assetFromTest.getAmount()),
                "Amount are not equals");

        assertTrue(assetGroupEquality,
                "Asset group are not equals");
    }

    /**
     * Verify if the asset state DTO is equals to the asset state from test.
     *
     * @param assetStateDTO asset state
     * @param assetStateId  asset state id
     */
    public void verifyAssetState(final AssetStateDTO assetStateDTO,
                                 final String assetStateId) {
        // We find the asset state.
        final Optional<DecodedProofValueResponse.DecodedProof> assetState = TapdData.findAssetStateByAssetStateId(assetStateId);
        assertTrue(assetState.isPresent(),
                "Asset state not found");

        // We compare each field.
        // TODO Check each field
        assertEquals(assetStateDTO.getAnchorBlockHash(),
                assetState.get().getAsset().getChainAnchor().getAnchorBlockHash(),
                "Anchor block hash are not equals");

        assertEquals(assetStateDTO.getAnchorOutpoint().getTxId(),
                assetState.get().getAsset().getChainAnchor().getAnchorTxId(),
                "Anchor outpoint tx id are not equals");

        assertEquals(assetStateDTO.getAnchorTx(),
                assetState.get().getAsset().getChainAnchor().getAnchorTx(),
                "Anchor tx are not equals");

        assertEquals(assetStateDTO.getInternalKey(),
                assetState.get().getAsset().getChainAnchor().getInternalKey(),
                "Internal key are not equals");

        assertEquals(assetStateDTO.getMerkleRoot(),
                assetState.get().getAsset().getChainAnchor().getMerkleRoot(),
                "Merkle root are not equals");

        assertEquals(assetStateDTO.getTapscriptSibling(),
                assetState.get().getAsset().getChainAnchor().getTapscriptSibling(),
                "Tapscript sibling are not equals");

        assertEquals(assetStateDTO.getScriptVersion(),
                assetState.get().getAsset().getScriptVersion(),
                "Script version are not equals");

        assertEquals(assetStateDTO.getScriptKey(),
                assetState.get().getAsset().getScriptKey(),
                "Script key are not equals");
    }

    /**
     * Verify if the asset state DTO is equals to the asset state from test.
     *
     * @param assetStateDTO asset state
     * @param assetId       asset id
     * @param outpointTxId  outpoint tx id
     * @param outpointVout  outpoint vout
     * @param scriptKey     scriptkey
     */
    public void verifyAssetState(final AssetStateDTO assetStateDTO,
                                 final String assetId,
                                 final String outpointTxId,
                                 final int outpointVout,
                                 final String scriptKey) {
        // Calculate unique value and its hash to retrieve and asset state
        String uniqueValue = assetId
                + "_" + outpointTxId + ":" + outpointVout
                + "_" + scriptKey;
        String assetStateId = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(uniqueValue.getBytes(UTF_8));
            assetStateId = DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available: " + e.getMessage());
        }

        verifyAssetState(assetStateDTO, assetStateId);
    }

}