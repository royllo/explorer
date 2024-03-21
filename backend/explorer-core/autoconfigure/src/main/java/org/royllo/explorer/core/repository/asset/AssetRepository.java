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
     * Find an asset by its asset id.
     *
     * @param assetId asset id
     * @return asset
     */
    Optional<Asset> findByAssetId(String assetId);

    /**
     * Find an asset by its asset id alias.
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
     * @param pageable   pagination parameters
     * @return results
     */
    @Query(value = "SELECT * FROM ASSET WHERE NAME ILIKE %?1%",
            countQuery = "SELECT count(*) FROM ASSET WHERE NAME ILIKE %?1%",
            nativeQuery = true)
    Page<Asset> findByName(String searchTerm, Pageable pageable);

    /**
     * Find assets by its complete or partial name.
     * Used if the database in use is not PostgreSQL.
     *
     * @param name     complete or partial name
     * @param pageable page parameters
     * @return assets containing the complete or partial name
     */
    Page<Asset> findByNameContainsIgnoreCaseOrderByName(String name, Pageable pageable);

    /**
     * Find assets by its asset group id.
     *
     * @param assetGroupId asset group id
     * @param pageable     pagination parameters
     * @return assets
     */
    @SuppressWarnings("checkstyle:MethodName")
    Page<Asset> findByAssetGroup_AssetGroupIdOrderById(String assetGroupId, Pageable pageable);

    /**
     * Find assets by its creator username.
     *
     * @param username creator username
     * @param pageable pagination parameters
     * @return user assets
     */
    @SuppressWarnings("checkstyle:MethodName")
    Page<Asset> findByCreator_UsernameOrderById(String username, Pageable pageable);

    /**
     * Find assets by its creator user id.
     *
     * @param userId   user id
     * @param pageable pagination parameters
     * @return user assets
     */
    Page<Asset> findByCreator_UserIdOrderById(String userId, Pageable pageable);

}
