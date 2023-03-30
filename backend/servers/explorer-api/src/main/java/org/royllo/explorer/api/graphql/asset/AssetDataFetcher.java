package org.royllo.explorer.api.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.netflix.graphql.dgs.exceptions.DgsInvalidInputArgumentException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.springframework.data.domain.Page;

import java.util.Objects;

import static org.royllo.explorer.api.configuration.APIConfiguration.DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.api.configuration.APIConfiguration.FIRST_PAGE;
import static org.royllo.explorer.api.configuration.APIConfiguration.MAXIMUM_PAGE_SIZE;

/**
 * Asset data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetDataFetcher extends BaseDataFetcher {

    /** Asset service. */
    private final AssetService assetService;

    /**
     * Query assets.
     * - Search if the "query" parameter is an assetId, if true, returns only this one.
     * - Search if the "query" parameter contains in assets name.
     *
     * @param query    the query to search for
     * @param page     the page number you want
     * @param pageSize the page size you want
     * @return list of assets corresponding to the search
     */
    @DgsQuery
    public final Page<AssetDTO> queryAssets(final @InputArgument String query,
                                            final @InputArgument Integer page,
                                            final @InputArgument Integer pageSize) {
        // Checking maximum page size.
        // Note : page size validation (> 0) is done by the service layer.
        if (Objects.requireNonNullElse(pageSize, DEFAULT_PAGE_SIZE) > MAXIMUM_PAGE_SIZE) {
            throw new DgsInvalidInputArgumentException("Page size can't be superior to " + MAXIMUM_PAGE_SIZE, null);
        }

        // Return the results.
        return assetService.queryAssets(query,
                Objects.requireNonNullElse(page, FIRST_PAGE),
                Objects.requireNonNullElse(pageSize, DEFAULT_PAGE_SIZE));
    }

    /**
     * Get an asset by its asset id.
     *
     * @param assetId taro asset id
     * @return asset
     */
    @DgsQuery
    public final AssetDTO assetByAssetId(final @InputArgument String assetId) {
        return assetService.getAssetByAssetId(assetId).orElseThrow(DgsEntityNotFoundException::new);
    }

}
