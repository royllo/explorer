package org.royllo.explorer.core.service.request;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.AssertionFailure;
import org.royllo.explorer.core.domain.request.AddProofRequest;
import org.royllo.explorer.core.domain.request.AddUniverseServerRequest;
import org.royllo.explorer.core.domain.request.ClaimAssetOwnershipRequest;
import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.AddUniverseServerRequestDTO;
import org.royllo.explorer.core.dto.request.ClaimAssetOwnershipRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.enums.ProofType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.joining;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_UNSPECIFIED;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.openedStatus;

/**
 * {@link RequestService} implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class RequestServiceImplementation extends BaseService implements RequestService {

    /** Maximum number of opened requests results. */
    public static final int MAXIMUM_OPENED_REQUESTS_RESULTS = 100;

    /** Request repository. */
    private final RequestRepository requestRepository;

    /** User service. */
    private final UserService userService;

    @Override
    public List<RequestDTO> getOpenedRequests() {
        logger.info("Getting opened requests");

        // Getting results.
        final List<RequestDTO> results = requestRepository.findByStatusInOrderById(openedStatus(),
                        Pageable.ofSize(MAXIMUM_OPENED_REQUESTS_RESULTS))
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
        return createAddProofRequest(proof, PROOF_TYPE_UNSPECIFIED);
    }

    @Override
    public AddProofRequestDTO createAddProofRequest(final String proof,
                                                    final ProofType proofType) {
        logger.info("Adding proof request with proof {}", proof);

        // Creating and saving the request.
        AddProofRequest request = AddProofRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .creator(ANONYMOUS_USER)
                .status(OPENED)
                .proof(proof)
                .type(proofType)
                .build();

        AddProofRequestDTO savedRequest = REQUEST_MAPPER.mapToAddAssetRequestDTO(requestRepository.save(request));
        logger.info("Request {} saved", savedRequest);
        return savedRequest;
    }

    @Override
    public AddUniverseServerRequestDTO createAddUniverseServerRequest(@NonNull final String serverAddress) {
        logger.info("Adding universe server request {}", serverAddress);

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

    @Override
    public ClaimAssetOwnershipRequestDTO createClaimAssetOwnershipRequest(final String userId,
                                                                          final String proofWithWitness) {
        logger.info("Adding claim ownership request {}", proofWithWitness);

        // Getting the user
        UserDTO user = userService.getUserByUserId(userId).orElseThrow(() -> new AssertionFailure("User not found: " + userId));

        // Creating and saving the request.
        ClaimAssetOwnershipRequest request = ClaimAssetOwnershipRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .creator(USER_MAPPER.mapToUser(user))
                .status(OPENED)
                .proofWithWitness(proofWithWitness)
                .build();

        ClaimAssetOwnershipRequestDTO savedRequest = REQUEST_MAPPER.mapToClaimAssetOwnershipRequestDTO(requestRepository.save(request));
        logger.info("Request {} saved", savedRequest);
        return savedRequest;
    }

}
