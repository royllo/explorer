package org.royllo.explorer.core.service.asset;

import io.micrometer.common.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.domain.asset.AssetState;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_DTO;

/**
 * {@link AssetStateService} implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class AssetStateServiceImplementation extends BaseService implements AssetStateService {

    /** Assert repository. */
    private final AssetRepository assetRepository;

    /** Assert state repository. */
    private final AssetStateRepository assetStateRepository;

    /** Asset service. */
    private final AssetService assetService;

    /** Bitcoin service. */
    private final BitcoinService bitcoinService;

    @Override
    public AssetStateDTO addAssetState(@NonNull AssetStateDTO newAssetState) {
        logger.info("Adding asset state {}", newAssetState);

        // Checking constraints.
        assert newAssetState.getId() == null : "Asset state already exists";
        assert newAssetState.getAsset() != null : "Linked asset is required";
        assert StringUtils.isNotBlank(newAssetState.getAsset().getAssetId()) : "Asset id is required";
        assert newAssetState.getAnchorOutpoint() != null : "Bitcoin transaction is required";
        // TODO Test this constraint.
        assert newAssetState.getAssetStateId() == null : "Asset state id should not be set, it will be calculated";

        // =============================================================================================================
        // We update and save the asset state.
        final AssetState assetStateToCreate = ASSET_STATE_MAPPER.mapToAssetState(newAssetState);

        // We check that the asset state does not already exist.
        assert assetStateRepository.findByAssetStateId(assetStateToCreate.getAssetStateId()).isEmpty() : "Asset state already exists";

        // Setting the creator.
        assetStateToCreate.setCreator(USER_MAPPER.mapToUser(ANONYMOUS_USER_DTO));

        // Setting the asset of this asset state. We check in database if we can find it with its assetId. If not, we set it.
        final Optional<Asset> asset = assetRepository.findByAssetId(assetStateToCreate.getAsset().getAssetId());
        if (asset.isPresent()) {
            // We set the asset of the asset state to create.
            assetStateToCreate.setAsset(asset.get());
        } else {
            // We create the asset and we set it .
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
        final AssetStateDTO assetStateCreated = ASSET_STATE_MAPPER.mapToAsseStateDTO(assetStateRepository.save(assetStateToCreate));
        logger.info("Asset state created with id {} : {}", assetStateCreated.getId(), assetStateCreated);
        return assetStateCreated;
    }

    @Override
    public Optional<AssetStateDTO> getAssetStateByAssetStateId(String assetStateId) {
        logger.info("Getting asset state with asset state id {}", assetStateId);

        final Optional<AssetState> assetState = assetStateRepository.findByAssetStateId(assetStateId);
        if (assetState.isEmpty()) {
            logger.info("Asset state with asset state id {} not found", assetStateId);
            return Optional.empty();
        } else {
            logger.info("Asset state with asset state id {} found: {}", assetStateId, assetState.get());
            return assetState.map(ASSET_STATE_MAPPER::mapToAsseStateDTO);
        }
    }

}
