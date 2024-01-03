package org.royllo.explorer.core.service.request;

import org.royllo.explorer.core.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.AddUniverseServerRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.util.enums.ProofType;

import java.util.List;
import java.util.Optional;

/**
 * Request service.
 */
public interface RequestService {

    /**
     * Returns the list of opened requests.
     * (will return a max of 100 requests)
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
     * Get a request by its requestId.
     *
     * @param requestId requestId
     * @return request
     */
    Optional<RequestDTO> getRequestByRequestId(String requestId);

    /**
     * Creates a request to add a proof.
     *
     * @param proof proof that validates the asset information
     * @return id The request created
     */
    AddProofRequestDTO createAddProofRequest(String proof);

    /**
     * Creates a request to add a proof.
     *
     * @param proof     proof that validates the asset information
     * @param proofType proof type
     * @return id of the request created
     */
    AddProofRequestDTO createAddProofRequest(String proof, ProofType proofType);

    /**
     * Creates a request to add an asset meta data.
     *
     * @param assetId  asset id
     * @param metaData Metadata corresponding to the meta hash stored in the genesis information
     * @return id of the request created
     */
    AddAssetMetaDataRequestDTO createAddAssetMetaDataRequest(String assetId,
                                                             String metaData);

    /**
     * Creates a request to add a royllo universe server to royllo database.
     *
     * @param serverAddress server address
     * @return id of the request created
     */
    AddUniverseServerRequestDTO createAddUniverseServerRequest(String serverAddress);

}
