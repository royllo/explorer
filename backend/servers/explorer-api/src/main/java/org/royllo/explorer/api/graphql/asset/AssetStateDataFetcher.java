package org.royllo.explorer.api.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.springframework.data.domain.Page;

/**
 * Asset state data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetStateDataFetcher extends BaseDataFetcher {

    /** Asset state service. */
    private final AssetStateService assetStateService;

    /**
     * Get asset state by asset state id.
     *
     * @param assetId    asset id
     * @param pageNumber the page number you want
     * @param pageSize   the page size you want
     * @return list of assets corresponding to the search
     */
    @DgsQuery
    public final Page<AssetStateDTO> assetStatesByAssetId(final @InputArgument String assetId,
                                                          final @InputArgument Integer pageNumber,
                                                          final @InputArgument Integer pageSize) {
        return assetStateService.getAssetStatesByAssetId(assetId,
                getCleanedPageNumber(pageNumber),
                getCleanedPageSize(pageSize));
    }

}
