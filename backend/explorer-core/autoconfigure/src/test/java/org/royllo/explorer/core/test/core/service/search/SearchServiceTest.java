package org.royllo.explorer.core.test.core.service.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.service.search.SQLSearchServiceImplementation;
import org.royllo.explorer.core.service.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_FROM_TEST;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS;

@SpringBootTest
@DirtiesContext
@DisplayName("SearchService tests")
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Test
    @DisplayName("queryAssets()")
    public void queryAssets() {
        // As we are running our tests with HSQLDB, we should check if we are not using PostgreSQL.
        assertFalse(((SQLSearchServiceImplementation) searchService).isUsingPostgreSQL());

        // Searching for a null value.
        Page<AssetDTO> results = searchService.queryAssets(null, 1, 5);
        assertEquals(0, results.getTotalElements());
        assertEquals(1, results.getTotalPages());

        // Searching for an asset that doesn't exist.
        results = searchService.queryAssets("NON_EXISTING_ASSET_ID", 1, 5);
        assertEquals(0, results.getTotalElements());
        assertEquals(0, results.getTotalPages());

        // Searching for an asset with its group asset id
        results = searchService.queryAssets(SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey(), 1, 5);
        assertEquals(3, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertThat(results.getContent())
                .extracting(AssetDTO::getAssetId)
                .containsExactlyInAnyOrder(SET_OF_ROYLLO_NFT_1_ASSET_ID,
                        SET_OF_ROYLLO_NFT_2_ASSET_ID,
                        SET_OF_ROYLLO_NFT_3_ASSET_ID);

        // Searching for an asset with its asset id.
        results = searchService.queryAssets(ROYLLO_COIN_ASSET_ID, 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertThat(results.getContent())
                .hasSize(1)
                .extracting(AssetDTO::getAssetId)
                .containsExactlyInAnyOrder(ROYLLO_COIN_ASSET_ID);

        // Searching for an asset with its asset id alias.
        results = searchService.queryAssets(TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS, 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertThat(results.getContent())
                .extracting(AssetDTO::getAssetId)
                .containsExactlyInAnyOrder(TRICKY_ROYLLO_COIN_ASSET_ID);

        // Searching for an asset with its partial name (trickyCoin) - only 1 result.
        results = searchService.queryAssets("ky", 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertThat(results.getContent())
                .hasSize(1)
                .extracting(AssetDTO::getAssetId)
                .containsExactlyInAnyOrder(TRICKY_ROYLLO_COIN_ASSET_ID);

        // Searching for an asset with its partial name uppercase (trickyRoylloCoin) - only 1 result.
        results = searchService.queryAssets("kyR", 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertThat(results.getContent())
                .hasSize(1)
                .extracting(AssetDTO::getAssetId)
                .containsExactlyInAnyOrder(TRICKY_ROYLLO_COIN_ASSET_ID);

        // Searching for an asset with its partial name corresponding to eight assets.
        // If addProof() is called before this test, then we should have one more assets in our database, so 9.
        results = searchService.queryAssets("royllo", 1, 5);
        assertTrue(results.getTotalElements() > 7);
        assertEquals(2, results.getTotalPages());
        assertThat(results.getContent())
                .extracting(AssetDTO::getId)
                .containsExactlyInAnyOrder(1L, 2L, 3L, 4L, 5L);

        // We are searching for a name but the size of our search is the same size as asset id alias.
        results = searchService.queryAssets("trickyRo", 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1L, results.getTotalPages());

        // We are searching for a name, but we ask for a page that doesn't exist.
        results = searchService.queryAssets("trickyRo", 10, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1L, results.getTotalPages());
    }

}
