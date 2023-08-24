package org.royllo.explorer.core.service.asset;

import io.micrometer.common.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.AssetState;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
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

    /** Assert state repository. */
    private final AssetStateRepository assetStateRepository;

    /** Bitcoin service. */
    private final BitcoinService bitcoinService;

    @Override
    public AssetStateDTO addAssetState(@NonNull AssetStateDTO newAssetState) {
        logger.info("Adding asset state {}", newAssetState);

        // Checking constraints.
        assert newAssetState.getId() == null : "Asset state already exists";
        // TODO Check bitcoin transaction output link
        assert newAssetState.getAsset() != null : "Linked asset is required";
        assert StringUtils.isNotBlank(newAssetState.getAsset().getAssetId()) : "Asset id is required";

        // =============================================================================================================
        // We update and save the asset state.
        final AssetState assetStateToCreate = ASSET_STATE_MAPPER.mapToAssetState(newAssetState);

        // Setting the creator.
        assetStateToCreate.setCreator(USER_MAPPER.mapToUser(ANONYMOUS_USER_DTO));

        // Setting the bitcoin transaction output ID if not already set.
        if (newAssetState.getAnchorOutpoint().getId() == null) {
            final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(newAssetState.getAnchorOutpoint().getTxId(), newAssetState.getAnchorOutpoint().getVout());
            assert bto.isPresent() : "UTXO " + newAssetState.getAnchorOutpoint().getTxId() + "/" + newAssetState.getAnchorOutpoint().getVout() + " Not found";
            assetStateToCreate.setAnchorOutpoint(BITCOIN_MAPPER.mapToBitcoinTransactionOutput(bto.get()));
        }

        return null;
    }

    @Override
    public Optional<AssetStateDTO> getAssetStateByAnchorOutpoint(String anchorOutpoint) {
        return Optional.empty();
    }

}
