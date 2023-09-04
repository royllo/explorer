package org.royllo.explorer.api.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.service.asset.AssetStateService;

/**
 * Asset state data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetStateDataFetcher extends BaseDataFetcher {

    /** Asset state service. */
    private final AssetStateService assetStateService;

}
