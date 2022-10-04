package org.royllo.explorer.api.service.universe;

import org.royllo.explorer.api.dto.universe.UpdateDTO;

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
