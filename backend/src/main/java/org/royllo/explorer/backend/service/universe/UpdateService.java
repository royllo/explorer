package org.royllo.explorer.backend.service.universe;

import org.royllo.explorer.backend.dto.universe.UpdateDTO;

import java.util.Optional;

/**
 * Update service.
 */
public interface UpdateService {

    /**
     * Get an update.
     *
     * @param id update id
     * @return update
     */
    Optional<UpdateDTO> getUpdateDTO(long id);

}
