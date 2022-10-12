package org.royllo.explorer.api.service.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.domain.request.AddAssetMetaRequest;
import org.royllo.explorer.api.domain.request.AddAssetRequest;
import org.royllo.explorer.api.domain.user.User;
import org.royllo.explorer.api.dto.request.AddAssetMetaRequestDTO;
import org.royllo.explorer.api.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.api.dto.request.RequestDTO;
import org.royllo.explorer.api.repository.request.RequestRepository;
import org.royllo.explorer.api.repository.user.UserRepository;
import org.royllo.explorer.api.util.base.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static org.royllo.explorer.api.util.constants.UserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.api.util.enums.RequestStatus.NEW;

/**
 * Request service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class RequestServiceImplementation extends BaseService implements RequestService {

    /** Anonymous user. */
    private User anonymousUser;

    /** User repository. */
    private final UserRepository userRepository;

    /** Request repository. */
    private final RequestRepository requestRepository;

    /**
     * Initialize request service implementation :
     * - Get and cache anonymous user.
     */
    @PostConstruct
    void initialize() {
        // We retrieve and cache the anonymous user as, for the moment, we don't manage users.
        userRepository.findById(ANONYMOUS_USER_ID).ifPresent(user -> anonymousUser = user);
    }

    @Override
    public Optional<RequestDTO> getRequest(final long id) {
        return requestRepository.findById(id).map(REQUEST_MAPPER::mapToRequestDTO);
    }

    @Override
    public AddAssetRequestDTO addAsset(final String genesisBootstrapInformation,
                                       final String proof) {
        // Creating the request.
        AddAssetRequest request = AddAssetRequest.builder()
                .creator(anonymousUser)
                .status(NEW)
                .genesisBootstrapInformation(genesisBootstrapInformation)
                .proof(proof)
                .build();
        logger.debug("addAsset - New request {}", request);

        // Saving the request.
        AddAssetRequestDTO savedRequest = REQUEST_MAPPER.mapToAddAssetRequestDTO(requestRepository.save(request));
        logger.debug("addAsset - Request {} saved", savedRequest);
        return savedRequest;
    }

    @Override
    public AddAssetMetaRequestDTO addAssetMeta(final String taroAssetId,
                                               final String meta) {
        // Creating the request.
        AddAssetMetaRequest request = AddAssetMetaRequest.builder()
                .creator(anonymousUser)
                .status(NEW)
                .taroAssetId(taroAssetId)
                .meta(meta)
                .build();
        logger.debug("addAssetMeta - New request {}", request);

        // Saving the request.
        AddAssetMetaRequestDTO savedRequest = REQUEST_MAPPER.mapToAddAssetMetaRequestDTO(requestRepository.save(request));
        logger.debug("addAssetMeta - Request {} saved", savedRequest);
        return savedRequest;
    }

}
