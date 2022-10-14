package org.royllo.explorer.core.service.asset;

import org.royllo.explorer.core.dto.asset.AssetDTO;

import java.util.List;
import java.util.Optional;

/**
 * Asset service.
 */
public interface AssetService {

    /**
     * Query for assets.
     * - Search if the value is an assetId, if true, returns only this one.
     * - Search if the value is contained in assets name.
     *
     * @param value the value to search for
     * @return list of assets corresponding to the search
     */
    List<AssetDTO> queryAssets(String value);

    /**
     * Get an asset.
     *
     * @param id id in database
     * @return asset
     */
    Optional<AssetDTO> getAsset(long id);

    /**
     * Get an asset by asset id.
     *
     * @param assetId asset id
     * @return asset
     */
    Optional<AssetDTO> getAssetByAssetId(String assetId);

}
