package org.royllo.explorer.batch.service;

import org.royllo.explorer.core.dto.request.RequestDTO;

/**
 * Request processor service.
 */
public interface RequestProcessorService {

    /**
     * Process request.
     *
     * @param requestToProcess request to process
     * @return result
     */
    RequestDTO processRequest(RequestDTO requestToProcess);

}
