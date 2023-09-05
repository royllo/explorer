package org.royllo.explorer.api.graphql.asset;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsInvalidInputArgumentException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.springframework.data.domain.Page;

import java.util.Objects;

import static org.royllo.explorer.api.configuration.APIConfiguration.DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.api.configuration.APIConfiguration.FIRST_PAGE;
import static org.royllo.explorer.api.configuration.APIConfiguration.MAXIMUM_PAGE_SIZE;

/**
 * Asset state data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class AssetStateDataFetcher extends BaseDataFetcher {

    /** Asset state service. */
    private final AssetStateService assetStateService;

    @DgsQuery
    public final Page<AssetStateDTO> queryAssetStates(final @InputArgument String query,
                                                      final @InputArgument Integer page,
                                                      final @InputArgument Integer pageSize) {
        // Checking maximum page size.
        // Note : page size validation (> 0) is done by the service layer.
        if (Objects.requireNonNullElse(pageSize, DEFAULT_PAGE_SIZE) > MAXIMUM_PAGE_SIZE) {
            throw new DgsInvalidInputArgumentException("Page size can't be superior to " + MAXIMUM_PAGE_SIZE, null);
        }

        // Return the results.
        return assetStateService.queryAssetStates(query,
                Objects.requireNonNullElse(page, FIRST_PAGE),
                Objects.requireNonNullElse(pageSize, DEFAULT_PAGE_SIZE));
    }

}
