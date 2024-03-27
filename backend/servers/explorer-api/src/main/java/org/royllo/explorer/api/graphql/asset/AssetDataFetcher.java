package org.royllo.explorer.api.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.search.SearchService;
import org.springframework.data.domain.Page;

/**
 * Asset data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetDataFetcher extends BaseDataFetcher {

    /** Asset service. */
    private final AssetService assetService;

    /** Search service. */
    private final SearchService searchService;

    /**
     * Query assets.
     * - Search if the "query" parameter is an assetId, if true, returns only this one.
     * - Search if the "query" parameter contains in assets name.
     *
     * @param query      the query to search for
     * @param pageNumber the page number you want
     * @param pageSize   the page size you want
     * @return list of assets corresponding to the search
     */
    @DgsQuery
    public final Page<AssetDTO> queryAssets(final @InputArgument String query,
                                            final @InputArgument Integer pageNumber,
                                            final @InputArgument Integer pageSize) {
        return searchService.queryAssets(query,
                getCleanedPageNumber(pageNumber),
                getCleanedPageSize(pageSize));
    }

    /**
     * Get an asset by its asset id.
     *
     * @param assetId taproot asset id
     * @return asset
     */
    @DgsQuery
    public final AssetDTO assetByAssetId(final @InputArgument String assetId) {
        return assetService.getAssetByAssetIdOrAlias(assetId)
                .orElseThrow(DgsEntityNotFoundException::new);
    }

}
