package org.royllo.explorer.core.service.asset;

import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Asset service.
 */
public interface AssetService {

    /**
     * Query for assets.
     * - Search if query is an assetId, if true, returns only this one.
     * - Search if query contains in assets name.
     *
     * @param query    the query
     * @param page     the page number we want to retrieve (First page is page 1)
     * @param pageSize the page size
     * @return list of assets corresponding to the search
     */
    Page<AssetDTO> queryAssets(String query, int page, int pageSize);

    /**
     * Add an asset.
     *
     * @param newAsset asset to createL
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
     * Get an asset by asset id.
     *
     * @param assetId asset id
     * @return asset
     */
    Optional<AssetDTO> getAssetByAssetId(String assetId);

}
