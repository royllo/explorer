package org.royllo.explorer.core.service.asset;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.AssetGroup;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * {@link AssetGroupService} implementation.
 */
@Service
@Validated
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class AssetGroupServiceImplementation extends BaseService implements AssetGroupService {

    /** Asset group repository. */
    private final AssetGroupRepository assetGroupRepository;

    @Override
    public AssetGroupDTO addAssetGroup(final @Valid @NonNull AssetGroupDTO newAssetGroup) {
        logger.info("Adding a new asset group {}", newAssetGroup);

        // =============================================================================================================
        // Checking constraints.

        // Asset group id already exists.
        assetGroupRepository.findByAssetGroupId(newAssetGroup.getAssetGroupId()).ifPresent(assetGroup -> {
            throw new IllegalArgumentException("Asset group id already registered");
        });

        // =============================================================================================================
        // Saving asset group.
        final AssetGroup assetGroupToCreate = ASSET_GROUP_MAPPER.mapToAssetGroup(newAssetGroup);
        assetGroupToCreate.setId(null);
        final AssetGroupDTO assetGroupCreated = ASSET_GROUP_MAPPER.mapToAssetGroupDTO(assetGroupRepository.save(assetGroupToCreate));
        logger.info("Asset group created: {}", assetGroupCreated);
        return assetGroupCreated;
    }

    @Override
    public Optional<AssetGroupDTO> getAssetGroupByAssetGroupId(final String assetGroupId) {
        logger.info("Getting asset group with asset group id: {}", assetGroupId);

        return assetGroupRepository.findByAssetGroupId(assetGroupId)
                .map(ASSET_GROUP_MAPPER::mapToAssetGroupDTO)
                .map(assetGroup -> {
                    logger.info("Asset group with asset group id {} found: {}", assetGroupId, assetGroup);
                    return assetGroup;
                })
                .or(() -> {
                    logger.info("Asset group with asset group id {} not found", assetGroupId);
                    return Optional.empty();
                });
    }

}
