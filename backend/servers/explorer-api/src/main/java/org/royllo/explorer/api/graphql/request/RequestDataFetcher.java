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
import org.royllo.explorer.core.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.service.request.RequestService;

import java.util.List;

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
        if (requestDTO instanceof AddAssetRequestDTO) {
            return "AddAssetRequest";
        } else if (requestDTO instanceof AddAssetMetaDataRequestDTO) {
            return "AddAssetMetaDataRequest";
        } else {
            throw new RuntimeException("Invalid type: " + requestDTO.getClass().getName() + " found in resolveRequest");
        }
    }

    /**
     * Get opened requests.
     *
     * @return requests with OPENED status
     */
    @DgsQuery
    public final List<RequestDTO> openedRequests() {
        return requestService.getOpenedRequests();
    }

    /**
     * Get a request by its id in database.
     *
     * @param id request id
     * @return request
     */
    @DgsQuery
    public final RequestDTO request(final @InputArgument long id) {
        return requestService.getRequest(id).orElseThrow(DgsEntityNotFoundException::new);
    }

    /**
     * Add a request to add an asset.
     *
     * @param input add asset request inputs
     * @return request created
     */
    @DgsMutation
    public final AddAssetRequestDTO addAssetRequest(final @InputArgument AddAssetRequestInputs input) {
        return requestService.addAsset(input.getGenesisPoint(),
                input.getName(),
                input.getMetaData(),
                input.getAssetId(),
                input.getOutputIndex(),
                input.getProof());
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
