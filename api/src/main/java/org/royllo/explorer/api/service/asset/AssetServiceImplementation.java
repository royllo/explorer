package org.royllo.explorer.api.service.asset;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.domain.asset.Asset;
import org.royllo.explorer.api.dto.asset.AssetDTO;
import org.royllo.explorer.api.repository.asset.AssetRepository;
import org.royllo.explorer.api.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<AssetDTO> queryAssets(final String value) {
        final Optional<Asset> firstSearch = assetRepository.findByAssetId(value);
        if (firstSearch.isPresent()) {
            // We found an asset with the corresponding assetId, we return it.
            return firstSearch.stream()
                    .map(ASSET_MAPPER::mapToAssetDTO)
                    .toList();
        } else {
            // We search all assets where "value" is in the name.
            return assetRepository.findByNameContainsIgnoreCase(value)
                    .stream()
                    .map(ASSET_MAPPER::mapToAssetDTO)
                    .toList();
        }
    }

    @Override
    public Optional<AssetDTO> getAsset(final long id) {
        return assetRepository.findById(id).map(ASSET_MAPPER::mapToAssetDTO);
    }

    @Override
    public Optional<AssetDTO> getAssetByAssetId(final String assetId) {
        return assetRepository.findByAssetId(assetId).map(ASSET_MAPPER::mapToAssetDTO);
    }

}
