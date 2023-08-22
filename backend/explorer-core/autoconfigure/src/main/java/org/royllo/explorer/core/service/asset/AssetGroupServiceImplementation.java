package org.royllo.explorer.core.service.asset;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.AssetGroup;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Asset group service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class AssetGroupServiceImplementation extends BaseService implements AssetGroupService {

    /**
     * Asset group repository.
     */
    private final AssetGroupRepository assetGroupRepository;

    @Override
    public AssetGroupDTO addAssetGroup(AssetGroupDTO newAssetGroup) {
        return null;
    }

    @Override
    public Optional<AssetGroupDTO> getAssetGroupByRawGroupKey(String rawGroupKey) {
        logger.info("Getting asset group with raw group key {}", rawGroupKey);

        final Optional<AssetGroup> assetGroup = assetGroupRepository.findByRawGroupKey(rawGroupKey);
        if (assetGroup.isEmpty()) {
            logger.info("Asset group with raw group key {} not found", assetGroup);
            return Optional.empty();
        } else {
            logger.info("Asset group with raw group key {} found: {}", rawGroupKey, assetGroup.get());
            return assetGroup.map(ASSET_GROUP_MAPPER::mapToAssetGroupDTO);
        }
    }

}
