package org.royllo.explorer.core.service.search;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.validator.PageNumber;
import org.royllo.explorer.core.util.validator.PageSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.ASSET_ID_ALIAS_LENGTH;
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
            isUsingPostgreSQL = "PostgreSQL".equalsIgnoreCase(databaseProductName);
        } catch (SQLException e) {
            logger.error("Impossible to retrieve database metadata {}", e.getMessage());
        }
    }

    @Override
    public Page<AssetDTO> queryAssets(final String query,
                                      final @PageNumber int pageNumber,
                                      final @PageSize int pageSize) {
        logger.info("Searching for {}", query);

        // Cleaning the query and choosing the right way to search.
        final String cleanedQuery = query.trim();
        switch (StringUtils.length(cleanedQuery)) {

            // =========================================================================================================
            case 0:
                // If the query is empty or null, we return an empty page.
                logger.info("The query is empty");
                return Page.empty();

            // =========================================================================================================
            case TWEAKED_GROUP_KEY_LENGTH:
                // Search if the "query" parameter is a tweaked group key (asset group) > returns all assets of this asset group.
                logger.info("The query '{}' corresponds to a tweaked group key", cleanedQuery);
                return assetGroupService.getAssetGroupByAssetGroupId(cleanedQuery)
                        .map(AssetGroupDTO::getAssetGroupId)
                        .map(assetGroupId -> assetRepository
                                .findByAssetGroup_AssetGroupIdOrderById(assetGroupId, getPageRequest(pageNumber, pageSize)))
                        .map(page -> page.map(ASSET_MAPPER::mapToAssetDTO))
                        .orElse(Page.empty());

            // =========================================================================================================
            case ASSET_ID_LENGTH:
                // Search if the "query" parameter is an asset id.
                logger.info("The query '{}' corresponds to an asset id", cleanedQuery);
                return assetRepository.findByAssetId(cleanedQuery)
                        .map(ASSET_MAPPER::mapToAssetDTO)
                        .map(asset -> new PageImpl<>(List.of(asset)))
                        .orElse(new PageImpl<>(Collections.emptyList()));

            // =========================================================================================================
            case ASSET_ID_ALIAS_LENGTH:
                // If nothing found, we will search on asset id alias.
                logger.info("The query '{}' corresponds to an asset id alias", cleanedQuery);
                return assetRepository.findByAssetIdAlias(cleanedQuery)
                        .map(ASSET_MAPPER::mapToAssetDTO)
                        .map(asset -> new PageImpl<>(List.of(asset)))
                        .orElse(new PageImpl<>(Collections.emptyList()));

            // =========================================================================================================
            default:
                // If nothing found, we search if there is an asset with "query" parameter as complete or partial asset name.
                logger.info("The query '{}' corresponds to search on partial name", cleanedQuery);
                if (isUsingPostgreSQL) {
                    // PostgreSQL "ILIKE" search.
                    return assetRepository.findByName(cleanedQuery, getPageRequest(pageNumber, pageSize))
                            .map(ASSET_MAPPER::mapToAssetDTO);
                } else {
                    // Classical SQL search.
                    return assetRepository.findByNameContainsIgnoreCaseOrderByName(cleanedQuery, getPageRequest(pageNumber, pageSize))
                            .map(ASSET_MAPPER::mapToAssetDTO);
                }

        }
    }

}
