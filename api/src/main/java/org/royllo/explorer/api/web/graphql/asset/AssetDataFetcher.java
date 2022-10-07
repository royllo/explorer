package org.royllo.explorer.api.web.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.dto.asset.AssetDTO;
import org.royllo.explorer.api.service.asset.AssetService;

/**
 * Asset data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetDataFetcher {

    /** Asset service. */
    private final AssetService assetService;

    /**
     * Get an asset by its id and vout.
     *
     * @param id asset id
     * @return transaction output
     */
    @DgsQuery
    public final AssetDTO asset(final @InputArgument long id) {
        System.out.println("---> " + assetService.getAsset(id));
        return assetService.getAsset(id).orElse(null);
    }

}
