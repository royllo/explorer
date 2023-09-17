package org.royllo.explorer.core.service.asset;

import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Asset state service.
 */
public interface AssetStateService {


    /**
     * Query asset states.
     * - Search if the "query" parameter contains "assetId:" in the beginning, if true, returns all lined asset states.
     *
     * @param query    the query
     * @param page     the page we want to retrieve (First page is page 1)
     * @param pageSize the page size
     * @return list of asset states corresponding to the search
     */
    Page<AssetStateDTO> queryAssetStates(String query, int page, int pageSize);

    /**
     * Add an asset state.
     *
     * @param newAssetState asset state to create
     * @return asset state created
     */
    AssetStateDTO addAssetState(AssetStateDTO newAssetState);

    /**
     * Get an asset state by its asset state.
     *
     * @param assetStateId asset state id
     * @return asset state
     */
    Optional<AssetStateDTO> getAssetStateByAssetStateId(String assetStateId);

}
