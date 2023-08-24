package org.royllo.explorer.core.service.asset;

import org.royllo.explorer.core.dto.asset.AssetStateDTO;

import java.util.Optional;

/**
 * {@link AssetStateService} implementation.
 */
public class AssetStateServiceImplementation implements AssetStateService {

    @Override
    public AssetStateDTO addAssetState(AssetStateDTO newAssetState) {
        return null;
    }

    @Override
    public Optional<AssetStateDTO> getAssetStateByAnchorOutpoint(String anchorOutpoint) {
        return Optional.empty();
    }

}
