package org.royllo.explorer.core.service.asset;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.AssetGroup;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * {@link AssetGroupService} implementation.
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
    public AssetGroupDTO addAssetGroup(@NotNull AssetGroupDTO newAssetGroup) {
        logger.info("Adding asset group {}", newAssetGroup);

        // Checking constraints.
        assert newAssetGroup.getId() == null : "Asset group id must be null";
        assert StringUtils.isNotBlank(newAssetGroup.getRawGroupKey()) : "Asset group key id is required";
        assert assetGroupRepository.findByRawGroupKey(newAssetGroup.getRawGroupKey()).isEmpty() : "Asset group key already registered";

        // Saving asset group.
        final AssetGroup assetGroupToCreate = ASSET_GROUP_MAPPER.mapToAssetGroup(newAssetGroup);
        final AssetGroupDTO assetGroupCreated = ASSET_GROUP_MAPPER.mapToAssetGroupDTO(assetGroupRepository.save(assetGroupToCreate));

        // We return the value.
        logger.info("Asset group created: {}", assetGroupCreated);
        return assetGroupCreated;
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
