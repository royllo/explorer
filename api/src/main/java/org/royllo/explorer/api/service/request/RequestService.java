package org.royllo.explorer.api.service.request;

import org.royllo.explorer.api.dto.request.AddAssetMetaRequestDTO;
import org.royllo.explorer.api.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.api.dto.request.RequestDTO;

import java.util.Optional;

/**
 * Request service.
 */
public interface RequestService {

    /**
     * Get a request.
     *
     * @param id id in database
     * @return request
     */
    Optional<RequestDTO> getRequest(long id);

    /**
     * Add an asset.
     *
     * @param genesisBootstrapInformation The full genesis information encoded in a portable manner, so it can be easily copied and pasted for address creation.
     * @param proof                       Proof that validates the asset information
     * @return id of the request created
     */
    AddAssetRequestDTO addAsset(String genesisBootstrapInformation,
                                String proof);

    /**
     * Add and asset meta.
     *
     * @param taroAssetId Taro asset id
     * @param meta        Metadata corresponding to the meta hash stored in the genesis information
     * @return id of the request created
     */
    AddAssetMetaRequestDTO addAssetMeta(String taroAssetId,
                                        String meta);

}
