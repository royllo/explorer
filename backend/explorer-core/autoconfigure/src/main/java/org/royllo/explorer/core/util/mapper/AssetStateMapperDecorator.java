package org.royllo.explorer.core.util.mapper;

import lombok.NonNull;
import org.royllo.explorer.core.domain.asset.AssetState;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Asset state mapper decorator.
 * This is used to calculate the Royllo asset state id.
 */
public abstract class AssetStateMapperDecorator implements AssetStateMapper {

    /** Asset state id field separator. */
    private static final String FIELD_SEPARATOR = "_";

    /** Delegate asset state mapper. */
    private final AssetStateMapper delegate;

    /**
     * Constructor.
     *
     * @param newDelegate delegate
     */
    public AssetStateMapperDecorator(final AssetStateMapper newDelegate) {
        this.delegate = newDelegate;
    }

    @Override
    public final AssetState mapToAssetState(final AssetStateDTO source) {
        AssetState assetState = delegate.mapToAssetState(source);
        if (assetState.getAssetStateId() == null) {
            final String newAssetId = calculateAssetStateId(source.getAsset().getAssetId(),
                    source.getAnchorOutpoint().getTxId(),
                    source.getAnchorOutpoint().getVout(),
                    source.getScriptKey());
            assetState.setAssetStateId(newAssetId);
        }
        return assetState;
    }

    @Override
    public final AssetStateDTO mapToAssetStateDTO(final DecodedProofResponse.DecodedProof source) {
        AssetStateDTO assetStateDTO = delegate.mapToAssetStateDTO(source);
        if (assetStateDTO.getAssetStateId() == null) {
            final String newAssetId = calculateAssetStateId(source.getAsset().getAssetGenesis().getAssetId(),
                    assetStateDTO.getAnchorOutpoint().getTxId(),
                    assetStateDTO.getAnchorOutpoint().getVout(),
                    assetStateDTO.getScriptKey());
            assetStateDTO.setAssetStateId(newAssetId);
        }
        return assetStateDTO;
    }

    private String calculateAssetStateId(@NonNull final String assetId,
                                         @NonNull final String outpointTxId,
                                         final int outpointVout,
                                         @NonNull final String scriptKey) {
        // If we are in an asset state creation, asset state id is null, so we calculate it.
        // We calculate the asset state id here.
        String uniqueValue = assetId
                + FIELD_SEPARATOR + outpointTxId + ":" + outpointVout
                + FIELD_SEPARATOR + scriptKey;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(uniqueValue.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available: " + e.getMessage());
        }
    }

}
