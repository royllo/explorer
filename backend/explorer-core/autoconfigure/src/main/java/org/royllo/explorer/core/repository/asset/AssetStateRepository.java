package org.royllo.explorer.core.repository.asset;

import org.royllo.explorer.core.domain.asset.AssetState;
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

}
