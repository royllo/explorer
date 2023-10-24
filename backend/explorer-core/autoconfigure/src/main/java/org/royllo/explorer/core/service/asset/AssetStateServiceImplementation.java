package org.royllo.explorer.core.service.asset;

import io.micrometer.common.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.domain.asset.AssetGroup;
import org.royllo.explorer.core.domain.asset.AssetState;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;

/**
 * {@link AssetStateService} implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class AssetStateServiceImplementation extends BaseService implements AssetStateService {

    /** Search parameter for asset id. */
    public static final String SEARCH_PARAMETER_ASSET_ID = "assetId:";

    /** Assert group repository. */
    private final AssetGroupRepository assetGroupRepository;

    /** Assert repository. */
    private final AssetRepository assetRepository;

    /** Assert state repository. */
    private final AssetStateRepository assetStateRepository;

    /** Asset group service. */
    private final AssetGroupService assetGroupService;

    /** Asset service. */
    private final AssetService assetService;

    /** Bitcoin service. */
    private final BitcoinService bitcoinService;

    @Override
    public Page<AssetStateDTO> queryAssetStates(@NonNull final String query,
                                                final int page,
                                                final int pageSize) {
        logger.info("Searching for {}", query);

        // For the moment, we only support a "assetId:value" query.
        assert query.contains(SEARCH_PARAMETER_ASSET_ID) : "Only assetId:value query is supported";

        // Checking constraints.
        assert page >= 1 : "Page number starts at page 1";

        // Results that will be returned.
        Page<AssetStateDTO> results = Page.empty();

        // If the query parameter contains "assetId:value", retrieve the value
        // and search for all the asset states of this asset.
        if (query.contains(SEARCH_PARAMETER_ASSET_ID)) {
            final String assetId = query.trim().split(SEARCH_PARAMETER_ASSET_ID)[1];
            results = assetStateRepository.findByAsset_AssetId(assetId, PageRequest.of(page - 1, pageSize))
                    .map(ASSET_STATE_MAPPER::mapToAssetStateDTO);
        }

        return results;
    }

    @Override
    public AssetStateDTO addAssetState(final @NonNull AssetStateDTO newAssetState) {
        logger.info("Adding asset state {}", newAssetState);

        // Checking constraints.
        assert newAssetState.getId() == null : "Asset state already exists";
        assert newAssetState.getAsset() != null : "Linked asset is required";
        assert StringUtils.isNotBlank(newAssetState.getAsset().getAssetId()) : "Asset id is required";
        assert newAssetState.getAnchorOutpoint() != null : "Bitcoin transaction is required";

        // =============================================================================================================
        // We update and save the asset state.
        final AssetState assetStateToCreate = ASSET_STATE_MAPPER.mapToAssetState(newAssetState);
        assetStateToCreate.setCreator(ANONYMOUS_USER);

        // We check that the asset state does not already exist.
        assert assetStateRepository.findByAssetStateId(assetStateToCreate.getAssetStateId()).isEmpty() : "Asset state already exists";

        // =============================================================================================================
        // Setting the asset of this asset state. We check in database if we can find it with its assetId. If not, we set it.
        final Optional<Asset> asset = assetRepository.findByAssetId(assetStateToCreate.getAsset().getAssetId());
        if (asset.isPresent()) {
            // We set the asset of the asset state to create.
            logger.info("Asset {} already exists with id : {}", asset.get().getAssetId(), asset.get().getId());
            assetStateToCreate.setAsset(asset.get());
        } else {
            logger.info("Asset {} does not exists", assetStateToCreate.getAsset().getAssetId());
            // We create the asset, and we set it (we also check for the group asset).

            // But before, we are setting the asset group of this asset state. Whe check in database if we can find it with its raw group key. If not we set it.
            if (assetStateToCreate.getAsset().getAssetGroup() != null && newAssetState.getAsset().getAssetGroup().getTweakedGroupKey() != null) {
                final Optional<AssetGroup> assetGroup = assetGroupRepository.findByTweakedGroupKey(assetStateToCreate.getAsset().getAssetGroup().getTweakedGroupKey());
                if (assetGroup.isPresent()) {
                    // We set the asset group of asset.
                    logger.info("Asset group {} already exists with id : {}", assetGroup.get().getTweakedGroupKey(), assetGroup.get().getId());
                    newAssetState.getAsset().setAssetGroup(ASSET_GROUP_MAPPER.mapToAssetGroupDTO(assetGroup.get()));
                } else {
                    // We create the asset group and set it.
                    final AssetGroupDTO assetGroupCreated = assetGroupService.addAssetGroup(newAssetState.getAsset().getAssetGroup());
                    logger.info("Asset group {} created with id : {}", assetGroupCreated.getTweakedGroupKey(), assetGroupCreated.getId());
                    newAssetState.getAsset().setAssetGroup(assetGroupCreated);
                }
            }

            // We create the asset.
            final AssetDTO assetCreated = assetService.addAsset(newAssetState.getAsset());
            assetStateToCreate.setAsset(ASSET_MAPPER.mapToAsset(assetCreated));
        }

        // Setting the bitcoin transaction output ID if not already set.
        if (newAssetState.getAnchorOutpoint().getId() == null) {
            final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(newAssetState.getAnchorOutpoint().getTxId(), newAssetState.getAnchorOutpoint().getVout());
            assert bto.isPresent() : "UTXO " + newAssetState.getAnchorOutpoint().getTxId() + "/" + newAssetState.getAnchorOutpoint().getVout() + " Not found";
            assetStateToCreate.setAnchorOutpoint(BITCOIN_MAPPER.mapToBitcoinTransactionOutput(bto.get()));
        }

        // We save and return the value.
        final AssetStateDTO assetStateCreated = ASSET_STATE_MAPPER.mapToAssetStateDTO(assetStateRepository.save(assetStateToCreate));
        logger.info("Asset state created with id {} : {}", assetStateCreated.getId(), assetStateCreated);
        return assetStateCreated;
    }

    @Override
    public Optional<AssetStateDTO> getAssetStateByAssetStateId(final String assetStateId) {
        logger.info("Getting asset state with asset state id {}", assetStateId);

        final Optional<AssetState> assetState = assetStateRepository.findByAssetStateId(assetStateId);
        if (assetState.isEmpty()) {
            logger.info("Asset state with asset state id {} not found", assetStateId);
            return Optional.empty();
        } else {
            logger.info("Asset state with asset state id {} found: {}", assetStateId, assetState.get());
            return assetState.map(ASSET_STATE_MAPPER::mapToAssetStateDTO);
        }
    }

}
