package org.royllo.explorer.core.service.request;

import org.royllo.explorer.core.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.core.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;

import java.util.List;
import java.util.Optional;

/**
 * Request service.
 */
public interface RequestService {

    /**
     * Returns the list of opened requests.
     *
     * @return opened requests
     */
    List<RequestDTO> getOpenedRequests();

    /**
     * Get a request.
     *
     * @param id id in database
     * @return request
     */
    Optional<RequestDTO> getRequest(long id);

    /**
     * Add a request to add an asset.
     *
     * @param genesisPoint The first outpoint of the transaction that created the asset (txid:vout)
     * @param name         The name of the asset
     * @param metaData     The hashed metadata of the asset
     * @param assetId      The asset ID that uniquely identifies the asset
     * @param outputIndex  The index of the output that carries the unique Taro commitment in the genesis transaction
     * @param proof        Proof that validates the asset information
     * @return id of the request created
     */
    AddAssetRequestDTO addAsset(String genesisPoint,
                                String name,
                                String metaData,
                                String assetId,
                                int outputIndex,
                                String proof);

    /**
     * Add a request to add an asset meta data.
     *
     * @param taroAssetId Taro asset id
     * @param metaData    Metadata corresponding to the meta hash stored in the genesis information
     * @return id of the request created
     */
    AddAssetMetaDataRequestDTO addAssetMetaData(String taroAssetId,
                                                String metaData);

}
