package org.royllo.explorer.core.service.asset;

import io.micrometer.common.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.ASSET_ID_SIZE;

/**
 * {@link AssetService} implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class AssetServiceImplementation extends BaseService implements AssetService {

    /** Assert repository. */
    private final AssetRepository assetRepository;

    /** Asset group service. */
    private final AssetGroupService assetGroupService;

    /** Bitcoin service. */
    private final BitcoinService bitcoinService;

    @Override
    public Page<AssetDTO> queryAssets(@NonNull final String query,
                                      final int page,
                                      final int pageSize) {
        logger.info("Searching for {}", query);

        // Checking constraints.
        assert page >= 1 : "Page number starts at page 1";

        // Results.
        Page<AssetDTO> results = Page.empty();

        // Search if the "query" parameter is a tweaked group key (asset group) > returns all assets of this asset group.
        final Optional<AssetGroupDTO> assetGroup = assetGroupService.getAssetGroupByAssetGroupId(query);
        if (assetGroup.isPresent()) {
            results = getAssetsByAssetGroupId(assetGroup.get().getAssetGroupId(), page, pageSize);
        }

        // If nothing found, we search if there is this asset in database with this exact asset id.
        if (results.isEmpty()) {
            final Optional<Asset> assetIdSearch = assetRepository.findByAssetId(query);
            if (assetIdSearch.isPresent()) {
                results = new PageImpl<>(assetIdSearch.stream()
                        .map(ASSET_MAPPER::mapToAssetDTO)
                        .toList());
            }
        }

        // If nothing found, we will search on asset id alias.
        if (results.isEmpty()) {
            final Optional<Asset> assetIdAliasSearch = assetRepository.findByAssetIdAlias(query);
            if (assetIdAliasSearch.isPresent()) {
                results = new PageImpl<>(assetIdAliasSearch.stream()
                        .map(ASSET_MAPPER::mapToAssetDTO)
                        .toList());
            }
        }

        // If nothing found, we search if there is an asset with "query" parameter as complete or partial asset name.
        if (results.isEmpty()) {
            results = assetRepository.findByNameContainsIgnoreCaseOrderByName(query,
                    PageRequest.of(page - 1, pageSize)).map(ASSET_MAPPER::mapToAssetDTO);
        }

        // Displaying logs and return results.
        if (results.isEmpty()) {
            logger.info("For '{}', there is no results", query);
        } else {
            logger.info("For '{}', {} result(s) with assets id(s): {}",
                    query,
                    results.getTotalElements(),
                    results.stream()
                            .map(AssetDTO::getId)
                            .map(Objects::toString)
                            .collect(joining(", ")));
        }
        return results;
    }

    @Override
    public AssetDTO addAsset(@NonNull final AssetDTO newAsset) {
        logger.info("Adding asset {}", newAsset);

        // Checking constraints.
        assert newAsset.getId() == null : "Asset already exists";
        assert newAsset.getGenesisPoint() != null : "Bitcoin transaction is required";
        assert assetRepository.findByAssetId(newAsset.getAssetId()).isEmpty() : newAsset.getAssetId() + " already registered";

        // =============================================================================================================
        // We update and save the asset.
        final Asset assetToCreate = ASSET_MAPPER.mapToAsset(newAsset);
        assetToCreate.setCreator(ANONYMOUS_USER);

        // Setting the bitcoin transaction output ID if not already set.
        if (newAsset.getGenesisPoint().getId() == null) {
            final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(newAsset.getGenesisPoint().getTxId(), newAsset.getGenesisPoint().getVout());
            assert bto.isPresent() : "UTXO " + newAsset.getGenesisPoint().getTxId() + "/" + newAsset.getGenesisPoint().getVout() + " Not found";
            assetToCreate.setGenesisPoint(BITCOIN_MAPPER.mapToBitcoinTransactionOutput(bto.get()));
        }

        // We check if an asset group is set.
        if (newAsset.getAssetGroup() != null && !StringUtils.isEmpty(newAsset.getAssetGroup().getTweakedGroupKey())) {
            // If the asset exists in database, we retrieve and set it.
            final Optional<AssetGroupDTO> assetGroup = assetGroupService.getAssetGroupByAssetGroupId(newAsset.getAssetGroup().getAssetGroupId());
            if (assetGroup.isPresent()) {
                assetToCreate.setAssetGroup(ASSET_GROUP_MAPPER.mapToAssetGroup(assetGroup.get()));
            } else {
                // If the asset group does not exist in database, we create it and set it.
                final AssetGroupDTO assetGroupCreated = assetGroupService.addAssetGroup(newAsset.getAssetGroup());
                assetToCreate.setAssetGroup(ASSET_GROUP_MAPPER.mapToAssetGroup(assetGroupCreated));
            }
        }

        // We save and return the value.
        final AssetDTO assetCreated = ASSET_MAPPER.mapToAssetDTO(assetRepository.save(assetToCreate));
        logger.info("Asset created with id {} : {}", assetCreated.getId(), assetCreated);
        return assetCreated;
    }

    @Override
    public boolean addMetaDataToAsset(final String assetId, final String metadata) {
        // We check that the asset exists.
        assert assetRepository.findByAssetId(assetId).isPresent() : assetId + " not found";

        // We get the content of the file.

        // We calculate the hash of the file and compare it with the metaDataHash of the asset in database.

        // We use tika to extract the file type of filename.

        // We rename the file to assetId.fileType and set the filename on the asset object.

        return true;
    }

    @Override
    public Optional<AssetDTO> getAsset(final long id) {
        logger.info("Getting asset with id {}", id);

        final Optional<Asset> asset = assetRepository.findById(id);
        if (asset.isEmpty()) {
            logger.info("Asset with id {} not found", id);
            return Optional.empty();
        } else {
            logger.info("Asset with id {} found: {}", id, asset.get());
            return asset.map(ASSET_MAPPER::mapToAssetDTO);
        }
    }

    @Override
    public Optional<AssetDTO> getAssetByAssetId(final String assetId) {
        logger.info("Getting asset with assetId {}", assetId);

        if (assetId == null) {
            return Optional.empty();
        }

        if (assetId.length() == ASSET_ID_SIZE) {
            // We received an asset id (we know it because of the size).
            Optional<Asset> asset = assetRepository.findByAssetId(assetId.trim());
            if (asset.isPresent()) {
                logger.info("Asset with assetId {} found: {}", assetId, asset.get());
                return asset.map(ASSET_MAPPER::mapToAssetDTO);
            } else {
                logger.info("Asset with assetId {} not found", assetId);
                return Optional.empty();
            }
        } else {
            // it's not an asset id (the size is not the good one), so we search on asset id alias.
            Optional<Asset> asset = assetRepository.findByAssetIdAlias(assetId.trim());
            if (asset.isPresent()) {
                logger.info("Asset with assetIdAlias {} found: {}", assetId, asset.get());
                return asset.map(ASSET_MAPPER::mapToAssetDTO);
            } else {
                logger.info("Asset with assetIdAlias {} not found", assetId);
                return Optional.empty();
            }
        }

    }

    @Override
    public Page<AssetDTO> getAssetsByAssetGroupId(final String assetGroupId, final int page, final int pageSize) {
        logger.info("Getting assets with asset group id {}", assetGroupId);

        // Checking constraints.
        assert page >= 1 : "Page number starts at page 1";

        // If asset group id is null, we return an empty page.
        if (assetGroupId == null) {
            return Page.empty();
        }

        return assetRepository.findByAssetGroup_AssetGroupId(assetGroupId.trim(), PageRequest.of(page - 1, pageSize))
                .map(ASSET_MAPPER::mapToAssetDTO);
    }

}
