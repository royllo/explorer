package org.royllo.explorer.api.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.service.asset.AssetService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Asset data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetDataFetcher extends BaseDataFetcher {

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
        final List<AssetDTO> results = assetService.queryAssets(value);
        if (results.isEmpty()) {
            logger.info("queryAssets - For '{}', there is no results", value);
        } else {
            logger.info("queryAssets - For '{}', {} result(s) with assets id(s): {}", value,
                    results.size(),
                    results.stream()
                            .map(AssetDTO::getId)
                            .map(Objects::toString)
                            .collect(Collectors.joining(", ")));
        }
        return results;
    }

    /**
     * Get an asset by its id in database.
     *
     * @param id id in database
     * @return asset
     */
    @DgsQuery
    public final AssetDTO asset(final @InputArgument long id) {
        final Optional<AssetDTO> asset = assetService.getAsset(id);
        if (asset.isEmpty()) {
            logger.info("asset - Asset with id {} not found", id);
            throw new DgsEntityNotFoundException();
        } else {
            logger.info("asset - Asset with id {} found: {}", id, asset.get());
            return asset.get();
        }
    }

    /**
     * Get an asset by its taro asset id.
     *
     * @param assetId taro asset id
     * @return asset
     */
    @DgsQuery
    public final AssetDTO assetByAssetId(final @InputArgument String assetId) {
        final Optional<AssetDTO> asset = assetService.getAssetByAssetId(assetId);
        if (asset.isEmpty()) {
            logger.info("assetByAssetId - Asset with id {} not found", assetId);
            throw new DgsEntityNotFoundException();
        } else {
            logger.info("assetByAssetId - Asset with id {} found: {}", assetId, asset.get());
            return asset.get();
        }
    }

}
