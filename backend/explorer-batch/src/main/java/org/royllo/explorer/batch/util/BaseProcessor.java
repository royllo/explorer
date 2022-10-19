package org.royllo.explorer.batch.util;

import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.util.base.Base;

/**
 * Base class for processor.
 *
 * @param <T> request type
 */
public abstract class BaseProcessor<T extends RequestDTO> extends Base {

    /** Request to process. */
    protected T requestToProcess;

    /**
     * This method process the request and returns the result.
     *
     * @param newRequestToProcess request to process
     * @return result of the process
     */
    public T process(final T newRequestToProcess) {
        requestToProcess = newRequestToProcess;
        processRequest(newRequestToProcess);
        return requestToProcess;
    }

    /**
     * Each processor have to implement this method.
     *
     * @param newRequestToProcess request to process
     * @return request processed
     */
    protected abstract T processRequest(T newRequestToProcess);

}
