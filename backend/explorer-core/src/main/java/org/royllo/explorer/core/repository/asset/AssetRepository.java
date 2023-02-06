package org.royllo.explorer.core.repository.asset;

import org.royllo.explorer.core.domain.asset.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Asset repository.
 */
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    /**
     * Find an asset by its assetId.
     *
     * @param assetId asset od
     * @return asset
     */
    Optional<Asset> findByAssetId(String assetId);

    /**
     * Find an asset with a partial name.
     *
     * @param name     partial name
     * @param pageable page configuration
     * @return assets containing the parameter
     */
    Page<Asset> findByNameContainsIgnoreCaseOrderByName(String name, Pageable pageable);

}
