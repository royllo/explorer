package org.royllo.explorer.core.service.asset;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.provider.storage.ContentService;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.ASSET_ID_LENGTH;

/**
 * {@link AssetService} implementation.
 */
@Service
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
    public void updateAsset(final String assetId,
                            final String metadata,
                            final BigInteger amount,
                            final ZonedDateTime issuanceDate) {
        final Optional<Asset> assetToUpdate = assetRepository.findByAssetId(assetId);

        // We check that the asset exists.
        assert assetToUpdate.isPresent() : assetId + " not found";

        // =============================================================================================================
        // If we have the metadata.
        if (metadata != null) {

            try {
                // Decoding (same as using xxd -r -p).
                byte[] decodedBytes = Hex.decodeHex(metadata);

                // Detecting the file type.
                final String mimeType = new Tika().detect(decodedBytes);
                String extension = MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();

                // If we have a file extension ".txt", we check if it's a JSON.
                if (".txt" .equalsIgnoreCase(extension)) {
                    if (isJSONValid(new String(decodedBytes))) {
                        extension = ".json";
                    }
                }

                // Saving the file.
                final String fileName = assetId + extension;
                contentService.storeFile(decodedBytes, fileName);
                logger.info("Asset id update for {}: Metadata saved as {}", assetId, fileName);

                // Setting the name of the file.
                assetToUpdate.get().setMetaDataFileName(fileName);
            } catch (DecoderException | MimeTypeException e) {
                logger.error("Asset id update for {}: Error decoding and saving metadata {}", assetId, e.getMessage());
            }
        }

        // =============================================================================================================
        // If we have the new amount.
        if (amount != null) {
            assetToUpdate.get().setAmount(amount);
            logger.info("Asset id update for {}: Amount updated to {}", assetId, amount);
        }

        // =============================================================================================================
        // If we have the issuance date.
        if (issuanceDate != null) {
            assetToUpdate.get().setIssuanceDate(issuanceDate);
            logger.info("Asset id update for {}: Issuance date updated to {}", assetId, issuanceDate);
        }

        // We save the asset with the new information.
        assetRepository.save(assetToUpdate.get());
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

        if (assetId.length() == ASSET_ID_LENGTH) {
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


    /**
     * Returns true if the string is a valid JSON.
     *
     * @param content string to check
     * @return true if content is a valid JSON
     */
    private boolean isJSONValid(final String content) {
        try {
            new ObjectMapper().readTree(content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Page<AssetDTO> getAssetsByUsername(@NonNull final String username, final int page, final int pageSize) {
        logger.info("Getting assets of username: {}", username);

        // Checking constraints.
        assert page >= 1 : "Page number starts at page 1";
        assert userRepository.findByUsernameIgnoreCase(username).isPresent() : "User not found with username: " + username;

        // Returning the results.
        return assetRepository.findByCreator_Username(username, PageRequest.of(page - 1, pageSize))
                .map(ASSET_MAPPER::mapToAssetDTO);
    }

}
