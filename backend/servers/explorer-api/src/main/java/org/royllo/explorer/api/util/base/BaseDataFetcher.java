package org.royllo.explorer.api.util.base;

import com.netflix.graphql.dgs.exceptions.DgsInvalidInputArgumentException;
import org.royllo.explorer.core.util.base.Base;

import java.util.Objects;

import static org.royllo.explorer.api.configuration.APIConfiguration.DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.api.configuration.APIConfiguration.FIRST_PAGE;
import static org.royllo.explorer.api.configuration.APIConfiguration.MAXIMUM_PAGE_SIZE;

/**
 * Base data fetcher.
 */
public class BaseDataFetcher extends Base {

    /**
     * Returns a cleaned page number (null and min check).
     *
     * @param pageNumber page number
     * @return cleaned page number
     */
    protected int getCleanedPageNumber(final Integer pageNumber) {
        final int finalPageNumber = Objects.requireNonNullElse(pageNumber, FIRST_PAGE);
        if (finalPageNumber < FIRST_PAGE) {
            throw new DgsInvalidInputArgumentException("Page number starts at page " + FIRST_PAGE, null);
        }
        return finalPageNumber;
    }

    /**
     * Returns a cleaned page size (null and max check).
     *
     * @param pageSize page size
     * @return cleaned page size
     */
    protected int getCleanedPageSize(final Integer pageSize) {
        final int finalPageSize = Objects.requireNonNullElse(pageSize, DEFAULT_PAGE_SIZE);
        if (finalPageSize > MAXIMUM_PAGE_SIZE) {
            throw new DgsInvalidInputArgumentException("Page size can't be superior to " + MAXIMUM_PAGE_SIZE, null);
        }
        return finalPageSize;
    }

}
