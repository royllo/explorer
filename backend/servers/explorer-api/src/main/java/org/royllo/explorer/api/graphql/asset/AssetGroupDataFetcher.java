package org.royllo.explorer.api.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.service.asset.AssetGroupService;

/**
 * Asset group data fetcher.
 * TODO NOT IMPLEMENTED YET - Waiting for asset group refactoring from lighting labs.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetGroupDataFetcher extends BaseDataFetcher {

    /** Asset group service. */
    private final AssetGroupService assetGroupService;

}
