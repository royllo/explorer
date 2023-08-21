package org.royllo.explorer.core.repository.asset;

import org.royllo.explorer.core.domain.asset.AssetState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link AssetState} repository.
 */
@Repository
public interface AssetStateRepository extends JpaRepository<AssetState, Long> {

}
