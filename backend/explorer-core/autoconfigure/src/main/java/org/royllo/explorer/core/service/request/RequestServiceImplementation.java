package org.royllo.explorer.core.service.request;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.core.domain.request.AddProofRequest;
import org.royllo.explorer.core.domain.request.AddUniverseServerRequest;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.AddUniverseServerRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.joining;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.openedStatus;

/**
 * {@link RequestService} implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class RequestServiceImplementation extends BaseService implements RequestService {

    /** Request repository. */
    private final RequestRepository requestRepository;

    @Override
    public List<RequestDTO> getOpenedRequests() {
        logger.info("Getting opened requests");

        // Getting results.
        final List<RequestDTO> results = requestRepository.findByStatusInOrderById(openedStatus())
                .stream()
                .map(REQUEST_MAPPER::mapToRequestDTO)
                .toList();

        // Displaying logs.
        if (results.isEmpty()) {
            logger.info("There is no result");
        } else {
            logger.info("{} results with requests ids: {}",
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
        logger.info("Getting request with id {}", id);

        final Optional<Request> request = requestRepository.findById(id);
        if (request.isEmpty()) {
            logger.info("Request with id {} not found", id);
            return Optional.empty();
        } else {
            logger.info("Request with id {} found: {}", id, request.get());
            return request.map(REQUEST_MAPPER::mapToRequestDTO);
        }
    }

    @Override
    public Optional<RequestDTO> getRequestByRequestId(@NonNull final String requestId) {
        logger.info("Getting request with requestId {}", requestId);

        final Optional<Request> request = requestRepository.findByRequestId(requestId);
        if (request.isEmpty()) {
            logger.info("Request with request requestId {} not found", requestId);
            return Optional.empty();
        } else {
            logger.info("Request with request requestId {} found: {}", requestId, request.get());
            return request.map(REQUEST_MAPPER::mapToRequestDTO);
        }
    }

    @Override
    public AddProofRequestDTO createAddProofRequest(final String proof) {
        logger.info("Adding proof request with proof {}", proof);

        // Creating and saving the request.
        AddProofRequest request = AddProofRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .creator(ANONYMOUS_USER)
                .status(OPENED)
                .proof(proof)
                .build();

        AddProofRequestDTO savedRequest = REQUEST_MAPPER.mapToAddAssetRequestDTO(requestRepository.save(request));
        logger.info("Request {} saved", savedRequest);
        return savedRequest;
    }

    @Override
    public AddAssetMetaDataRequestDTO createAddAssetMetaDataRequest(@NonNull final String assetId,
                                                                    final String metaData) {
        logger.info("Adding metadata request for Taproot asset id {}", assetId);

        // Creating and saving the request.
        AddAssetMetaDataRequest request = AddAssetMetaDataRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .creator(ANONYMOUS_USER)
                .status(OPENED)
                .assetId(assetId)
                .metaData(metaData)
                .build();

        AddAssetMetaDataRequestDTO savedRequest = REQUEST_MAPPER.mapToAddAssetMetaRequestDTO(requestRepository.save(request));
        logger.info("Request {} saved", savedRequest);
        return savedRequest;
    }

    @Override
    public AddUniverseServerRequestDTO createAddUniverseServerRequest(@NonNull final String serverAddress) {
        logger.info("Adding universe server {}", serverAddress);

        // Creating and saving the request.
        AddUniverseServerRequest request = AddUniverseServerRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .creator(ANONYMOUS_USER)
                .status(OPENED)
                .serverAddress(serverAddress)
                .build();

        AddUniverseServerRequestDTO savedRequest = REQUEST_MAPPER.mapToAddUniverseServerRequestDTO(requestRepository.save(request));
        logger.info("Request {} saved", savedRequest);
        return savedRequest;
    }
}
