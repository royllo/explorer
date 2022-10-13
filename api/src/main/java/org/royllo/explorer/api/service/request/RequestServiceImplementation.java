package org.royllo.explorer.api.service.request;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.domain.request.AddAssetMetaDataRequest;
import org.royllo.explorer.api.domain.request.AddAssetRequest;
import org.royllo.explorer.api.domain.user.User;
import org.royllo.explorer.api.dto.request.AddAssetMetaDataRequestDTO;
import org.royllo.explorer.api.dto.request.AddAssetRequestDTO;
import org.royllo.explorer.api.dto.request.RequestDTO;
import org.royllo.explorer.api.repository.request.RequestRepository;
import org.royllo.explorer.api.repository.user.UserRepository;
import org.royllo.explorer.api.util.base.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static org.royllo.explorer.api.util.constants.UserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.api.util.enums.RequestStatus.OPENED;

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
    public List<RequestDTO> getOpenedRequests() {
        return requestRepository.findByStatusOrderById(OPENED)
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
                .status(OPENED)
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
