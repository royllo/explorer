package org.royllo.explorer.api.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.springframework.data.domain.Page;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.royllo.explorer.api.configuration.APIConfiguration.DEFAULT_PAGE_SIZE;

/**
 * Asset data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetDataFetcher extends BaseDataFetcher {

    /**
     * Asset service.
     */
    private final AssetService assetService;

    /**
     * Query for assets.
     * - Search if query is an assetId, if true, returns only this one.
     * - Search if query contains in assets name.
     *
     * @param query the query to search for
     * @param page  the page number you want
     * @return list of assets corresponding to the search
     */
    @DgsQuery
    public final Page<AssetDTO> queryAssets(final @InputArgument String query,
                                            final @InputArgument Integer page) {
        final Page<AssetDTO> results = assetService.queryAssets(query,
                Objects.requireNonNullElse(page, 1),
                DEFAULT_PAGE_SIZE);
        // Logs.
        if (results.isEmpty()) {
            logger.info("queryAssets - For '{}', there is no results", query);
        } else {
            logger.info("queryAssets - For '{}', {} result(s) with assets id(s): {}", query,
                    results.getTotalElements(),
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
