package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mempoolTransactionServiceMock")
@DisplayName("AssetService tests")
public class AssetServiceTest extends BaseTest {

    @Autowired
    private BitcoinService bitcoinService;

    @Autowired
    private AssetService assetService;

    @Test
    @DisplayName("queryAssets()")
    public void queryAssets() {
        // Test on two coins in database : "royllostar" and "starbackrcoin"

        // Searching for an asset that doesn't exist.
        Page<AssetDTO> results = assetService.queryAssets("NON-EXISTING", 1, 5);
        assertEquals(0, results.getTotalElements());
        assertEquals(0, results.getTotalPages());

        // Searching for an asset with its asset id.
        results = assetService.queryAssets(ASSET_ID_NUMBER_01,1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(1, results.getContent().get(0).getId());

        // Searching for an asset with its partial name - only 1 result.
        results = assetService.queryAssets("back",1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(1, results.getContent().get(0).getId());

        // Searching for an asset with its partial name uppercase - only 1 result.
        results = assetService.queryAssets("BACK",1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(1, results.getContent().get(0).getId());

        // Searching for an asset with its partial name uppercase - only 1 result.
        results = assetService.queryAssets("ROYLLO",1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(2, results.getContent().get(0).getId());

        // Searching for an asset with its partial name corresponding to two assets.
        results = assetService.queryAssets("star",1, 5);
        assertEquals(2, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        Set<Long> ids = results.stream()
                .map(AssetDTO::getId)
                .collect(Collectors.toSet());
        assertTrue(ids.contains(1L));
        assertTrue(ids.contains(2L));

        // We have 9 assets to tests pagination.

        // Searching for the 9 assets with a page size of 4.
        results = assetService.queryAssets("TestPaginationCoin",1, 4);
        assertEquals(9, results.getTotalElements());
        assertEquals(3, results.getTotalPages());

        // Searching for the 9 assets with a page size of 5 - Page 0.
        results = assetService.queryAssets("TestPaginationCoin",1, 5);
        assertEquals(5, results.getNumberOfElements());
        assertEquals(9, results.getTotalElements());
        assertEquals(2, results.getTotalPages());
        assertEquals(1009, results.getContent().get(0).getId());
        assertEquals(1001, results.getContent().get(1).getId());
        assertEquals(1002, results.getContent().get(2).getId());
        assertEquals(1003, results.getContent().get(3).getId());
        assertEquals(1004, results.getContent().get(4).getId());

        // Searching for the 9 assets with a page size of 5 - Page 1.
        results = assetService.queryAssets("TestPaginationCoin",2, 5);
        assertEquals(4, results.getNumberOfElements());
        assertEquals(9, results.getTotalElements());
        assertEquals(2, results.getTotalPages());
        assertEquals(1005, results.getContent().get(0).getId());
        assertEquals(1006, results.getContent().get(1).getId());
        assertEquals(1007, results.getContent().get(2).getId());
        assertEquals(1008, results.getContent().get(3).getId());
    }

    @Test
    @DisplayName("addAsset()")
    public void addAsset() {
        // =============================================================================================================
        // First test - Trying to save an existing asset.
        try {
            assetService.addAsset(AssetDTO.builder()
                    .id(1L)
                    .build());
        } catch (AssertionError e) {
            assertEquals("Asset already exists", e.getMessage());
        }

        // =============================================================================================================
        // Second test - Bitcoin transaction is null.
        try {
            assetService.addAsset(AssetDTO.builder()
                    .build());
        } catch (AssertionError e) {
            assertEquals("Bitcoin transaction is required", e.getMessage());
        }

        // =============================================================================================================
        // Third test - AssetId is already in the database.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_1_TXID, 0);
        assertTrue(bto.isPresent());

        try {
            assetService.addAsset(AssetDTO.builder()
                    .genesisPoint(bto.get())
                    .assetId("b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e")
                    .build());
        } catch (AssertionError e) {
            assertTrue(e.getMessage().endsWith("already registered"));
        }

        // =============================================================================================================
        // Now adding a real asset - WE DIT NOT SEARCH FOR THE TRANSACTION OUTPUT IN DATABASE.
        final AssetDTO asset1 = assetService.addAsset(AssetDTO.builder()
                .genesisPoint(BitcoinTransactionOutputDTO.builder()
                        .txId(BITCOIN_TRANSACTION_3_TXID)
                        .vout(0)
                        .build())
                .name("testCoin")
                .metaData("my meta data")
                .assetId("my asset id")
                .outputIndex(8)
                .build());

        // Testing asset value.
        assertNotNull(asset1.getId());
        assertNotNull(asset1.getGenesisPoint());
        assertNotNull(asset1.getGenesisPoint().getId());
        assertEquals(BITCOIN_TRANSACTION_3_TXID, asset1.getGenesisPoint().getTxId());
        assertEquals(0, asset1.getGenesisPoint().getVout());
        assertEquals("76a9140aa7e954ae2c972225309f0992e3ecd698a90f5f88ac", asset1.getGenesisPoint().getScriptPubKey());
        assertEquals("OP_DUP OP_HASH160 OP_PUSHBYTES_20 0aa7e954ae2c972225309f0992e3ecd698a90f5f OP_EQUALVERIFY OP_CHECKSIG", asset1.getGenesisPoint().getScriptPubKeyAsm());
        assertEquals("p2pkh", asset1.getGenesisPoint().getScriptPubKeyType());
        assertEquals("1yLucPwZwVuNxMFTXyyX5qTk3YFNpAGEz", asset1.getGenesisPoint().getScriptPubKeyAddress());
        assertEquals(0, new BigDecimal("2036308").compareTo(asset1.getGenesisPoint().getValue()));
        assertEquals(0, asset1.getCreator().getId());
        assertEquals("anonymous", asset1.getCreator().getUsername());
        assertEquals("testCoin", asset1.getName());
        assertEquals("my meta data", asset1.getMetaData());
        assertEquals("my asset id", asset1.getAssetId());
        assertEquals(8, asset1.getOutputIndex());

        // =============================================================================================================
        // Now adding a real asset with an existing transaction output.
        final AssetDTO asset2 = assetService.addAsset(AssetDTO.builder()
                .genesisPoint(bto.get())
                .name("testCoin2")
                .metaData("metaData2")
                .assetId("assetId2")
                .outputIndex(9)
                .build());

        // Testing asset value.
        assertNotNull(asset2.getId());
        assertEquals(1, asset2.getGenesisPoint().getId());
    }

    @Test
    @DisplayName("getAsset()")
    public void getAsset() {
        // Non-existing asset.
        Optional<AssetDTO> asset = assetService.getAsset(0);
        assertFalse(asset.isPresent());

        // Existing asset.
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertEquals(asset.get().getId(), 1);
        Assertions.assertEquals(asset.get().getGenesisPoint().getTxId(), "2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea");
        Assertions.assertEquals(asset.get().getGenesisPoint().getVout(), 0);
        assertEquals(asset.get().getName(), "starbackrcoin");
        assertEquals(asset.get().getMetaData(), "737461726261636b72206d6f6e6579");
        assertEquals(asset.get().getAssetId(), "b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e");
        assertEquals(asset.get().getOutputIndex(), 0);
    }

    @Test
    @DisplayName("getAssetByAssetId()")
    public void getAssetByAssetId() {
        // Non-existing asset.
        Optional<AssetDTO> asset = assetService.getAssetByAssetId("NON-EXISTING");
        assertFalse(asset.isPresent());

        // Existing asset.
        asset = assetService.getAssetByAssetId("b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e");
        assertTrue(asset.isPresent());
        assertEquals(1, asset.get().getId());
        Assertions.assertEquals("2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea", asset.get().getGenesisPoint().getTxId());
        Assertions.assertEquals(0, asset.get().getGenesisPoint().getVout());
        assertEquals("starbackrcoin", asset.get().getName());
        assertEquals("737461726261636b72206d6f6e6579", asset.get().getMetaData());
        assertEquals("b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e", asset.get().getAssetId());
        assertEquals(0, asset.get().getOutputIndex());
    }

}
