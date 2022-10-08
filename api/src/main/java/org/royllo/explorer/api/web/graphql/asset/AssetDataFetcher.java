package org.royllo.explorer.api.web.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.dto.asset.AssetDTO;
import org.royllo.explorer.api.service.asset.AssetService;

import java.util.List;

/**
 * Asset data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetDataFetcher {

    /** Asset service. */
    private final AssetService assetService;

    /**
     * Query for assets.
     * - Search if the value is an assetId, if true, returns only this one.
     * - Search if the value is contained in assets name.
     *
     * @param value the value to search for
     * @return list of assets corresponding to the search
     */
    @DgsQuery
    public final List<AssetDTO> queryAssets(final @InputArgument String value) {
        return assetService.queryAssets(value);
    }

    /**
     * Get an asset by its id in database.
     *
     * @param id id in database
     * @return asset
     */
    @DgsQuery
    public final AssetDTO asset(final @InputArgument long id) {
        return assetService.getAsset(id).orElse(null);
    }

    /**
     * Get an asset by its taro asset id.
     *
     * @param assetId taro asset id
     * @return asset
     */
    @DgsQuery
    public final AssetDTO assetByAssetId(final @InputArgument String assetId) {
        return assetService.getAssetByAssetId(assetId).orElse(null);
    }

}
