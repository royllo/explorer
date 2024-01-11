package org.royllo.explorer.core.repository.asset;

import org.royllo.explorer.core.domain.asset.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
     * Find assets by its complete or partial name.
     * Takes advantage of PostgreSQL "pg_trgm" and "ILIKE".
     *
     * @param searchTerm search term
     * @param pageable   pagination
     * @return results
     */
    @Query(value = "SELECT * FROM ASSET WHERE NAME ILIKE %?1%",
            countQuery = "SELECT count(*) FROM ASSET WHERE NAME ILIKE %?1%",
            nativeQuery = true)
    Page<Asset> findByName(String searchTerm, Pageable pageable);

    /**
     * Find assets by its complete or partial name.
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
     * @param pageable     page parameters
     * @return assets
     */
    @SuppressWarnings("checkstyle:MethodName")
    Page<Asset> findByAssetGroup_AssetGroupId(String assetGroupId, Pageable pageable);

}
