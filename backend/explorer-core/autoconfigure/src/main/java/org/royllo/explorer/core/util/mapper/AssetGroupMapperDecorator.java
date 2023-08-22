package org.royllo.explorer.core.util.mapper;

import org.royllo.explorer.core.domain.asset.AssetGroup;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;

/**
 * Asset group mapper decorator.
 * This is used to make the bidirectional link between asset and asset group.
 */
public abstract class AssetGroupMapperDecorator implements AssetGroupMapper {

    /** Delegate asset group mapper. */
    private final AssetGroupMapper delegate;

    /**
     * Constructor.
     *
     * @param newDelegate delegate
     */
    public AssetGroupMapperDecorator(AssetGroupMapper newDelegate) {
        this.delegate = newDelegate;
    }

    @Override
    public AssetGroupDTO mapToAssetGroupDTO(AssetGroup source) {
        AssetGroupDTO assetGroupDTO = delegate.mapToAssetGroupDTO(source);
        assetGroupDTO.getAssets().forEach(assetDTO -> assetDTO.setAssetGroup(assetGroupDTO));
        return assetGroupDTO;
    }

}
