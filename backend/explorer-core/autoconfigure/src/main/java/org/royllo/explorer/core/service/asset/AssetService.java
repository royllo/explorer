package org.royllo.explorer.core.service.asset;

import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.time.ZonedDateTime;
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
    AssetDTO addAsset(AssetDTO newAsset);

    /**
     * Update an asset.
     * Some data can only be retrieved from issuance proof.
     * This method is used to update the asset when we encounter the asset issuance proof.
     *
     * @param assetId      asset id that need to be updated
     * @param metadata     meta data
     * @param amount       amount minted
     * @param issuanceDate asset issuance date
     */
    void updateAsset(String assetId,
                     String metadata,
                     BigInteger amount,
                     ZonedDateTime issuanceDate);

    /**
     * Update an asset with user data.
     *
     * @param assetId         asset id that need to be updated
     * @param newAssetIdAlias new asset id alias
     * @param newReadme       readme
     */
    void updateAssetWithUserData(String assetId,
                                 String newAssetIdAlias,
                                 String newReadme);

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
     * @param assetId asset id or asset id alias
     * @return asset
     */
    Optional<AssetDTO> getAssetByAssetId(String assetId);


    /**
     * Get assets by asset group id.
     *
     * @param assetGroupId asset group id
     * @param page         page number
     * @param pageSize     page size
     * @return assets
     */
    Page<AssetDTO> getAssetsByAssetGroupId(String assetGroupId, int page, int pageSize);

    /**
     * Gets assets by username.
     *
     * @param username username
     * @param page     page number
     * @param pageSize page size
     * @return assets owned by user
     */
    Page<AssetDTO> getAssetsByUsername(String username, int page, int pageSize);

}
