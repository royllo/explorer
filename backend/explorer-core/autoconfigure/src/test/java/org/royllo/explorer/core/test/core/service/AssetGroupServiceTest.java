package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
            fail("Should have thrown an exception");
        } catch (AssertionError e) {
            assertEquals("Asset group id must be null", e.getMessage());
        }

        // =============================================================================================================
        // Second constraint test - Asset group key id is required.
        try {
            assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                    .build());
            fail("Should have thrown an exception");
        } catch (AssertionError e) {
            assertEquals("Asset group key id is required", e.getMessage());
        }

        // =============================================================================================================
        // Third constraint test - Asset group key already registered.
        try {
            assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                    .rawGroupKey(ROYLLO_COIN_RAW_GROUP_KEY)
                    .build());
            fail("Should have thrown an exception");
        } catch (AssertionError e) {
            assertEquals("Asset group key already registered", e.getMessage());
        }

        // =============================================================================================================
        // Now creating a real asset group.
        AssetGroupDTO assetGroupDTO = assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                .assetIdSig("NEW-ASSET-ID-SIG")
                .rawGroupKey("NEW-ASSET-GROUP-KEY")
                .tweakedGroupKey("NEW-ASSET-GROUP-KEY-TWEAKED")
                .build());

        assertNotNull(assetGroupDTO);
        assertNotNull(assetGroupDTO.getId());
        assertEquals("NEW-ASSET-ID-SIG", assetGroupDTO.getAssetIdSig());
        assertEquals("NEW-ASSET-GROUP-KEY", assetGroupDTO.getRawGroupKey());
        assertEquals("NEW-ASSET-GROUP-KEY-TWEAKED", assetGroupDTO.getTweakedGroupKey());
    }

    @Test
    @DisplayName("getAssetGroupByRawGroupKey()")
    public void getAssetGroupByRawGroupKey() {
        // =============================================================================================================
        // Non-existing asset group.
        Optional<AssetGroupDTO> assetGroup = assetGroupService.getAssetGroupByRawGroupKey("NON-EXISTING");
        assertFalse(assetGroup.isPresent());

        // =============================================================================================================
        // Existing asset group on testnet and in our database initialization script ("roylloCoin") .
        assetGroup = assetGroupService.getAssetGroupByRawGroupKey(ROYLLO_COIN_RAW_GROUP_KEY);
        assertTrue(assetGroup.isPresent());
        assertEquals(1L, assetGroup.get().getId());
        assertEquals(ROYLLO_COIN_ASSET_ID_SIG, assetGroup.get().getAssetIdSig());
        assertEquals(ROYLLO_COIN_RAW_GROUP_KEY, assetGroup.get().getRawGroupKey());
        assertEquals(ROYLLO_COIN_TWEAKED_GROUP_KEY, assetGroup.get().getTweakedGroupKey());
    }

}
