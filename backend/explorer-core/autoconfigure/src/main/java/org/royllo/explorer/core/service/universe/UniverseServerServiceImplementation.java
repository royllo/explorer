package org.royllo.explorer.core.service.universe;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.universe.UniverseServer;
import org.royllo.explorer.core.dto.universe.UniverseServerDTO;
import org.royllo.explorer.core.repository.universe.UniverseServerRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.exceptions.universe.UniverseServerCreationException;
import org.royllo.explorer.core.util.validator.ServerAddress;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER;

/**
 * {@link UniverseServerService} implementation.
 */
@Service
@Validated
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class UniverseServerServiceImplementation extends BaseService implements UniverseServerService {

    /** Universe server repository. */
    private final UniverseServerRepository universeServerRepository;

    @Override
    public UniverseServerDTO addUniverseServer(final @ServerAddress @NonNull String serverAddress) {
        logger.info("Adding universe server with address {}", serverAddress);

        // =============================================================================================================
        // Checking constraints.

        // Checking the server does not already exist in database.
        universeServerRepository.findByServerAddress(serverAddress).ifPresent(universeServer -> {
            throw new UniverseServerCreationException(serverAddress + " is already in our database");
        });

        // =============================================================================================================
        // Creating the universe server.

        final UniverseServer universeServer = universeServerRepository.save(UniverseServer.builder()
                .universeServerId(UUID.randomUUID().toString())
                .owner(ANONYMOUS_USER)
                .serverAddress(serverAddress.trim())
                .build());

        final UniverseServerDTO universeServerDTO = UNIVERSE_SERVER_MAPPER.mapToUniverseServerDTO(universeServer);
        logger.info("Universe server created with id {} : {}", universeServerDTO.getId(), universeServerDTO);
        return universeServerDTO;
    }

    @Override
    public Optional<UniverseServerDTO> getUniverseServerByServerAddress(final @NonNull String serverAddress) {
        logger.info("Getting universe server by its server address {}", serverAddress);

        return universeServerRepository.findByServerAddress(serverAddress)
                .map(UNIVERSE_SERVER_MAPPER::mapToUniverseServerDTO)
                .map(universeServerDTO -> {
                    logger.info("Universe server found: {}", universeServerDTO);
                    return universeServerDTO;
                })
                .or(() -> {
                    logger.info("Universe server {} not found", serverAddress);
                    return Optional.empty();
                });
    }

    @Override
    public List<UniverseServerDTO> getAllUniverseServers() {
        logger.info("Getting all universe servers");

        return universeServerRepository.findAll()
                .stream()
                .map(UNIVERSE_SERVER_MAPPER::mapToUniverseServerDTO)
                .toList();
    }

}
