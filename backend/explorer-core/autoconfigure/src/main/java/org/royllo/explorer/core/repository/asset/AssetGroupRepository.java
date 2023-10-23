package org.royllo.explorer.core.repository.asset;

import org.royllo.explorer.core.domain.asset.AssetGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link AssetGroup} repository.
 */
@Repository
public interface AssetGroupRepository extends JpaRepository<AssetGroup, Long> {

    /**
     * Find an asset group by its tweaked group key.
     *
     * @param tweakedGroupKey tweaked group key
     * @return asset group
     */
    Optional<AssetGroup> findByTweakedGroupKey(String tweakedGroupKey);

}
