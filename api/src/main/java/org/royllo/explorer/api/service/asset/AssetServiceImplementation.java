package org.royllo.explorer.api.service.asset;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.dto.asset.AssetDTO;
import org.royllo.explorer.api.repository.asset.AssetRepository;
import org.royllo.explorer.api.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Asset service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class AssetServiceImplementation extends BaseService implements AssetService {

    /** Assert repository. */
    private final AssetRepository assetRepository;

    @Override
    public Optional<AssetDTO> getAsset(final long id) {
        return assetRepository.findById(id).map(ASSET_MAPPER::mapToAssetDTO);
    }

    @Override
    public Optional<AssetDTO> getAssetByAssetId(final String assetId) {
        return assetRepository.findByAssetId(assetId).map(ASSET_MAPPER::mapToAssetDTO);
    }

}
