package org.royllo.explorer.core.repository.asset;

import org.royllo.explorer.core.domain.asset.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * {@link Asset} repository.
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
     * find by asset id.
     *
     * @param assetIdAlias asset id alias
     * @return asset
     */
    Optional<Asset> findByAssetIdAlias(String assetIdAlias);

    /**
     * Find an asset by its complete or partial name.
     *
     * @param name     complete or partial name
     * @param pageable page parameters
     * @return assets containing the complete or partial name
     */
    Page<Asset> findByNameContainsIgnoreCaseOrderByName(String name, Pageable pageable);

    /**
     * Find assets by asset group id.
     *
     * @param assetGroupId asset group id
     * @return assets
     */
    @SuppressWarnings("checkstyle:MethodName")
    Set<Asset> findByAssetGroup_AssetGroupId(String assetGroupId);

}
