package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
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
                .assetGroupId("TWEAKED_GROUP_KEY_10000")
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
    @DisplayName("getAssetGroupByTweakedGroupKey()")
    public void getAssetGroupByRawGroupKey() {
        // =============================================================================================================
        // Non-existing asset group.
        Optional<AssetGroupDTO> assetGroup = assetGroupService.getAssetGroupByAssetGroupId("NON_EXISTING_ASSET_GROUP_ID");
        assertFalse(assetGroup.isPresent());

        // =============================================================================================================
        // Existing asset group on testnet and in our database initialization script.
        assetGroup = assetGroupService.getAssetGroupByAssetGroupId("TWEAKED_GROUP_KEY_10000");
        assertTrue(assetGroup.isPresent());
        assertEquals(10000, assetGroup.get().getId());
        assertEquals("TWEAKED_GROUP_KEY_10000", assetGroup.get().getAssetGroupId());
        assertEquals("RAW_GROUP_KEY_10000", assetGroup.get().getRawGroupKey());
        assertEquals("TWEAKED_GROUP_KEY_10000", assetGroup.get().getTweakedGroupKey());
        assertEquals("ASSET_WITNESS_10000", assetGroup.get().getAssetWitness());
    }

}
