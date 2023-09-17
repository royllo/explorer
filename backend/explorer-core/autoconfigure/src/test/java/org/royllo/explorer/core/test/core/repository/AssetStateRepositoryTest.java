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

@SpringBootTest
@DisplayName("AssetStateRepositoryTest tests")
public class AssetStateRepositoryTest {

    @Autowired
    private AssetStateRepository assetStateRepository;

    @Test
    @DisplayName("findByAsset_AssetId()")
    public void findByAsset_AssetId() {
        // One asset with three asset states.
        Page<AssetState> results = assetStateRepository.findByAsset_AssetId("asset_id_9", Pageable.ofSize(2));
        assertEquals(3, results.getTotalElements());
        assertEquals(2, results.getSize());
        assertEquals(2, results.getTotalPages());

        // We change the number of elements by page
        results = assetStateRepository.findByAsset_AssetId("asset_id_9", Pageable.ofSize(5));
        assertEquals(3, results.getTotalElements());
        assertEquals(5, results.getSize());
        assertEquals(1, results.getTotalPages());
    }

}
