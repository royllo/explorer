package org.royllo.explorer.core.test.core.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.domain.asset.AssetState;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;

@SpringBootTest
@DisplayName("AssetStateRepository tests")
public class AssetStateRepositoryTest {

    @Autowired
    private AssetStateRepository assetStateRepository;

    @Test
    @DisplayName("findByAsset_AssetIdOrderById()")
    public void findByAsset_AssetIdOrderById() {
        // One asset with three asset states.
        Page<AssetState> results = assetStateRepository.findByAsset_AssetIdOrderById(TRICKY_ROYLLO_COIN_ASSET_ID, Pageable.ofSize(2));
        assertEquals(4, results.getTotalElements());
        assertEquals(2, results.getSize());
        assertEquals(2, results.getTotalPages());

        // We change the page size.
        results = assetStateRepository.findByAsset_AssetIdOrderById(TRICKY_ROYLLO_COIN_ASSET_ID, Pageable.ofSize(5));
        assertEquals(4, results.getTotalElements());
        assertEquals(5, results.getSize());
        assertEquals(1, results.getTotalPages());
    }

}
