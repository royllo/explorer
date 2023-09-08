package org.royllo.explorer.core.util.base;

import lombok.NonNull;
import org.mapstruct.factory.Mappers;
import org.royllo.explorer.core.util.mapper.AssetGroupMapper;
import org.royllo.explorer.core.util.mapper.AssetMapper;
import org.royllo.explorer.core.util.mapper.AssetStateMapper;
import org.royllo.explorer.core.util.mapper.BitcoinMapper;
import org.royllo.explorer.core.util.mapper.ProofFileMapper;
import org.royllo.explorer.core.util.mapper.RequestMapper;
import org.royllo.explorer.core.util.mapper.UniverseServerMapper;
import org.royllo.explorer.core.util.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Base.
 */
public abstract class Base {

    /** Asset group mapper. */
    protected static final AssetGroupMapper ASSET_GROUP_MAPPER = Mappers.getMapper(AssetGroupMapper.class);

    /** Asset mapper. */
    protected static final AssetMapper ASSET_MAPPER = Mappers.getMapper(AssetMapper.class);

    /** Asset state mapper. */
    protected static final AssetStateMapper ASSET_STATE_MAPPER = Mappers.getMapper(AssetStateMapper.class);

    /** User related mapper. */
    protected static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    /** Bitcoin related mapper. */
    protected static final BitcoinMapper BITCOIN_MAPPER = Mappers.getMapper(BitcoinMapper.class);

    /** Request related mapper. */
    protected static final RequestMapper REQUEST_MAPPER = Mappers.getMapper(RequestMapper.class);

    /** Proof file related mapper. */
    protected static final ProofFileMapper PROOF_FILE_MAPPER = Mappers.getMapper(ProofFileMapper.class);

    /** Universe server related mapper. */
    protected static final UniverseServerMapper UNIVERSE_SERVER_MAPPER = Mappers.getMapper(UniverseServerMapper.class);

    /** Logger. */
    protected final Logger logger = LoggerFactory.getLogger(getClass().getName());

    /**
     * Returns the sha256 value calculated with the parameter.
     *
     * @param value value
     * @return sha256 of value
     */
    protected static String sha256(@NonNull final String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available: " + e.getMessage());
        }
    }

}
