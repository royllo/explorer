package org.royllo.explorer.core.service.asset;

import jakarta.validation.Valid;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetDTOCreatorUpdate;
import org.royllo.explorer.core.dto.asset.AssetDTOIssuanceUpdate;
import org.royllo.explorer.core.util.validator.PageNumber;
import org.royllo.explorer.core.util.validator.PageSize;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Asset service.
 */
public interface AssetService {

    /**
     * Add an asset.
     *
     * @param newAsset asset to create
     * @return asset created
     */
    AssetDTO addAsset(@Valid AssetDTO newAsset);

    /**
     * Update an asset with issuance data.
     * Some data can only be retrieved from issuance proof.
     * This method is used to update the asset when we encounter the asset issuance proof.
     *
     * @param assetId     asset id that need to be updated
     * @param assetUpdate data to update
     */
    void updateAssetIssuanceData(String assetId,
                                 @Valid AssetDTOIssuanceUpdate assetUpdate);

    /**
     * Update an asset with creator data.
     *
     * @param assetId     asset id that need to be updated
     * @param assetUpdate data to update
     */
    void updateAssetCreatorData(String assetId,
                                @Valid AssetDTOCreatorUpdate assetUpdate);

    /**
     * Get an asset.
     *
     * @param id id in database
     * @return asset
     */
    Optional<AssetDTO> getAsset(long id);

    /**
     * Get an asset by its asset id or asset id alias.
     *
     * @param assetIdOrAlias asset id or asset id alias
     * @return asset
     */
    Optional<AssetDTO> getAssetByAssetIdOrAlias(String assetIdOrAlias);


    /**
     * Get assets by asset group id.
     *
     * @param assetGroupId asset group id
     * @param pageNumber   page number
     * @param pageSize     page size
     * @return assets
     */
    Page<AssetDTO> getAssetsByAssetGroupId(String assetGroupId,
                                           @PageNumber int pageNumber,
                                           @PageSize int pageSize);

    /**
     * Get assets by username.
     *
     * @param username   username
     * @param pageNumber page number
     * @param pageSize   page size
     * @return assets owned by user
     */
    Page<AssetDTO> getAssetsByUsername(String username,
                                       @PageNumber int pageNumber,
                                       @PageSize int pageSize);

    /**
     * Get assets by userId.
     *
     * @param userId     userId
     * @param pageNumber page number
     * @param pageSize   page size
     * @return assets owned by user
     */
    Page<AssetDTO> getAssetsByUserId(String userId,
                                     @PageNumber int pageNumber,
                                     @PageSize int pageSize);

}
