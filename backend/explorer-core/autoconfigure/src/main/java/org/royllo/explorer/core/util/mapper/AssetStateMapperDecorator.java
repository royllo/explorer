package org.royllo.explorer.core.util.mapper;

import org.royllo.explorer.core.domain.asset.AssetState;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Asset state mapper decorator.
 * This is used to calculate the Royllo asset state id.
 */
public abstract class AssetStateMapperDecorator implements AssetStateMapper {

    /** Asset state field separator. */
    private static final String FIELD_SEPARATOR = "_";

    /** Delegate asset state mapper. */
    private final AssetStateMapper delegate;

    /**
     * Constructor.
     *
     * @param newDelegate delegate
     */
    public AssetStateMapperDecorator(AssetStateMapper newDelegate) {
        this.delegate = newDelegate;
    }

    @Override
    public AssetState mapToAssetState(AssetStateDTO source) {
        AssetState assetState = delegate.mapToAssetState(source);

        // If we are in an asset state creation, asset state id is null, so we calculate it.
        if (assetState.getAssetStateId() == null) {
            // We calculate the asset state id here.
            String uniqueValue = source.getAsset().getAssetId()
                    + FIELD_SEPARATOR + source.getAnchorOutpoint().getTxId() + ":" + source.getAnchorOutpoint().getVout()
                    + FIELD_SEPARATOR + source.getScriptKey();
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] digest = md.digest(uniqueValue.getBytes(StandardCharsets.UTF_8));
                assetState.setAssetStateId(DatatypeConverter.printHexBinary(digest).toLowerCase());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("SHA-256 is not available: " + e.getMessage());
            }
        }
        return assetState;
    }

}
