package org.royllo.explorer.core.util.mapper;

import io.micrometer.common.util.StringUtils;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;

import java.util.Random;

/**
 * Asset mapper decorator.
 * This is used to calculate the asset id alias if not set.
 */
public abstract class AssetMapperDecorator implements AssetMapper {

    /** Characters to choose from when generating an asset id alias. */
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /** The length of the asset id alias. */
    public static final int ALIAS_LENGTH = 8;

    /** Random number generator. */
    private final Random random = new Random();

    /** Delegate asset mapper. */
    private final AssetMapper delegate;

    /**
     * Constructor.
     *
     * @param newDelegate delegate
     */
    public AssetMapperDecorator(final AssetMapper newDelegate) {
        this.delegate = newDelegate;
    }

    @Override
    public final Asset mapToAsset(final AssetDTO source) {
        Asset asset = delegate.mapToAsset(source);
        if (asset != null && StringUtils.isEmpty(asset.getAssetIdAlias())) {
            asset.setAssetIdAlias(getAssetIdAlias());
        }
        return asset;
    }

    @Override
    public final AssetDTO mapToAssetDTO(final Asset source) {
        return delegate.mapToAssetDTO(source);
    }

    @Override
    public final AssetGroupDTO mapToAssetGroupDTO(final DecodedProofResponse.DecodedProof.Asset.AssetGroup source) {
        return delegate.mapToAssetGroupDTO(source);
    }

    @Override
    public final AssetDTO mapToAssetDTO(final DecodedProofResponse.DecodedProof source) {
        return delegate.mapToAssetDTO(source);
    }

    /**
     * Returns a random alias.
     * @return random alias
     */
    private String getAssetIdAlias() {
        StringBuilder assetIdAlias = new StringBuilder(ALIAS_LENGTH);

        for (int i = 0; i < ALIAS_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            assetIdAlias.append(CHARACTERS.charAt(index));
        }

        return assetIdAlias.toString();
    }

}