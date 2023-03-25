package org.royllo.explorer.core.service.asset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.dto.asset.AssetDTO;
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
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_DTO;

/**
 * Asset service implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class AssetServiceImplementation extends BaseService implements AssetService {

    /**
     * Assert repository.
     */
    private final AssetRepository assetRepository;

    /**
     * Bitcoin service.
     */
    private final BitcoinService bitcoinService;

    @Override
    public Page<AssetDTO> queryAssets(@NonNull final String query,
                                      final int page,
                                      final int pageSize) {
        logger.info("queryAssets - Searching for {}", query);

        // Checking constraints.
        assert page >= 1 : "Page number starts at page 1";

        // Getting results.
        Page<AssetDTO> results;
        final Optional<Asset> firstSearch = assetRepository.findByAssetId(query);
        if (firstSearch.isPresent()) {
            // We found an asset with the corresponding assetId, we return it.
            results = new PageImpl<>(firstSearch.stream()
                    .map(ASSET_MAPPER::mapToAssetDTO)
                    .toList());
        } else {
            // We search all assets where "query" is in the name.
            results = assetRepository.findByNameContainsIgnoreCaseOrderByName(query,
                            PageRequest.of(page - 1, pageSize))
                    .map(ASSET_MAPPER::mapToAssetDTO);
        }

        // Displaying logs.
        if (results.isEmpty()) {
            logger.info("queryAssets - For '{}', there is no results", query);
        } else {
            logger.info("queryAssets - For '{}', {} result(s) with assets id(s): {}",
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
        logger.info("addAsset - Adding asset {}", newAsset);

        // Checking constraints.
        assert newAsset.getId() == null : "Asset already exists";
        assert newAsset.getGenesisPoint() != null : "Bitcoin transaction is required";
        assert assetRepository.findByAssetId(newAsset.getAssetId()).isEmpty() : newAsset.getAssetId() + " already registered";

        // We update and save the asset.
        final Asset assetToCreate = ASSET_MAPPER.mapToAsset(newAsset);
        // Setting the creator.
        assetToCreate.setCreator(USER_MAPPER.mapToUser(ANONYMOUS_USER_DTO));
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
        logger.info("getAsset - Getting asset with id {}", id);

        final Optional<Asset> asset = assetRepository.findById(id);
        if (asset.isEmpty()) {
            logger.info("getAsset - Asset with id {} not found", id);
            return Optional.empty();
        } else {
            logger.info("getAsset - Asset with id {} found: {}", id, asset.get());
            return asset.map(ASSET_MAPPER::mapToAssetDTO);
        }
    }

    @Override
    public Optional<AssetDTO> getAssetByAssetId(@NonNull final String assetId) {
        logger.info("getAssetByAssetId - Getting asset with assetId {}", assetId);

        final Optional<Asset> asset = assetRepository.findByAssetId(assetId);
        if (asset.isEmpty()) {
            logger.info("getAssetByAssetId - Asset with assetId {} not found", assetId);
            return Optional.empty();
        } else {
            logger.info("getAssetByAssetId - Asset with assetId {} found: {}", assetId, asset.get());
            return asset.map(ASSET_MAPPER::mapToAssetDTO);
        }
    }

}
