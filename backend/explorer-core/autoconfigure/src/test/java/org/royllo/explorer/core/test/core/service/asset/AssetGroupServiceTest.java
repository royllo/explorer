package org.royllo.explorer.core.test.core.service.asset;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        // Constraint tests.

        // Asset group parameter is null.
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> assetGroupService.addAssetGroup(null))
                .withMessage("newAssetGroup is marked non-null but is null");

        // Asset group parameters are invalid.
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> assetGroupService.addAssetGroup(AssetGroupDTO.builder().build()))
                .satisfies(violations -> {
                    assertEquals(2, violations.getConstraintViolations().size());
                    assertTrue(isPropertyInConstraintViolations(violations, "assetGroupId"));
                    assertTrue(isPropertyInConstraintViolations(violations, "tweakedGroupKey"));
                });

        // Asset group key already registered.
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                        .assetGroupId(UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey())
                        .tweakedGroupKey("TWEAKED_GROUP_KEY_10000")
                        .build()))
                .withMessage("Asset group id already registered");

        // =============================================================================================================
        // Normal behavior tests.

        // Now creating a real asset group.
        assertThat(assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                .id(1L)
                .assetGroupId("NEW_ASSET_GROUP_KEY_TWEAKED")
                .rawGroupKey("NEW_ASSET_GROUP_KEY")
                .tweakedGroupKey("NEW_ASSET_GROUP_KEY_TWEAKED")
                .assetWitness("NEW_ASSET_WITNESS")
                .build()))
                .isNotNull()
                .satisfies(assetGroupDTO -> {
                    assertNotNull(assetGroupDTO.getId());
                    assertNotEquals(1L, assetGroupDTO.getId());
                    assertEquals("NEW_ASSET_GROUP_KEY_TWEAKED", assetGroupDTO.getAssetGroupId());
                    assertEquals("NEW_ASSET_GROUP_KEY", assetGroupDTO.getRawGroupKey());
                    assertEquals("NEW_ASSET_GROUP_KEY_TWEAKED", assetGroupDTO.getTweakedGroupKey());
                    assertEquals("NEW_ASSET_WITNESS", assetGroupDTO.getAssetWitness());
                });
    }

    @Test
    @DisplayName("getAssetGroupByAssetGroupId()")
    public void getAssetGroupByAssetGroupId() {
        // =============================================================================================================
        // Non-existing asset group.
        assertFalse(assetGroupService.getAssetGroupByAssetGroupId("NON_EXISTING_ASSET_GROUP_ID").isPresent());

        // =============================================================================================================
        // Existing asset group on testnet and in our database initialization script.
        var unlimitedAssetGroupFromTapd = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup();
        assertThat(assetGroupService.getAssetGroupByAssetGroupId(unlimitedAssetGroupFromTapd.getTweakedGroupKey()))
                .isPresent()
                .hasValueSatisfying(assetGroupDTO -> {
                    assertNotNull(assetGroupDTO.getId());
                    assertEquals(unlimitedAssetGroupFromTapd.getTweakedGroupKey(), assetGroupDTO.getAssetGroupId());
                    assertEquals(unlimitedAssetGroupFromTapd.getRawGroupKey(), assetGroupDTO.getRawGroupKey());
                    assertEquals(unlimitedAssetGroupFromTapd.getTweakedGroupKey(), assetGroupDTO.getTweakedGroupKey());
                    assertEquals(unlimitedAssetGroupFromTapd.getAssetWitness(), assetGroupDTO.getAssetWitness());
                });
    }

}
