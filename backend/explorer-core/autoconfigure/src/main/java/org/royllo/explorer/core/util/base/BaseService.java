package org.royllo.explorer.core.util.base;

import org.springframework.data.domain.PageRequest;

/**
 * Base service.
 */
public class BaseService extends Base {

    /**
     * Get page request.
     *
     * @param pageNumber page number (starts at 1)
     * @param pageSize   page size
     * @return page request
     */
    protected PageRequest getPageRequest(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber - 1, pageSize);
    }

}
