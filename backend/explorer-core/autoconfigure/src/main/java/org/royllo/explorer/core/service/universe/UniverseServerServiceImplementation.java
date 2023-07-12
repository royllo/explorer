package org.royllo.explorer.core.service.universe;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.universe.UniverseServer;
import org.royllo.explorer.core.dto.universe.UniverseServerDTO;
import org.royllo.explorer.core.repository.universe.UniverseServerRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.exceptions.universe.UniverseServerCreationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.validator.ServerAddressValidator.HOSTNAME_PATTERN;
import static org.royllo.explorer.core.util.validator.ServerAddressValidator.IP_ADDRESS_PATTERN;

/**
 * Universe server service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class UniverseServerServiceImplementation extends BaseService implements UniverseServerService {

    /** Universe server repository. */
    private final UniverseServerRepository universeServerRepository;

    @Override
    public UniverseServerDTO addUniverseServer(final String serverAddress) {
        logger.info("Adding universe server with address {}", serverAddress);

        // Checking if the server address is not null.
        if (serverAddress == null) {
            logger.error("Server address cannot be null");
            throw new UniverseServerCreationException("Server address cannot be null");
        }

        // Checking if the server address is valid.
        if (!HOSTNAME_PATTERN.matcher(serverAddress.trim()).matches()
                && !IP_ADDRESS_PATTERN.matcher(serverAddress.trim()).matches()) {
            logger.error("Invalid server address {}", serverAddress.trim());
            throw new UniverseServerCreationException("Invalid server address " + serverAddress.trim());
        }

        // Checking the server does not already exist in database.
        if (universeServerRepository.findByServerAddress(serverAddress.trim()).isPresent()) {
            logger.error("Universe server ({}) is already in our database", serverAddress.trim());
            throw new UniverseServerCreationException(serverAddress.trim() + " is already in our database");
        }

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
    public Optional<UniverseServerDTO> getUniverseServerByServerAddress(@NonNull final String serverAddress) {
        logger.info("Getting universe server by its server address address {}", serverAddress);

        final Optional<UniverseServer> universeServer = universeServerRepository.findByServerAddress(serverAddress.trim());
        if (universeServer.isEmpty()) {
            logger.info("Universe server {} not found", serverAddress);
            return Optional.empty();
        } else {
            logger.info("Universe server {} found: {}", serverAddress, universeServer.get());
            return universeServer.map(UNIVERSE_SERVER_MAPPER::mapToUniverseServerDTO);
        }
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
