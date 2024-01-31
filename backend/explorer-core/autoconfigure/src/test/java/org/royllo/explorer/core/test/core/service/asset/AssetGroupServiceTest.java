package org.royllo.explorer.core.test.core.service.asset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("AssetGroupService tests")
public class AssetGroupServiceTest extends TestWithMockServers {

    @Autowired
    private AssetGroupService assetGroupService;

    @Test
    @DisplayName("addAssetGroup()")
    public void addAssetGroup() {
        // =============================================================================================================
        // Constraint test - Trying to save an asset group with an ID.
        AssertionError e = assertThrows(AssertionError.class, () -> assetGroupService.addAssetGroup(AssetGroupDTO.builder().id(1L).build()));
        assertEquals("Asset group id must be null", e.getMessage());

        // =============================================================================================================
        // Constraint test - Asset group key id is required.
        e = assertThrows(AssertionError.class, () -> assetGroupService.addAssetGroup(AssetGroupDTO.builder().build()));
        assertEquals("Asset group id is required", e.getMessage());

        // =============================================================================================================
        // Constraint test - Asset group key id is required.
        e = assertThrows(AssertionError.class, () -> assetGroupService.addAssetGroup(AssetGroupDTO.builder().assetGroupId("0").build()));
        assertEquals("Tweaked Asset group key is required", e.getMessage());

        // =============================================================================================================
        // Constraint test - Tweaked asset group key id is required.
        e = assertThrows(AssertionError.class, () -> assetGroupService.addAssetGroup(AssetGroupDTO.builder().build()));
        assertEquals("Asset group id is required", e.getMessage());

        // =============================================================================================================
        // Constraint test - Asset group key already registered.
        e = assertThrows(AssertionError.class, () -> assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                .assetGroupId(UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey())
                .tweakedGroupKey("TWEAKED_GROUP_KEY_10000").build()));
        assertEquals("Asset group id already registered", e.getMessage());

        // =============================================================================================================
        // Now creating a real asset group.
        AssetGroupDTO assetGroupDTO = assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                .assetGroupId("NEW_ASSET_GROUP_KEY_TWEAKED")
                .rawGroupKey("NEW_ASSET_GROUP_KEY")
                .tweakedGroupKey("NEW_ASSET_GROUP_KEY_TWEAKED")
                .assetWitness("NEW_ASSET_WITNESS")
                .build());

        assertNotNull(assetGroupDTO);
        assertNotNull(assetGroupDTO.getId());
        assertEquals("NEW_ASSET_GROUP_KEY", assetGroupDTO.getRawGroupKey());
        assertEquals("NEW_ASSET_GROUP_KEY_TWEAKED", assetGroupDTO.getTweakedGroupKey());
        assertEquals("NEW_ASSET_WITNESS", assetGroupDTO.getAssetWitness());
    }

    @Test
    @DisplayName("getAssetGroupByAssetGroupId()")
    public void getAssetGroupByAssetGroupId() {
        // =============================================================================================================
        // Non-existing asset group.
        Optional<AssetGroupDTO> assetGroup = assetGroupService.getAssetGroupByAssetGroupId("NON_EXISTING_ASSET_GROUP_ID");
        assertFalse(assetGroup.isPresent());

        // =============================================================================================================
        // Existing asset group on testnet and in our database initialization script.
        assetGroup = assetGroupService.getAssetGroupByAssetGroupId(UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey());
        assertTrue(assetGroup.isPresent());
        assertNotNull(assetGroup.get().getId());
        assertEquals(UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey(), assetGroup.get().getAssetGroupId());
        assertEquals(UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getRawGroupKey(), assetGroup.get().getRawGroupKey());
        assertEquals(UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey(), assetGroup.get().getTweakedGroupKey());
        assertEquals(UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getAssetWitness(), assetGroup.get().getAssetWitness());
    }

}
