package org.royllo.explorer.core.service.asset;

import org.royllo.explorer.core.dto.asset.AssetGroupDTO;

import java.util.Optional;

/**
 * Asset group service.
 */
public interface AssetGroupService {

    /**
     * Add an asset group.
     *
     * @param newAssetGroup asset group to create
     * @return asset group created
     */
    AssetGroupDTO addAssetGroup(AssetGroupDTO newAssetGroup);

    /**
     * Get an asset group by its tweaked group key.
     *
     * @param tweakedGroupKey tweaked group key
     * @return asset group
     */
    Optional<AssetGroupDTO> getAssetGroupByTweakedGroupKey(String tweakedGroupKey);

}
