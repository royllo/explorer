package org.royllo.explorer.api.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.service.asset.AssetGroupService;

/**
 * Asset group data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetGroupDataFetcher extends BaseDataFetcher {

    /** Asset group service. */
    private final AssetGroupService assetGroupService;

    /**
     * Get an asset group by its asset group id.
     *
     * @param assetGroupId asset group id
     * @return asset group
     */
    @DgsQuery
    public final AssetGroupDTO assetGroupByAssetGroupId(final @InputArgument String assetGroupId) {
        return assetGroupService.getAssetGroupByAssetGroupId(assetGroupId)
                .orElseThrow(DgsEntityNotFoundException::new);
    }

}
