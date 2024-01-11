package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.service.search.SQLSearchServiceImplementation;
import org.royllo.explorer.core.service.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;
import java.util.stream.Collectors;

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

        // Searching for an asset that doesn't exist.
        Page<AssetDTO> results = searchService.queryAssets("NON_EXISTING_ASSET_ID", 1, 5);
        assertEquals(0, results.getTotalElements());
        assertEquals(0, results.getTotalPages());

        // Searching for an asset with its group asset id
        results = searchService.queryAssets(SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey(), 1, 5);
        assertEquals(3, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(SET_OF_ROYLLO_NFT_1_ASSET_ID, results.getContent().get(0).getAssetId());
        assertEquals(SET_OF_ROYLLO_NFT_2_ASSET_ID, results.getContent().get(1).getAssetId());
        assertEquals(SET_OF_ROYLLO_NFT_3_ASSET_ID, results.getContent().get(2).getAssetId());

        // Searching for an asset with its asset id.
        results = searchService.queryAssets(ROYLLO_COIN_ASSET_ID, 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(1, results.getContent().get(0).getId());

        // Searching for an asset with its asset id alias.
        results = searchService.queryAssets(TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS, 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, results.getContent().get(0).getAssetId());

        // Searching for an asset with its partial name (trickyCoin) - only 1 result.
        results = searchService.queryAssets("ky", 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, results.getContent().get(0).getAssetId());

        // Searching for an asset with its partial name uppercase (trickyRoylloCoin) - only 1 result.
        results = searchService.queryAssets("kyR", 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, results.getContent().get(0).getAssetId());

        // Searching for an asset with its partial name corresponding to eight assets.
        results = searchService.queryAssets("royllo", 1, 4);
        assertEquals(8, results.getTotalElements());
        assertEquals(2, results.getTotalPages());
        Set<Long> ids = results.stream()
                .map(AssetDTO::getId)
                .collect(Collectors.toSet());
        assertTrue(ids.contains(1L));
        assertTrue(ids.contains(2L));
        assertTrue(ids.contains(3L));
        assertTrue(ids.contains(4L));
    }

}
