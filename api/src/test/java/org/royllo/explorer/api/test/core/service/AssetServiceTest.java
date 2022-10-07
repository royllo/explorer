package org.royllo.explorer.api.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.dto.asset.AssetDTO;
import org.royllo.explorer.api.service.asset.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mempoolTransactionServiceMock")
@DisplayName("AssetService tests")
public class AssetServiceTest {

    @Autowired
    private AssetService assetService;

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
        assertEquals(asset.get().getGenesisPoint().getTxId(), "2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea");
        assertEquals(asset.get().getGenesisPoint().getVout(), 0);
        assertEquals(asset.get().getName(), "starbackrcoin");
        assertEquals(asset.get().getMeta(), "737461726261636b72206d6f6e6579");
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
        assertEquals("2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea", asset.get().getGenesisPoint().getTxId());
        assertEquals(0, asset.get().getGenesisPoint().getVout());
        assertEquals("starbackrcoin", asset.get().getName());
        assertEquals("737461726261636b72206d6f6e6579", asset.get().getMeta());
        assertEquals("b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e", asset.get().getAssetId());
        assertEquals(0, asset.get().getOutputIndex());
    }

}
