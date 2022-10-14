package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mempoolTransactionServiceMock")
@DisplayName("AssetService tests")
public class AssetServiceTest extends BaseTest {

    @Autowired
    private AssetService assetService;

    @Test
    @DisplayName("queryAssets()")
    public void queryAssets() {
        // Test on two coins in database : "royllostar" and "starbackrcoin"

        // Searching for an asset that doesn't exist.
        List<AssetDTO> results = assetService.queryAssets("NON-EXISTING");
        assertEquals(0, results.size());

        // Searching for an asset with its asset id.
        results = assetService.queryAssets(ASSET_ID_NUMBER_01);
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getId());

        // Searching for an asset with its partial name - only 1 result.
        results = assetService.queryAssets("back");
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getId());

        // Searching for an asset with its partial name uppercase - only 1 result.
        results = assetService.queryAssets("BACK");
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getId());

        // Searching for an asset with its partial name uppercase - only 1 result.
        results = assetService.queryAssets("ROYLLO");
        assertEquals(1, results.size());
        assertEquals(2, results.get(0).getId());

        // Searching for an asset with its partial name corresponding to two assets.
        results = assetService.queryAssets("star");
        assertEquals(2, results.size());
        Set<Long> ids = results.stream()
                .map(AssetDTO::getId)
                .collect(Collectors.toSet());
        assertTrue(ids.contains(1L));
        assertTrue(ids.contains(2L));
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
