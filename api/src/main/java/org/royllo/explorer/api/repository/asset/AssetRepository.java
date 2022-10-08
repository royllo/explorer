package org.royllo.explorer.api.repository.asset;

import org.royllo.explorer.api.domain.asset.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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
     * @param name partial name
     * @return assets containing the parameter
     */
    List<Asset> findByNameContainsIgnoreCase(String name);

}
