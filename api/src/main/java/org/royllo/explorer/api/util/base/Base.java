package org.royllo.explorer.api.util.base;

import org.mapstruct.factory.Mappers;
import org.royllo.explorer.api.util.mapper.AssetMapper;
import org.royllo.explorer.api.util.mapper.BitcoinMapper;
import org.royllo.explorer.api.util.mapper.RequestMapper;
import org.royllo.explorer.api.util.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base.
 */
public abstract class Base {

    /** Logger. */
    protected final Logger logger = LoggerFactory.getLogger(getClass().getName());

    /** Asset related mapper. */
    protected static final AssetMapper ASSET_MAPPER = Mappers.getMapper(AssetMapper.class);

    /** User related mapper. */
    protected static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    /** Bitcoin related mapper. */
    protected static final BitcoinMapper BITCOIN_MAPPER = Mappers.getMapper(BitcoinMapper.class);

    /** Request related mapper. */
    protected static final RequestMapper REQUEST_MAPPER = Mappers.getMapper(RequestMapper.class);

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
