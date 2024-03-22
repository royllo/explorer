package org.royllo.explorer.core.service.search;

import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.util.validator.PageNumber;
import org.royllo.explorer.core.util.validator.PageSize;
import org.springframework.data.domain.Page;

/**
 * Search service.
 */
public interface SearchService {

    /**
     * Query assets following this algorithm:
     * - Search if the "query" parameter is a tweaked group keys (asset group) > returns all assets of this asset group.
     * - Search if the "query" parameter is an asset id (asset) > returns the asset.
     * - Otherwise search the "query" parameter in assets names.
     *
     * @param query      Query
     * @param pageNumber Page we want to retrieve (First page is page 1)
     * @param pageSize   Pge size (Maximum set to MAXIMUM_PAGE_SIZE)
     * @return list of assets corresponding to the search
     */
    Page<AssetDTO> queryAssets(String query,
                               @PageNumber int pageNumber,
                               @PageSize int pageSize);

}
