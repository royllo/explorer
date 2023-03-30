package org.royllo.explorer.core.service.request;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.core.domain.request.AddProof;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.joining;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_DTO;
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
        logger.info("getOpenedRequests - Getting opened requests");

        // Getting results.
        final List<RequestDTO> results = requestRepository.findByStatusOrderById(OPENED)
                .stream()
                .map(REQUEST_MAPPER::mapToRequestDTO)
                .toList();

        // Displaying logs.
        if (results.isEmpty()) {
            logger.info("getOpenedRequests - There is no result");
        } else {
            logger.info("getOpenedRequests - {} results with requests ids: {}",
                    results.size(),
                    results.stream()
                            .map(RequestDTO::getId)
                            .map(Object::toString)
                            .collect(joining(", ")));
        }
        return results;
    }

    @Override
    public Optional<RequestDTO> getRequest(final long id) {
        logger.info("getRequest - Getting request with id {}", id);

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
    public Optional<RequestDTO> getRequestByRequestId(@NonNull final String requestId) {
        logger.info("getRequestByRequestId - Getting request with requestId {}", requestId);

        final Optional<Request> request = requestRepository.findByRequestId(requestId);
        if (request.isEmpty()) {
            logger.info("getRequestByRequestId - Request with request requestId {} not found", requestId);
            return Optional.empty();
        } else {
            logger.info("getRequestByRequestId - Request with request requestId {} found: {}", requestId, request.get());
            return request.map(REQUEST_MAPPER::mapToRequestDTO);
        }
    }

    @Override
    public AddProofRequestDTO addProofRequest(@NonNull final String rawProof) {
        logger.info("addProofRequest - Adding proof request with raw proof {}", rawProof);

        // Creating and saving the request.
        AddProof request = AddProof.builder()
                .requestId(UUID.randomUUID().toString())
                .creator(USER_MAPPER.mapToUser(ANONYMOUS_USER_DTO))
                .status(OPENED)
                .rawProof(rawProof)
                .build();

        AddProofRequestDTO savedRequest = REQUEST_MAPPER.mapToAddAssetRequestDTO(requestRepository.save(request));
        logger.info("addProofRequest - Request {} saved", savedRequest);
        return savedRequest;
    }

    @Override
    public AddAssetMetaDataRequestDTO addAssetMetaDataRequest(@NonNull final String assetId,
                                                              final String metaData) {
        logger.info("addAssetMetaDataRequest - Adding metadata request for taro asset id {}", assetId);

        // =============================================================================================================
        // Creating the request.
        AddAssetMetaDataRequest request = AddAssetMetaDataRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .creator(USER_MAPPER.mapToUser(ANONYMOUS_USER_DTO))
                .status(OPENED)
                .assetId(assetId)
                .metaData(metaData)
                .build();

        // =============================================================================================================
        // Saving the request.
        AddAssetMetaDataRequestDTO savedRequest = REQUEST_MAPPER.mapToAddAssetMetaRequestDTO(requestRepository.save(request));
        logger.info("addAssetMetaDataRequest - Request {} saved", savedRequest);
        return savedRequest;
    }

}
