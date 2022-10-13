package org.royllo.explorer.core.service.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.core.domain.request.AddAssetRequest;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.core.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.constants.UserConstants;
import org.royllo.explorer.core.util.enums.RequestStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

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
        userRepository.findById(UserConstants.ANONYMOUS_USER_ID).ifPresent(user -> anonymousUser = user);
    }

    @Override
    public List<RequestDTO> getOpenedRequests() {
        return requestRepository.findByStatusOrderById(RequestStatus.OPENED)
                .stream()
                .map(REQUEST_MAPPER::mapToRequestDTO)
                .toList();
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
                .status(RequestStatus.OPENED)
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
    public AddAssetMetaDataRequestDTO addAssetMetaData(final String taroAssetId,
                                                       final String metaData) {
        // Creating the request.
        AddAssetMetaDataRequest request = AddAssetMetaDataRequest.builder()
                .creator(anonymousUser)
                .status(RequestStatus.OPENED)
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
