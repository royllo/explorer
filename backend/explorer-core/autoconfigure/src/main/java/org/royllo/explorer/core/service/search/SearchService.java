package org.royllo.explorer.core.service.search;

import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.springframework.data.domain.Page;

/**
 * Search service.
 */
public interface SearchService {

    /**
     * Query assets following this algorithm:
     * - Search if the "query" parameter is a tweaked group keys (asset group) > returns all assets of this asset group.
     * - Search if the "query" parameter is an assetId (asset) > returns the asset.
     * - Else search the "query" parameter in assets names.
     *
     * @param query    the query
     * @param page     the page we want to retrieve (First page is page 1)
     * @param pageSize the page size
     * @return list of assets corresponding to the search
     */
    Page<AssetDTO> queryAssets(String query, int page, int pageSize);

}
