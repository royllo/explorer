package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_DTO;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mempoolTransactionServiceMock")
@DisplayName("AssetStateService tests")
public class AssetStateServiceTest extends BaseTest {

    @Autowired
    private AssetStateService assetStateService;

    @Test
    @DisplayName("addAssetState()")
    public void addAssetState() {
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

    }

    @Test
    @DisplayName("getAssetStateByAnchorOutpoint()")
    public void getAssetStateByAnchorOutpoint() {
        // =============================================================================================================
        // Non-existing asset group.
        Optional<AssetStateDTO> assetState = assetStateService.getAssetStateByAnchorOutpoint("NON-EXISTING");
        assertFalse(assetState.isPresent());

        // =============================================================================================================
        // Existing asset state on testnet and in our database initialization script ("roylloCoin") .
        // assetState = assetStateService.getAssetStateByAnchorOutpoint(ROYLLO_COIN_ANCHOR_OUTPOINT);
        assertEquals(ROYLLO_COIN_STATE_ID, assetState.get().getId());
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
        assertEquals(ROYLLO_COIN_ANCHOR_OUTPOINT, assetState.get().getAnchorOutpoint().getTxId() + "/" + assetState.get().getAnchorOutpoint().getVout());
        assertEquals(ROYLLO_COIN_ANCHOR_TX, assetState.get().getAnchorTx());
        assertEquals(ROYLLO_COIN_ANCHOR_TX_ID, assetState.get().getAnchorTxId());
        assertEquals(UNKNOWN_ROYLLO_COIN_ANCHOR_INTERNAL_KEY, assetState.get().getInternalKey());
        assertEquals(UNKNOWN_ROYLLO_COIN_TX_MERKLE_PROOF, assetState.get().getMerkleRoot());
        assertEquals(ROYLLO_COIN_TAPSCRIPT_SIBLING, assetState.get().getTapscriptSibling());
        assertEquals(ROYLLO_COIN_SCRIPT_VERSION, assetState.get().getScriptVersion());
        assertEquals(ROYLLO_COIN_SCRIPT_KEY, assetState.get().getScriptKey());
        assertEquals(ROYLLO_COIN_SCRIPT_VERSION, assetState.get().getScriptVersion());
    }

}
