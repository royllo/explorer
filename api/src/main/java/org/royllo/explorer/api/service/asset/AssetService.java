package org.royllo.explorer.api.service.asset;

import org.royllo.explorer.api.dto.asset.AssetDTO;

import java.util.Optional;

/**
 * Asset service.
 */
public interface AssetService {

    /**
     * Get an asset.
     * @param id id
     * @return asset
     */
    Optional<AssetDTO> getAsset(long id);

    /**
     * Get an asset by asset id.
     * @param assetId asset id
     * @return asset
     */
    Optional<AssetDTO> getAssetByAssetId(String assetId);

}
