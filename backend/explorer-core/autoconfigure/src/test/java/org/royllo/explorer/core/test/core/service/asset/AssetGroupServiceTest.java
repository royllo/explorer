package org.royllo.explorer.core.test.core.service.asset;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.royllo.test.tapd.asset.DecodedProofValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_DTO;
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
        assertThrows(IllegalArgumentException.class, () -> assetGroupService.addAssetGroup(null));

        // Asset group parameters are invalid.
        ConstraintViolationException violations = assertThrows(ConstraintViolationException.class, () -> {
            assetGroupService.addAssetGroup(AssetGroupDTO.builder().build());
        });
        assertEquals(2, violations.getConstraintViolations().size());
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("assetGroupId")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("tweakedGroupKey")));

        // Asset group key already registered.
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> assetGroupService.addAssetGroup(
                AssetGroupDTO.builder()
                        .creator(ANONYMOUS_USER_DTO)
                        .assetGroupId(UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey())
                        .tweakedGroupKey("TWEAKED_GROUP_KEY_10000")
                        .build()));
        assertEquals("Asset group id already registered", e.getMessage());

        // =============================================================================================================
        // Normal behavior tests.

        // Now creating a real asset group.
        AssetGroupDTO assetGroupDTO = assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                .id(1L)
                .creator(ANONYMOUS_USER_DTO)
                .assetGroupId("NEW_ASSET_GROUP_KEY_TWEAKED")
                .rawGroupKey("NEW_ASSET_GROUP_KEY")
                .tweakedGroupKey("NEW_ASSET_GROUP_KEY_TWEAKED")
                .assetWitness("NEW_ASSET_WITNESS")
                .build());
        assertNotNull(assetGroupDTO);
        assertNotNull(assetGroupDTO.getId());
        assertNotEquals(1L, assetGroupDTO.getId());
        assertEquals("NEW_ASSET_GROUP_KEY_TWEAKED", assetGroupDTO.getAssetGroupId());
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
        DecodedProofValueResponse.DecodedProof.Asset.AssetGroup unlimitedAssetgroup = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup();
        assetGroup = assetGroupService.getAssetGroupByAssetGroupId(unlimitedAssetgroup.getTweakedGroupKey());
        assertTrue(assetGroup.isPresent());
        assertNotNull(assetGroup.get().getId());
        assertEquals(unlimitedAssetgroup.getTweakedGroupKey(), assetGroup.get().getAssetGroupId());
        assertEquals(unlimitedAssetgroup.getRawGroupKey(), assetGroup.get().getRawGroupKey());
        assertEquals(unlimitedAssetgroup.getTweakedGroupKey(), assetGroup.get().getTweakedGroupKey());
        assertEquals(unlimitedAssetgroup.getAssetWitness(), assetGroup.get().getAssetWitness());
    }

}
