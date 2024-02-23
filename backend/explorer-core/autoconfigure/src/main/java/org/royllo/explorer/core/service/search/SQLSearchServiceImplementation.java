package org.royllo.explorer.core.service.search;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.ASSET_ALIAS_LENGTH;
import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.ASSET_ID_LENGTH;
import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.TWEAKED_GROUP_KEY_LENGTH;

/**
 * {@link SearchService} SQL implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class SQLSearchServiceImplementation extends BaseService implements SearchService {

    /** Datasource. */
    private final DataSource dataSource;

    /** Assert repository. */
    private final AssetRepository assetRepository;

    /** Asset group service. */
    private final AssetGroupService assetGroupService;

    /** Indicates if it's a postgresql database. */
    @Getter
    private boolean isUsingPostgreSQL = false;

    @PostConstruct
    public void init() {
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            isUsingPostgreSQL = "PostgreSQL".equals(databaseProductName);
        } catch (SQLException e) {
            logger.error("Impossible to retrieve database metadata {}", e.getMessage());
        }
    }

    @Override
    public Page<AssetDTO> queryAssets(@NonNull final String query,
                                      final int page,
                                      final int pageSize) {
        logger.info("Searching for {}", query);

        // Checking constraints.
        assert page >= 1 : "Page number starts at page 1";

        // Cleaning the query.
        final String cleanedQuery = query.trim();

        // =============================================================================================================
        // TWEAKED_GROUP_KEY_SIZE search.
        if (cleanedQuery.length() == TWEAKED_GROUP_KEY_LENGTH) {
            // Search if the "query" parameter is a tweaked group key (asset group) > returns all assets of this asset group.
            final Optional<AssetGroupDTO> assetGroup = assetGroupService.getAssetGroupByAssetGroupId(cleanedQuery);
            if (assetGroup.isPresent()) {
                logger.info("The query '{}' corresponds to a tweaked group key", cleanedQuery);
                return assetRepository.findByAssetGroup_AssetGroupIdOrderById(assetGroup.get().getAssetGroupId(), PageRequest.of(page - 1, pageSize))
                        .map(ASSET_MAPPER::mapToAssetDTO);
            }
        }

        // =============================================================================================================
        // ASSET_ID search.
        if (cleanedQuery.length() == ASSET_ID_LENGTH) {
            // Search if the "query" parameter is an asset id.
            final Optional<Asset> assetIdSearch = assetRepository.findByAssetId(cleanedQuery);
            if (assetIdSearch.isPresent()) {
                logger.info("The query '{}' corresponds to an asset id", cleanedQuery);
                return new PageImpl<>(assetIdSearch.stream()
                        .map(ASSET_MAPPER::mapToAssetDTO)
                        .toList());
            }
        }

        // =============================================================================================================
        // ASSET_ID_ALIAS search.
        if (cleanedQuery.length() == ASSET_ALIAS_LENGTH) {
            // If nothing found, we will search on asset id alias.
            final Optional<Asset> assetIdAliasSearch = assetRepository.findByAssetIdAlias(cleanedQuery);
            if (assetIdAliasSearch.isPresent()) {
                logger.info("The query '{}' corresponds to an asset id alias", cleanedQuery);
                return new PageImpl<>(assetIdAliasSearch.stream()
                        .map(ASSET_MAPPER::mapToAssetDTO)
                        .toList());
            }
        }

        // If nothing found, we search if there is an asset with "query" parameter as complete or partial asset name.
        Page<AssetDTO> results;
        if (isUsingPostgreSQL) {
            // PostgreSQL "ILIKE" search.
            results = assetRepository.findByName(cleanedQuery,
                    PageRequest.of(page - 1, pageSize)).map(ASSET_MAPPER::mapToAssetDTO);
        } else {
            results = assetRepository.findByNameContainsIgnoreCaseOrderByName(cleanedQuery,
                    PageRequest.of(page - 1, pageSize)).map(ASSET_MAPPER::mapToAssetDTO);
        }

        // Displaying logs and return results.
        if (results.isEmpty()) {
            logger.info("For '{}', there is no results", cleanedQuery);
        } else {
            logger.info("For '{}', {} result(s) with assets id(s): {}",
                    cleanedQuery,
                    results.getTotalElements(),
                    results.stream()
                            .map(AssetDTO::getId)
                            .map(Objects::toString)
                            .collect(joining(", ")));
        }

        return results;
    }

}
