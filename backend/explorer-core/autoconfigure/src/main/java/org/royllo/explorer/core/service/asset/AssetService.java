package org.royllo.explorer.core.service.asset;

import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Asset service.
 */
public interface AssetService {

    /**
     * Query assets following this algorithm:
     * - Search if the "query" parameter is a tweaked group keys (asset group) > returns all assets of this asset group.
     * - Search if the "query" parameter is an assetId (asset) > returns the asset.
     * - Else search the "query" parameter in assets names.
     *
     * @param query    the query
     * @param page     the page we want to retrieve (First page is page 1)
     * @param pageSize the page size
     * @return list of assets corresponding to the search
     */
    Page<AssetDTO> queryAssets(String query, int page, int pageSize);

    /**
     * Add an asset.
     *
     * @param newAsset asset to create
     * @return asset created
     */
    AssetDTO addAsset(AssetDTO newAsset);

    /**
     * Get an asset.
     *
     * @param id id in database
     * @return asset
     */
    Optional<AssetDTO> getAsset(long id);

    /**
     * Get an asset by its asset id or asset id alias.
     *
     * @param assetId asset id or asset id alias
     * @return asset
     */
    Optional<AssetDTO> getAssetByAssetId(String assetId);

    /**
     * Get assets by asset group id.
     *
     * @param assetGroupId asset group id
     * @param page         page number
     * @param pageSize     page size
     * @return assets
     */
    Page<AssetDTO> getAssetsByAssetGroupId(String assetGroupId, int page, int pageSize);

}
