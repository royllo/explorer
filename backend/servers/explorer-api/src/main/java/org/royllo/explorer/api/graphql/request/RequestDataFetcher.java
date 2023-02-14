package org.royllo.explorer.api.graphql.request;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsTypeResolver;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.service.request.RequestService;

/**
 * Request data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class RequestDataFetcher extends BaseDataFetcher {

    /** Request service. */
    private final RequestService requestService;

    /**
     * DGS type resolver.
     *
     * @param requestDTO request dto
     * @return type
     */
    @DgsTypeResolver(name = "Request")
    public String resolveRequest(final RequestDTO requestDTO) {
        // TODO Simplify when pattern Matching for switch will be available.
        if (requestDTO instanceof AddProofRequestDTO) {
            return "AddProofRequest";
        } else if (requestDTO instanceof AddAssetMetaDataRequestDTO) {
            return "AddAssetMetaDataRequest";
        } else {
            throw new RuntimeException("Invalid type: " + requestDTO.getClass().getName() + " found in resolveRequest");
        }
    }

    /**
     * Get a request by its request id in database.
     *
     * @param requestId request id
     * @return request
     */
    @DgsQuery
    public final RequestDTO requestByRequestId(final @InputArgument String requestId) {
        return requestService.getRequestByRequestId(requestId).orElseThrow(DgsEntityNotFoundException::new);
    }

    /**
     * Add a request to add a proof.
     *
     * @param input add asset request inputs
     * @return request created
     */
    @DgsMutation
    public final AddProofRequestDTO addProofRequest(final @InputArgument AddProofRequestInputs input) {
        return requestService.addProof(input.getRawProof());
    }

    /**
     * Add a request to add an asset meta data.
     *
     * @param input add asset meta data request inputs
     * @return request created
     */
    @DgsMutation
    public final AddAssetMetaDataRequestDTO addAssetMetaDataRequest(final @InputArgument AddAssetMetaDataRequestInputs input) {
        return requestService.addAssetMetaData(input.getAssetId(), input.getMetaData());
    }

}
