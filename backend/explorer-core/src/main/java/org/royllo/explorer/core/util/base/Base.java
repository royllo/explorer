package org.royllo.explorer.core.util.base;

import org.mapstruct.factory.Mappers;
import org.royllo.explorer.core.util.mapper.AssetMapper;
import org.royllo.explorer.core.util.mapper.BitcoinMapper;
import org.royllo.explorer.core.util.mapper.ProofMapper;
import org.royllo.explorer.core.util.mapper.RequestMapper;
import org.royllo.explorer.core.util.mapper.UserMapper;
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

    /** Proof related mapper. */
    protected static final ProofMapper PROOF_MAPPER = Mappers.getMapper(ProofMapper.class);

}
