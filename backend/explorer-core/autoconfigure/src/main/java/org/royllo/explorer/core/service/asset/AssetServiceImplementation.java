package org.royllo.explorer.core.service.asset;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetDTOCreatorUpdate;
import org.royllo.explorer.core.dto.asset.AssetDTOIssuanceUpdate;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.provider.storage.ContentService;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.exceptions.bitcoin.TransactionNotFoundException;
import org.royllo.explorer.core.util.validator.PageNumber;
import org.royllo.explorer.core.util.validator.PageSize;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.ASSET_ID_LENGTH;

/**
 * {@link AssetService} implementation.
 */
@Service
@Validated
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class AssetServiceImplementation extends BaseService implements AssetService {

    /** Assert repository. */
    private final AssetRepository assetRepository;

    /** User repository. */
    private final UserRepository userRepository;

    /** Asset group service. */
    private final AssetGroupService assetGroupService;

    /** Bitcoin service. */
    private final BitcoinService bitcoinService;

    /** Content service. */
    private final ContentService contentService;

    @Override
    public AssetDTO addAsset(final @Valid @NonNull AssetDTO newAsset) {
        logger.info("Adding a new asset {}", newAsset);

        // =============================================================================================================
        // Checking constraints.

        // Asset id already exists.
        assetRepository.findByAssetId(newAsset.getAssetId()).ifPresent(asset -> {
            throw new IllegalArgumentException("Asset already registered");
        });

        // =============================================================================================================
        // We create the asset.

        final Asset assetToCreate = ASSET_MAPPER.mapToAsset(newAsset);

        // As we are creating a new asset, we set the id to null.
        assetToCreate.setId(null);

        // We set the bitcoin transaction.
        final BitcoinTransactionOutput bto = bitcoinService
                .getBitcoinTransactionOutput(newAsset.getGenesisPoint().getTxId(), newAsset.getGenesisPoint().getVout())
                .map(BITCOIN_MAPPER::mapToBitcoinTransactionOutput)
                .orElseThrow(() -> new TransactionNotFoundException("UTXO Not found"));
        assetToCreate.setGenesisPoint(bto);

        // Set asset group (If it's present).
        if (newAsset.getAssetGroup() != null) {
            final AssetGroupDTO assetGroupDTO = assetGroupService
                    .getAssetGroupByAssetGroupId(newAsset.getAssetGroup().getAssetGroupId())
                    .orElseGet(() -> assetGroupService.addAssetGroup(newAsset.getAssetGroup()));
            assetToCreate.setAssetGroup(ASSET_GROUP_MAPPER.mapToAssetGroup(assetGroupDTO));
        }

        // We save and return the value.
        final AssetDTO assetCreated = ASSET_MAPPER.mapToAssetDTO(assetRepository.save(assetToCreate));
        logger.info("Asset created: {}", assetCreated);
        return assetCreated;
    }

    @Override
    public void updateAssetIssuanceData(final String assetId,
                                        final @Valid @NonNull AssetDTOIssuanceUpdate assetUpdate) {
        logger.info("Update asset with asset id {} with {}", assetId, assetUpdate);

        // =============================================================================================================
        // Checking constraints.

        // We check that the asset exists and get it.
        final Asset assetToUpdate = assetRepository.findByAssetId(assetId)
                .orElseThrow(() -> new IllegalArgumentException(assetId + "not found"));

        // =============================================================================================================
        // Updating the asset.

        // If we have the metadata.
        if (assetUpdate.metadata() != null) {
            try {
                // Decoding (same as using xxd -r -p).
                byte[] decodedBytes = Hex.decodeHex(assetUpdate.metadata());

                // Detecting the file type.
                final String mimeType = new Tika().detect(decodedBytes);
                String extension = MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();

                // If we have a file extension ".txt", we check if it's a JSON.
                if (".txt".equalsIgnoreCase(extension)) {
                    if (isValidJSON(new String(decodedBytes))) {
                        extension = ".json";
                    }
                }

                // Saving the file.
                final String fileName = assetId + extension;
                contentService.storeFile(decodedBytes, fileName);
                logger.info("Asset metadata updated for asset id {}: Metadata file saved as {}", assetId, fileName);

                // Setting the name of the file.
                assetToUpdate.setMetaDataFileName(fileName);
            } catch (DecoderException | MimeTypeException e) {
                logger.error("Asset metadata update for {}: Error decoding and saving metadata {}", assetId, e.getMessage());
            }
        }

        // If we have the new amount.
        if (assetUpdate.amount() != null) {
            assetToUpdate.setAmount(assetUpdate.amount());
            logger.info("Asset amount update for {}: Amount updated to {}", assetId, assetUpdate.amount());
        }

        // If we have the issuance date.
        if (assetUpdate.issuanceDate() != null) {
            assetToUpdate.setIssuanceDate(assetUpdate.issuanceDate());
            logger.info("Asset issuance update for {}: Issuance date updated to {}", assetId, assetUpdate.issuanceDate());
        }

        // We save the asset with the new information.
        assetRepository.save(assetToUpdate);
    }

    @Override
    public void updateAssetCreatorData(final String assetId,
                                       final @Valid @NonNull AssetDTOCreatorUpdate assetUpdate) {
        logger.info("Update asset with asset id {} with {}", assetId, assetUpdate);

        // =============================================================================================================
        // Checking constraints.

        // We check that the asset exists and get it.
        final Asset assetToUpdate = assetRepository.findByAssetId(assetId)
                .orElseThrow(() -> new IllegalArgumentException(assetId + "not found"));

        // We check that the asset id alias is not already registered.
        assetRepository.findByAssetIdAlias(assetUpdate.assetIdAlias()).ifPresent(asset -> {
            throw new IllegalArgumentException("Asset id alias already registered");
        });

        // =============================================================================================================
        // Updating the asset.

        // If we have the asset id alias.
        if (assetUpdate.assetIdAlias() != null) {
            assetToUpdate.setAssetIdAlias(assetUpdate.assetIdAlias().trim());
            logger.info("Asset update for {}: Asset id alias updated to {}", assetId, assetUpdate.assetIdAlias());
        }

        // If we have the readme.
        if (assetUpdate.readme() != null) {
            assetToUpdate.setReadme(assetUpdate.readme());
            logger.info("Asset update for {}: Readme updated to {}", assetId, assetUpdate.readme());
        }

        // We save the asset with the new information.
        assetRepository.save(assetToUpdate);
    }

    @Override
    public Optional<AssetDTO> getAsset(final long id) {
        logger.info("Getting asset with id {}", id);

        return assetRepository.findById(id)
                .map(ASSET_MAPPER::mapToAssetDTO)
                .map(asset -> {
                    logger.info("Asset with id {} found: {}", id, asset);
                    return asset;
                })
                .or(() -> {
                    logger.info("Asset with id {} not found", id);
                    return Optional.empty();
                });
    }

    @Override
    public Optional<AssetDTO> getAssetByAssetIdOrAlias(final String assetIdOrAlias) {
        logger.info("Getting asset with asset id {}", assetIdOrAlias);

        if (StringUtils.length(assetIdOrAlias) == ASSET_ID_LENGTH) {
            // We received an asset id (we know it because of the size).
            return assetRepository.findByAssetId(assetIdOrAlias)
                    .map(ASSET_MAPPER::mapToAssetDTO)
                    .map(asset -> {
                        logger.info("Asset with asset id {} found: {}", assetIdOrAlias, asset);
                        return asset;
                    })
                    .or(() -> {
                        logger.info("Asset with asset id {} not found", assetIdOrAlias);
                        return Optional.empty();
                    });
        } else {
            // It's not an asset id (the size is not the good one), so we search on asset id alias.
            return assetRepository.findByAssetIdAlias(assetIdOrAlias)
                    .map(ASSET_MAPPER::mapToAssetDTO)
                    .map(asset -> {
                        logger.info("Asset with asset id alias {} found: {}", assetIdOrAlias, asset);
                        return asset;
                    })
                    .or(() -> {
                        logger.info("Asset with asset id alias {} not found", assetIdOrAlias);
                        return Optional.empty();
                    });
        }

    }

    @Override
    public Page<AssetDTO> getAssetsByAssetGroupId(final String assetGroupId,
                                                  final @PageNumber int pageNumber,
                                                  final @PageSize int pageSize) {
        logger.info("Getting assets with asset group id: {}", assetGroupId);

        return assetRepository
                .findByAssetGroup_AssetGroupIdOrderById(assetGroupId, getPageRequest(pageNumber, pageSize))
                .map(ASSET_MAPPER::mapToAssetDTO);
    }

    @Override
    public Page<AssetDTO> getAssetsByUsername(final String username,
                                              final @PageNumber int pageNumber,
                                              final @PageSize int pageSize) {
        logger.info("Getting assets of username: {}", username);

        return assetRepository
                .findByCreator_UsernameOrderById(username, getPageRequest(pageNumber, pageSize))
                .map(ASSET_MAPPER::mapToAssetDTO);
    }

    @Override
    public Page<AssetDTO> getAssetsByUserId(final String userId,
                                            final @PageNumber int pageNumber,
                                            final @PageSize int pageSize) {
        logger.info("Getting assets of user id: {}", userId);

        return assetRepository
                .findByCreator_UserIdOrderById(userId, getPageRequest(pageNumber, pageSize))
                .map(ASSET_MAPPER::mapToAssetDTO);
    }

}
