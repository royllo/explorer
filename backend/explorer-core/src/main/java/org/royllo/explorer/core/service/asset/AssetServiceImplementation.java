package org.royllo.explorer.core.service.asset;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.constants.UserConstants;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * Asset service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class AssetServiceImplementation extends BaseService implements AssetService {

    /** Anonymous user. */
    private User anonymousUser;

    /** User repository. */
    private final UserRepository userRepository;

    /** Assert repository. */
    private final AssetRepository assetRepository;

    /** Bitcoin service. */
    private final BitcoinService bitcoinService;

    /**
     * Initialize request service implementation :
     * - Get and cache anonymous user.
     */
    @PostConstruct
    void initialize() {
        // We retrieve and cache the anonymous user as, for the moment, we don't manage users.
        userRepository.findById(UserConstants.ANONYMOUS_USER_ID).ifPresent(user -> anonymousUser = user);
    }

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
    public AssetDTO addAsset(final AssetDTO newAsset) {
        // We check constraints.
        assert newAsset.getId() == null : "Asset already exists";
        assert newAsset.getGenesisPoint() != null : "Bitcoin transaction is required";
        assert assetRepository.findByAssetId(newAsset.getAssetId()).isEmpty() : newAsset.getAssetId() + " already registered";

        // We save the value.
        final Asset assetToCreate = ASSET_MAPPER.mapToAsset(newAsset);
        // Setting the creator.
        assetToCreate.setCreator(anonymousUser);
        // Setting the bitcoin transaction output ID if not already set.
        if (newAsset.getGenesisPoint().getId() == null) {
            final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(newAsset.getGenesisPoint().getTxId(), newAsset.getGenesisPoint().getVout());
            assert bto.isPresent() : "UTXO " + newAsset.getGenesisPoint().getTxId() + "/" + newAsset.getGenesisPoint().getVout() + " Not found";
            assetToCreate.setGenesisPoint(BITCOIN_MAPPER.mapToBitcoinTransactionOutput(bto.get()));
        }
        final Asset assetCreated = assetRepository.save(assetToCreate);

        // We return the value.
        logger.info("Asset created with id {} : {}", assetCreated.getId(), assetCreated);
        return ASSET_MAPPER.mapToAssetDTO(assetCreated);
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
