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

import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;

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
    public AssetGroupDTO addAssetGroup(final @NotNull AssetGroupDTO newAssetGroup) {
        logger.info("Adding asset group {}", newAssetGroup);

        // Checking constraints.
        assert newAssetGroup.getId() == null : "Asset group id must be null";
        assert StringUtils.isNotBlank(newAssetGroup.getAssetGroupId()) : "Asset group id is required";
        assert StringUtils.isNotBlank(newAssetGroup.getTweakedGroupKey()) : "Tweaked Asset group key is required";
        assert assetGroupRepository.findByAssetGroupId(newAssetGroup.getAssetGroupId()).isEmpty() : "Asset group id already registered";

        // Saving asset group.
        final AssetGroup assetGroupToCreate = ASSET_GROUP_MAPPER.mapToAssetGroup(newAssetGroup);
        assetGroupToCreate.setCreator(ANONYMOUS_USER);
        final AssetGroupDTO assetGroupCreated = ASSET_GROUP_MAPPER.mapToAssetGroupDTO(assetGroupRepository.save(assetGroupToCreate));

        // We return the value.
        logger.info("Asset group created: {}", assetGroupCreated);
        return assetGroupCreated;
    }

    @Override
    public Optional<AssetGroupDTO> getAssetGroupByAssetGroupId(final String assetGroupId) {
        logger.info("Getting asset group with asset group id: {}", assetGroupId);

        final Optional<AssetGroup> assetGroup = assetGroupRepository.findByAssetGroupId(assetGroupId);
        if (assetGroup.isEmpty()) {
            logger.info("Asset group with asset group id {} not found", assetGroupId);
            return Optional.empty();
        } else {
            logger.info("Asset group with asset group id {} found: {}", assetGroupId, assetGroup.get());
            return assetGroup.map(ASSET_GROUP_MAPPER::mapToAssetGroupDTO);
        }
    }

}
