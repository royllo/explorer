package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedHashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_USERNAME;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mempoolTransactionServiceMock")
@DisplayName("AssetGroupService tests")
public class AssetGroupServiceTest extends BaseTest {

    @Autowired
    private AssetGroupService assetGroupService;

    @Test
    @DisplayName("addAssetGroup()")
    public void addAssetGroup() {
        // =============================================================================================================
        // First constraint test - Trying to save an asset group with an ID.
        try {
            assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                    .id(1L)
                    .build());
        } catch (AssertionError e) {
            assertEquals("Asset group id must be null", e.getMessage());
        }

        // =============================================================================================================
        // Second constraint test - Asset group key id is required.
        try {
            assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                    .build());
        } catch (AssertionError e) {
            assertEquals("Asset group key id is required", e.getMessage());
        }

        // =============================================================================================================
        // Third constraint test - Asset group key already registered.
        try {
            assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                    .rawGroupKey(ROYLLO_COIN_RAW_GROUP_KEY)
                    .build());
        } catch (AssertionError e) {
            assertEquals("Asset group key already registered", e.getMessage());
        }

        // =============================================================================================================
        // Now creating a real asset group.
        AssetGroupDTO assetGroupDTO = assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                .assetIdSig("NEW-ASSET-ID-SIG")
                .rawGroupKey("NEW-ASSET-GROUP-KEY")
                .tweakedGroupKey("NEW-ASSET-GROUP-KEY-TWEAKED")
                // TODO I should now have to manually instantiate the Set. Maybe use @Singular ?
                .assets(new LinkedHashSet<>())
                .build());

        assertNotNull(assetGroupDTO);
        assertNotNull(assetGroupDTO.getId());
        assertEquals("NEW-ASSET-ID-SIG", assetGroupDTO.getAssetIdSig());
        assertEquals("NEW-ASSET-GROUP-KEY", assetGroupDTO.getRawGroupKey());
        assertEquals("NEW-ASSET-GROUP-KEY-TWEAKED", assetGroupDTO.getTweakedGroupKey());
        assertEquals(0, assetGroupDTO.getAssets().size());
    }

    @Test
    @DisplayName("getAssetGroupByRawGroupKey()")
    public void getAssetGroupByRawGroupKey() {
        // =============================================================================================================
        // Non-existing asset.
        Optional<AssetGroupDTO> assetGroupDTO = assetGroupService.getAssetGroupByRawGroupKey("NON-EXISTING");
        assertFalse(assetGroupDTO.isPresent());

        // =============================================================================================================
        // Existing asset group on testnet and in our database initialization script ("roylloCoin") .
        assetGroupDTO = assetGroupService.getAssetGroupByRawGroupKey(ROYLLO_COIN_RAW_GROUP_KEY);
        assertTrue(assetGroupDTO.isPresent());
        assertEquals(1L, assetGroupDTO.get().getId());
        assertEquals(ROYLLO_COIN_ASSET_ID_SIG, assetGroupDTO.get().getAssetIdSig());
        assertEquals(ROYLLO_COIN_RAW_GROUP_KEY, assetGroupDTO.get().getRawGroupKey());
        assertEquals(ROYLLO_COIN_TWEAKED_GROUP_KEY, assetGroupDTO.get().getTweakedGroupKey());
        assertEquals(1, assetGroupDTO.get().getAssets().size());

        // =============================================================================================================
        // Testing the associated asset.
        AssetDTO assetDTO = assetGroupDTO.get().getAssets().iterator().next();
        assertNotNull(assetDTO);
        assertEquals(1L, assetDTO.getId());

        // User.
        assertNotNull(assetDTO.getCreator());
        assertEquals(ANONYMOUS_USER_ID, assetDTO.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, assetDTO.getCreator().getUsername());

        // Asset group.
        assertNotNull(assetDTO.getAssetGroup());
        assertEquals(1L, assetDTO.getAssetGroup().getId());
        assertEquals(ROYLLO_COIN_ASSET_ID_SIG, assetDTO.getAssetGroup().getAssetIdSig());
        assertEquals(ROYLLO_COIN_RAW_GROUP_KEY, assetDTO.getAssetGroup().getRawGroupKey());
        assertEquals(ROYLLO_COIN_TWEAKED_GROUP_KEY, assetDTO.getAssetGroup().getTweakedGroupKey());

        // Asset fields values.
        assertEquals(ROYLLO_COIN_ASSET_ID, assetDTO.getAssetId());
        assertEquals(ROYLLO_COIN_GENESIS_POINT_TXID, assetDTO.getGenesisPoint().getTxId());
        assertEquals(ROYLLO_COIN_GENESIS_POINT_VOUT, assetDTO.getGenesisPoint().getVout());
        assertEquals(ROYLLO_COIN_META_DATA_HASH, assetDTO.getMetaDataHash());
        assertEquals(ROYLLO_COIN_NAME, assetDTO.getName());
        assertEquals(ROYLLO_COIN_OUTPUT_INDEX, assetDTO.getOutputIndex());
        assertEquals(ROYLLO_COIN_VERSION, assetDTO.getVersion());
        assertEquals(ROYLLO_COIN_TYPE, assetDTO.getType());
        assertEquals(0, assetDTO.getAmount().compareTo(ROYLLO_COIN_AMOUNT));
    }

}
