package org.royllo.explorer.core.service.asset;

import org.royllo.explorer.core.dto.asset.AssetStateDTO;

import java.util.Optional;

/**
 * Asset state service.
 */
public interface AssetStateService {

    /**
     * Add an asset state.
     *
     * @param newAssetState asset state to create
     * @return asset state created
     */
    AssetStateDTO addAssetState(AssetStateDTO newAssetState);

    /**
     * Get an asset state by its anchor outpoint.
     *
     * @param anchorOutpoint anchor outpoint
     * @return asset state
     */
    Optional<AssetStateDTO> getAssetStateByAnchorOutpoint(String anchorOutpoint);

}
