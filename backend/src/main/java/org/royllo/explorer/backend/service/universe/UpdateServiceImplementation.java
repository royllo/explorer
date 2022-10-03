package org.royllo.explorer.backend.service.universe;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.backend.dto.universe.UpdateDTO;
import org.royllo.explorer.backend.repository.universe.UpdateRepository;
import org.royllo.explorer.backend.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Update service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class UpdateServiceImplementation extends BaseService implements UpdateService {

    /** Update request repository. */
    private final UpdateRepository updateRepository;

    @Override
    public Optional<UpdateDTO> getUpdateDTO(final long id) {
        return updateRepository.findById(id).map(UPDATE_REQUEST_MAPPER::mapToUpdateRequestDTO);
    }

}
