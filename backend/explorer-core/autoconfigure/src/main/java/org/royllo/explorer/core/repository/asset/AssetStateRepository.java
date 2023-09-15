package org.royllo.explorer.core.repository.asset;

import org.royllo.explorer.core.domain.asset.AssetState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link AssetState} repository.
 */
@Repository
public interface AssetStateRepository extends JpaRepository<AssetState, Long> {

    /**
     * Find an asset state by its asset state id.
     *
     * @param assetStateId asset state id
     * @return asset state
     */
    Optional<AssetState> findByAssetStateId(String assetStateId);

    /**
     * Find all the asset states for a given asset.
     * TODO Add an order when we will know how to order them.
     *
     * @param assetId  asset id
     * @param pageable page parameters
     * @return asset states
     */
    @SuppressWarnings("checkstyle:MethodName")
    Page<AssetState> findByAsset_AssetId(String assetId, Pageable pageable);

}
