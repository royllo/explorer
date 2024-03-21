package org.royllo.explorer.core.service.asset;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.AssetState;
import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.exceptions.bitcoin.TransactionNotFoundException;
import org.royllo.explorer.core.util.validator.PageNumber;
import org.royllo.explorer.core.util.validator.PageSize;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * {@link AssetStateService} implementation.
 */
@Service
@Validated
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class AssetStateServiceImplementation extends BaseService implements AssetStateService {

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
    public AssetStateDTO addAssetState(final @Valid @NonNull AssetStateDTO newAssetState) {
        logger.info("Adding a new asset state {}", newAssetState);

        final AssetState assetStateToCreate = ASSET_STATE_MAPPER.mapToAssetState(newAssetState);

        // =============================================================================================================
        // Checking constraints.

        // We check that the asset state does not already exist.
        assetStateRepository.findByAssetStateId(assetStateToCreate.getAssetStateId()).ifPresent(assetState -> {
            throw new IllegalArgumentException("Asset state already exists");
        });

        // =============================================================================================================
        // Saving the asset state.

        // We update and save the asset state.
        assetStateToCreate.setId(null);

        // Setting the asset of this asset state. We check in database if we can find it with its assetId. If not, we create it.
        final AssetDTO asset = assetService.getAssetByAssetIdOrAlias(assetStateToCreate.getAsset().getAssetId())
                .orElseGet(() -> assetService.addAsset(newAssetState.getAsset()));
        assetStateToCreate.setAsset(ASSET_MAPPER.mapToAsset(asset));

        // Setting the bitcoin transaction output ID if not already set.
        final BitcoinTransactionOutput bto = bitcoinService
                .getBitcoinTransactionOutput(newAssetState.getAnchorOutpoint().getTxId(), newAssetState.getAnchorOutpoint().getVout())
                .map(BITCOIN_MAPPER::mapToBitcoinTransactionOutput)
                .orElseThrow(() -> new TransactionNotFoundException("UTXO Not found"));
        assetStateToCreate.setAnchorOutpoint(bto);

        // We save and return the value.
        final AssetStateDTO assetStateCreated = ASSET_STATE_MAPPER.mapToAssetStateDTO(assetStateRepository.save(assetStateToCreate));
        logger.info("Asset state created: {}", assetStateCreated);
        return assetStateCreated;
    }

    @Override
    public Optional<AssetStateDTO> getAssetStateByAssetStateId(final String assetStateId) {
        logger.info("Getting asset state with asset state id {}", assetStateId);

        return assetStateRepository
                .findByAssetStateId(assetStateId)
                .map(ASSET_STATE_MAPPER::mapToAssetStateDTO)
                .map(assetStateDTO -> {
                    logger.info("Asset state with asset state id {} found: {}", assetStateId, assetStateDTO);
                    return assetStateDTO;
                })
                .or(() -> {
                    logger.info("Asset state with asset state id {} not found", assetStateId);
                    return Optional.empty();
                });
    }

    @Override
    public Page<AssetStateDTO> getAssetStatesByAssetId(final String assetId,
                                                       final @PageNumber int pageNumber,
                                                       final @PageSize int pageSize) {
        logger.info("Getting asset states where asset state id = {}", assetId);

        return assetStateRepository
                .findByAsset_AssetIdOrderById(assetId, getPageRequest(pageNumber, pageSize))
                .map(ASSET_STATE_MAPPER::mapToAssetStateDTO);
    }

}
