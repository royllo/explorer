package org.royllo.explorer.core.repository.asset;

import org.royllo.explorer.core.domain.asset.AssetGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link AssetGroup} repository.
 */
@Repository
public interface AssetGroupRepository extends JpaRepository<AssetGroup, Long> {

}
