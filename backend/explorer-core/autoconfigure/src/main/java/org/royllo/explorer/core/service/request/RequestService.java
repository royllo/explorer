package org.royllo.explorer.core.service.request;

import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.AddUniverseServerRequestDTO;
import org.royllo.explorer.core.dto.request.ClaimAssetOwnershipRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.util.enums.ProofType;

import java.util.List;
import java.util.Optional;

/**
 * Request service.
 */
public interface RequestService {

    /**
     * Get the list of opened requests.
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
     * @return Request created
     */
    AddProofRequestDTO createAddProofRequest(String proof, ProofType proofType);

    /**
     * Creates a request to add a universe server.
     *
     * @param serverAddress server address
     * @return Request created
     */
    AddUniverseServerRequestDTO createAddUniverseServerRequest(String serverAddress);

    /**
     * Creates a request to claim ownership of an existing asset.
     *
     * @param userId           user id
     * @param proofWithWitness proof with witness
     * @return Request created
     */
    ClaimAssetOwnershipRequestDTO createClaimAssetOwnershipRequest(String userId,
                                                                   String proofWithWitness);

}
