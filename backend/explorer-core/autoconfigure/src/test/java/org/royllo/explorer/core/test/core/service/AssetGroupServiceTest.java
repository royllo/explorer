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
    @DisplayName("getAssetGroupByRawGroupKey()")
    public void getAssetGroupByRawGroupKey() {
        // Non-existing asset.
        Optional<AssetGroupDTO> assetGroupDTO = assetGroupService.getAssetGroupByRawGroupKey("NON-EXISTING");
        assertFalse(assetGroupDTO.isPresent());

        // Existing asset group on testnet and in our database initialization script ("roylloCoin") .
        assetGroupDTO = assetGroupService.getAssetGroupByRawGroupKey(ROYLLO_COIN_RAW_GROUP_KEY);
        assertTrue(assetGroupDTO.isPresent());
        assertEquals(1L, assetGroupDTO.get().getId());
        assertEquals(ROYLLO_COIN_RAW_GROUP_KEY, assetGroupDTO.get().getRawGroupKey());
        assertEquals(ROYLLO_COIN_TWEAKED_GROUP_KEY, assetGroupDTO.get().getTweakedGroupKey());
        assertEquals(ROYLLO_COIN_ASSET_ID_SIG, assetGroupDTO.get().getAssetIdSig());
        assertEquals(1, assetGroupDTO.get().getAssets().size());

        // Testing the associated asset.
        AssetDTO assetDTO = assetGroupDTO.get().getAssets().iterator().next();
        assertNotNull(assetDTO);
        assertEquals(1L, assetDTO.getId());
        assertNotNull(assetDTO.getCreator());
        assertEquals(ANONYMOUS_USER_ID, assetDTO.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, assetDTO.getCreator().getUsername());
        assertNotNull(assetDTO.getAssetGroup());
        assertEquals(ROYLLO_COIN_ASSET_ID, assetDTO.getAssetId());
        //assertEquals(ROYLLO_COIN_ASSET_NAME, assetDTO.getAssetName());
    }

}
