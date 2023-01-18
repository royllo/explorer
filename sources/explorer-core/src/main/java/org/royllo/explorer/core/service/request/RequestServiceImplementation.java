package org.royllo.explorer.core.service.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.core.domain.request.AddAssetRequest;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.core.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;

/**
 * Request service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class RequestServiceImplementation extends BaseService implements RequestService {

    /** Request repository. */
    private final RequestRepository requestRepository;

    @Override
    public List<RequestDTO> getOpenedRequests() {
        final List<RequestDTO> results = requestRepository.findByStatusOrderById(OPENED)
                .stream()
                .map(REQUEST_MAPPER::mapToRequestDTO)
                .toList();
        if (results.isEmpty()) {
            logger.info("getOpenedRequests - There is no results");
        } else {
            logger.info("getOpenedRequests - {} results with requests ids: {}",
                    results.size(),
                    results.stream()
                            .map(RequestDTO::getId)
                            .map(Object::toString)
                            .collect(Collectors.joining(", ")));
        }
        return results;
    }

    @Override
    public Optional<RequestDTO> getRequest(final long id) {
        final Optional<Request> request = requestRepository.findById(id);
        if (request.isEmpty()) {
            logger.info("getRequest - Request with id {} not found", id);
            return Optional.empty();
        } else {
            logger.info("getRequest - Request with id {} found: {}", id, request.get());
            return request.map(REQUEST_MAPPER::mapToRequestDTO);
        }
    }

    @Override
    public AddAssetRequestDTO addAsset(final String genesisPoint,
                                       final String name,
                                       final String metaData,
                                       final String assetId,
                                       final int outputIndex,
                                       final String proof) {
        // =============================================================================================================
        // Creating the request.
        AddAssetRequest request = AddAssetRequest.builder()
                .creator(USER_MAPPER.mapToUser(ANONYMOUS_USER))
                .status(OPENED)
                .genesisPoint(genesisPoint)
                .name(name)
                .metaData(metaData)
                .assetId(assetId)
                .outputIndex(outputIndex)
                .proof(proof)
                .build();
        logger.debug("addAsset - New request to add {}", request);

        // =============================================================================================================
        // Saving the request.
        AddAssetRequestDTO savedRequest = REQUEST_MAPPER.mapToAddAssetRequestDTO(requestRepository.save(request));
        logger.debug("addAsset - Request {} saved", savedRequest);
        return savedRequest;
    }

    @Override
    public AddAssetMetaDataRequestDTO addAssetMetaData(final String taroAssetId,
                                                       final String metaData) {
        // TODO add data validation

        // Creating the request.
        AddAssetMetaDataRequest request = AddAssetMetaDataRequest.builder()
                .creator(USER_MAPPER.mapToUser(ANONYMOUS_USER))
                .status(OPENED)
                .assetId(taroAssetId)
                .metaData(metaData)
                .build();
        logger.debug("addAssetMeta - New request {}", request);

        // Saving the request.
        AddAssetMetaDataRequestDTO savedRequest = REQUEST_MAPPER.mapToAddAssetMetaRequestDTO(requestRepository.save(request));
        logger.debug("addAssetMeta - Request {} saved", savedRequest);
        return savedRequest;
    }

}
