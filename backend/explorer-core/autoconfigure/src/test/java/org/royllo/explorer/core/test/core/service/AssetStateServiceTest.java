package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.royllo.explorer.core.service.asset.AssetStateServiceImplementation.SEARCH_PARAMETER_ASSET_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_DTO;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_ID;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mempoolTransactionServiceMock")
@DisplayName("AssetStateService tests")
public class AssetStateServiceTest extends BaseTest {

    @Autowired
    private AssetGroupRepository assetGroupRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetStateRepository assetStateRepository;

    @Autowired
    private BitcoinService bitcoinService;

    @Autowired
    private AssetStateService assetStateService;

    @Test
    @DisplayName("queryAssetStates()")
    public void queryAssetStates() {
        // Search for asset states without specifying the SEARCH_PARAMETER_ASSET_ID parameter.
        try {
            assetStateService.queryAssetStates("dezded", 1, 5);
            fail("An exception should have been raised");
        } catch (AssertionError e) {
            assertEquals("Only assetId:value query is supported", e.getMessage());
        }

        // Searching for asset states for an asset id that doesn't exist.
        Page<AssetStateDTO> results = assetStateService.queryAssetStates(SEARCH_PARAMETER_ASSET_ID + "NON-EXISTING", 1, 5);
        assertEquals(0, results.getTotalElements());
        assertEquals(0, results.getTotalPages());

        // Searching for the asset states of an existing asset.
        results = assetStateService.queryAssetStates(SEARCH_PARAMETER_ASSET_ID + "asset_id_9", 1, 5);
        assertEquals(3, results.getTotalElements());
        assertEquals(5, results.getSize());
        assertEquals(1, results.getTotalPages());
    }

    @Test
    @DisplayName("addAssetState()")
    public void addAssetState() {
        // We retrieve a bitcoin transaction output from database for our test.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_1_TXID, 0);
        assertTrue(bto.isPresent());

        // =============================================================================================================
        // First constraint test - Trying to save an asset state with an ID.
        try {
            assetStateService.addAssetState(AssetStateDTO.builder()
                    .id(1L)
                    .build());
            fail("Should have thrown an exception");
        } catch (AssertionError e) {
            assertEquals("Asset state already exists", e.getMessage());
        }

        // =============================================================================================================
        // Second constraint test - Trying to save an asset state without an asset.
        try {
            assetStateService.addAssetState(AssetStateDTO.builder()
                    .build());
            fail("Should have thrown an exception");
        } catch (AssertionError e) {
            assertEquals("Linked asset is required", e.getMessage());
        }

        // =============================================================================================================
        // Third constraint test - Asset id is required.
        try {
            assetStateService.addAssetState(AssetStateDTO.builder()
                    .asset(AssetDTO.builder().build())
                    .build());
            fail("Should have thrown an exception");
        } catch (AssertionError e) {
            assertEquals("Asset id is required", e.getMessage());
        }

        // =============================================================================================================
        // Fourth constraint test - Asset id is required.
        try {
            assetStateService.addAssetState(AssetStateDTO.builder()
                    .asset(AssetDTO.builder()
                            .assetId("TEST")
                            .build())
                    .build());
            fail("Should have thrown an exception");
        } catch (AssertionError e) {
            assertEquals("Bitcoin transaction is required", e.getMessage());
        }

        // =============================================================================================================
        // We create an asset state from scratch (The asset doesn't exist in database).
        int assetGroupCount = assetGroupRepository.findAll().size();
        int assetCount = assetRepository.findAll().size();
        int assetStateCount = assetStateRepository.findAll().size();

        final AssetStateDTO firstAssetStateCreated = assetStateService.addAssetState(AssetStateDTO.builder()
                .creator(ANONYMOUS_USER_DTO)
                .asset(AssetDTO.builder()
                        .assetId("TEST_COIN_ASSET_ID")
                        .genesisPoint(bto.get())
                        .build())
                .anchorBlockHash("TEST_ANCHOR_BLOCK_HASH")
                .anchorOutpoint(bto.get())
                .anchorTx("TEST_ANCHOR_TX")
                .anchorTxId("TEST_ANCHOR_TX_ID")
                .internalKey("TEST_INTERNAL_KEY")
                .merkleRoot("TEST_MERKLE_ROOT")
                .tapscriptSibling("TEST_TAPSCRIPT_SIBLING")
                .scriptVersion(0)
                .scriptKey("TEST_SCRIPT_KEY")
                .build());

        // We check what has been created.
        assertEquals(assetGroupCount, assetGroupRepository.findAll().size());
        assertEquals(assetCount + 1, assetRepository.findAll().size());
        assertEquals(assetStateCount + 1, assetStateRepository.findAll().size());

        // We check what was created.
        assertNotNull(firstAssetStateCreated.getId());
        // Asset state id is calculated from the asset state data.
        // TEST_COIN_ASSET_ID_2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea:0_TEST_SCRIPT_KEY
        assertEquals("83347bed4fa20045ab67de4d9fb0da1f73df6cc5ebdb8e44dcb51a0c5e2dd75a", firstAssetStateCreated.getAssetStateId());
        // User.
        assertNotNull(firstAssetStateCreated.getCreator());
        assertEquals(ANONYMOUS_USER_ID, firstAssetStateCreated.getCreator().getUserId());
        // Asset.
        assertNotNull(firstAssetStateCreated.getAsset());
        assertNotNull(firstAssetStateCreated.getAsset().getId());
        assertEquals("TEST_COIN_ASSET_ID", firstAssetStateCreated.getAsset().getAssetId());
        // Asset group.
        assertNull(firstAssetStateCreated.getAsset().getAssetGroup());
        // Asset state data.
        assertEquals("TEST_ANCHOR_BLOCK_HASH", firstAssetStateCreated.getAnchorBlockHash());
        assertEquals("2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea", firstAssetStateCreated.getAnchorOutpoint().getTxId());
        assertEquals(0, firstAssetStateCreated.getAnchorOutpoint().getVout());
        assertEquals("TEST_ANCHOR_TX", firstAssetStateCreated.getAnchorTx());
        assertEquals("TEST_ANCHOR_TX_ID", firstAssetStateCreated.getAnchorTxId());
        assertEquals("TEST_INTERNAL_KEY", firstAssetStateCreated.getInternalKey());
        assertEquals("TEST_MERKLE_ROOT", firstAssetStateCreated.getMerkleRoot());
        assertEquals("TEST_TAPSCRIPT_SIBLING", firstAssetStateCreated.getTapscriptSibling());
        assertEquals(0, firstAssetStateCreated.getScriptVersion());
        assertEquals("TEST_SCRIPT_KEY", firstAssetStateCreated.getScriptKey());

        // Test if the asset exists in database.
        final Optional<AssetDTO> assetCreated = assetService.getAssetByAssetId("TEST_COIN_ASSET_ID");
        assertTrue(assetCreated.isPresent());
        assertNotNull(assetCreated.get().getId());

        // =============================================================================================================
        // We try to create the same asset state again.
        try {
            assetStateService.addAssetState(AssetStateDTO.builder()
                    .creator(ANONYMOUS_USER_DTO)
                    .asset(AssetDTO.builder()
                            .assetId("TEST_COIN_ASSET_ID")
                            .genesisPoint(bto.get())
                            .build())
                    .anchorBlockHash("TEST_ANCHOR_BLOCK_HASH")
                    .anchorOutpoint(bto.get())
                    .anchorTx("TEST_ANCHOR_TX")
                    .anchorTxId("TEST_ANCHOR_TX_ID")
                    .internalKey("TEST_INTERNAL_KEY")
                    .merkleRoot("TEST_MERKLE_ROOT")
                    .tapscriptSibling("TEST_TAPSCRIPT_SIBLING")
                    .scriptVersion(0)
                    .scriptKey("TEST_SCRIPT_KEY")
                    .build());
            fail("Should have thrown an exception");
        } catch (AssertionError e) {
            assertEquals("Asset state already exists", e.getMessage());
        }

        // We check that nothing has been created.
        assertEquals(assetGroupCount, assetGroupRepository.findAll().size());
        assertEquals(assetCount + 1, assetRepository.findAll().size());
        assertEquals(assetStateCount + 1, assetStateRepository.findAll().size());

        // =============================================================================================================
        // We create a second asset state from scratch (on the asset we created previously.
        assetStateService.addAssetState(AssetStateDTO.builder()
                .creator(ANONYMOUS_USER_DTO)
                .asset(AssetDTO.builder()
                        .assetId("TEST_COIN_ASSET_ID")
                        .genesisPoint(bto.get())
                        .build())
                .anchorBlockHash("TEST_ANCHOR_BLOCK_HASH_2")
                .anchorOutpoint(bto.get())
                .anchorTx("TEST_ANCHOR_TX_2")
                .anchorTxId("TEST_ANCHOR_TX_ID_2")
                .internalKey("TEST_INTERNAL_KEY_2")
                .merkleRoot("TEST_MERKLE_ROOT_2")
                .tapscriptSibling("TEST_TAPSCRIPT_SIBLING_2")
                .scriptVersion(1)
                .scriptKey("TEST_SCRIPT_KEY_2")
                .build());

        // We check what has been created.
        assertEquals(assetGroupCount, assetGroupRepository.findAll().size());
        assertEquals(assetCount + 1, assetRepository.findAll().size());
        assertEquals(assetStateCount + 2, assetStateRepository.findAll().size());

        // We check what was created (this time we used the getByAssetId() method).
        final Optional<AssetStateDTO> secondAssetStateCreated = assetStateService.getAssetStateByAssetStateId("fcc1576228519673fbf7ae22f6447cab5d6b5aa6747818d1df4b5f0dc83aac15");
        assertTrue(secondAssetStateCreated.isPresent());
        assertNotNull(secondAssetStateCreated.get().getId());
        // Asset state id is calculated from the asset state data.
        // TEST_COIN_ASSET_ID_2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea:0_TEST_SCRIPT_KEY_2
        assertEquals("fcc1576228519673fbf7ae22f6447cab5d6b5aa6747818d1df4b5f0dc83aac15", secondAssetStateCreated.get().getAssetStateId());
        // User.
        assertNotNull(secondAssetStateCreated.get().getCreator());
        assertEquals(ANONYMOUS_USER_ID, secondAssetStateCreated.get().getCreator().getUserId());
        // Asset.
        assertNotNull(secondAssetStateCreated.get().getAsset());
        assertNotNull(secondAssetStateCreated.get().getAsset().getId());
        assertEquals("TEST_COIN_ASSET_ID", secondAssetStateCreated.get().getAsset().getAssetId());
        // Asset group.
        assertNull(secondAssetStateCreated.get().getAsset().getAssetGroup());
        // Asset state data.
        assertEquals("TEST_ANCHOR_BLOCK_HASH_2", secondAssetStateCreated.get().getAnchorBlockHash());
        assertEquals("2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea", secondAssetStateCreated.get().getAnchorOutpoint().getTxId());
        assertEquals(0, secondAssetStateCreated.get().getAnchorOutpoint().getVout());
        assertEquals("TEST_ANCHOR_TX_2", secondAssetStateCreated.get().getAnchorTx());
        assertEquals("TEST_ANCHOR_TX_ID_2", secondAssetStateCreated.get().getAnchorTxId());
        assertEquals("TEST_INTERNAL_KEY_2", secondAssetStateCreated.get().getInternalKey());
        assertEquals("TEST_MERKLE_ROOT_2", secondAssetStateCreated.get().getMerkleRoot());
        assertEquals("TEST_TAPSCRIPT_SIBLING_2", secondAssetStateCreated.get().getTapscriptSibling());
        assertEquals(1, secondAssetStateCreated.get().getScriptVersion());
        assertEquals("TEST_SCRIPT_KEY_2", secondAssetStateCreated.get().getScriptKey());

    }

    @Test
    @DisplayName("getAssetStateByAssetStateId()")
    public void getAssetStateByAssetStateId() {
        // =============================================================================================================
        // Non-existing asset group.
        Optional<AssetStateDTO> assetState = assetStateService.getAssetStateByAssetStateId("NON-EXISTING");
        assertFalse(assetState.isPresent());

        // =============================================================================================================
        // Existing asset state on testnet and in our database initialization script ("roylloCoin") .
        assetState = assetStateService.getAssetStateByAssetStateId(ROYLLO_COIN_ASSET_STATE_ID);
        assertTrue(assetState.isPresent());
        assertEquals(ROYLLO_COIN_STATE_ID, assetState.get().getId());
        assertEquals(ROYLLO_COIN_ASSET_STATE_ID, assetState.get().getAssetStateId());
        // User.
        assertNotNull(assetState.get().getCreator());
        assertEquals(ANONYMOUS_USER_DTO.getId(), assetState.get().getCreator().getId());
        // Asset.
        assertNotNull(assetState.get().getAsset());
        assertEquals(ROYLLO_COIN_ID, assetState.get().getAsset().getId());
        assertEquals(ROYLLO_COIN_ASSET_ID, assetState.get().getAsset().getAssetId());
        // Asset group.
        assertNotNull(assetState.get().getAsset().getAssetGroup());
        assertEquals(ROYLLO_COIN_GROUP_ID, assetState.get().getAsset().getAssetGroup().getId());
        assertEquals(ROYLLO_COIN_RAW_GROUP_KEY, assetState.get().getAsset().getAssetGroup().getRawGroupKey());
        // Asset state data.
        assertEquals(ROYLLO_COIN_ANCHOR_BLOCK_HASH, assetState.get().getAnchorBlockHash());
        assertEquals(ROYLLO_COIN_ANCHOR_OUTPOINT, assetState.get().getAnchorOutpoint().getTxId() + ":" + assetState.get().getAnchorOutpoint().getVout());
        assertEquals(ROYLLO_COIN_ANCHOR_TX, assetState.get().getAnchorTx());
        assertEquals(ROYLLO_COIN_ANCHOR_TX_ID, assetState.get().getAnchorTxId());
        assertEquals(ROYLLO_COIN_INTERNAL_KEY, assetState.get().getInternalKey());
        assertEquals(ROYLLO_COIN_MERKLE_ROOT, assetState.get().getMerkleRoot());
        assertEquals(ROYLLO_COIN_TAPSCRIPT_SIBLING, assetState.get().getTapscriptSibling());
        assertEquals(ROYLLO_COIN_SCRIPT_VERSION, assetState.get().getScriptVersion());
        assertEquals(ROYLLO_COIN_SCRIPT_KEY, assetState.get().getScriptKey());
        assertEquals(ROYLLO_COIN_SCRIPT_VERSION, assetState.get().getScriptVersion());
    }

}
