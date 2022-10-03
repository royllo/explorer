package org.royllo.explorer.backend.util.base;

import org.mapstruct.factory.Mappers;
import org.royllo.explorer.backend.util.mapper.BitcoinMapper;
import org.royllo.explorer.backend.util.mapper.UpdateMapper;
import org.royllo.explorer.backend.util.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base.
 */
public abstract class Base {

    /** Logger. */
    protected final Logger logger = LoggerFactory.getLogger(getClass().getName());

    /** User related mapper. */
    protected static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    /** Bitcoin related mapper. */
    protected static final BitcoinMapper BITCOIN_MAPPER = Mappers.getMapper(BitcoinMapper.class);

    /** Update request mapper. */
    protected static final UpdateMapper UPDATE_REQUEST_MAPPER = Mappers.getMapper(UpdateMapper.class);

    /**
     * Returns a formatted ID to display.
     *
     * @param id id to format
     * @return formatted id
     */
    protected static String getFormattedId(final long id) {
        return String.format("%09d", id);
    }

}
