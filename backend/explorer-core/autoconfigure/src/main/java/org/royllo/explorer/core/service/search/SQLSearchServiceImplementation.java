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
import java.util.Objects;

import static java.util.stream.Collectors.joining;
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
            isUsingPostgreSQL = false;
        }
    }

    @Override
    public Page<AssetDTO> queryAssets(final String query,
                                      final @PageNumber int pageNumber,
                                      final @PageSize int pageSize) {
        logger.info("Searching for {}", query);

        // Cleaning the query and choosing the right way to search.
        Page<AssetDTO> results = Page.empty();
        switch (StringUtils.length(query)) {

            // =========================================================================================================
            // If the query is empty or null, we return an empty page.
            case 0:
                logger.debug("The query is empty");
                return results;

            // =========================================================================================================
            // Search if the "query" parameter is a tweaked group key > returns all assets of this asset group.
            case TWEAKED_GROUP_KEY_LENGTH:
                logger.debug("The query '{}' may correspond to a tweaked group key", query);
                results = assetGroupService.getAssetGroupByAssetGroupId(query.trim())
                        .map(AssetGroupDTO::getAssetGroupId)
                        .map(assetGroupId -> assetRepository
                                .findByAssetGroup_AssetGroupIdOrderById(assetGroupId, getPageRequest(pageNumber, pageSize)))
                        .map(page -> page.map(ASSET_MAPPER::mapToAssetDTO))
                        .orElse(Page.empty());
                break;

            // =========================================================================================================
            // Search if the "query" parameter is an asset id.
            case ASSET_ID_LENGTH:
                logger.debug("The query '{}' may correspond to an asset id", query);
                results = assetRepository
                        .findByAssetId(query.trim())
                        .map(ASSET_MAPPER::mapToAssetDTO)
                        .map(asset -> new PageImpl<>(Collections.singletonList(asset)))
                        .orElse(new PageImpl<>(Collections.emptyList()));
                break;

            // =========================================================================================================
            // If nothing found, we will search on asset id alias.
            case ASSET_ID_ALIAS_LENGTH:
                logger.debug("The query '{}' may correspond to an asset id alias", query);
                results = assetRepository
                        .findByAssetIdAlias(query.trim())
                        .map(ASSET_MAPPER::mapToAssetDTO)
                        .map(asset -> new PageImpl<>(List.of(asset)))
                        .orElse(new PageImpl<>(Collections.emptyList()));
                break;

            default:
                break;
        }

        // =============================================================================================================
        // If nothing found, we search if there is an asset with "query" parameter as complete or partial asset name.
        logger.debug("No result for query '{}' so we search on partial name", query);
        if (results.getTotalElements() == 0) {
            if (isUsingPostgreSQL) {
                // PostgreSQL "ILIKE" search.
                results = assetRepository
                        .findByName(query, getPageRequest(pageNumber, pageSize))
                        .map(ASSET_MAPPER::mapToAssetDTO);
            } else {
                // Classical SQL search.
                results = assetRepository
                        .findByNameContainsIgnoreCaseOrderByName(query, getPageRequest(pageNumber, pageSize))
                        .map(ASSET_MAPPER::mapToAssetDTO);
            }
        }

        // =============================================================================================================
        // Displaying logs and return results.
        if (results.isEmpty()) {
            logger.info("Searching for '{}', there is no results", query);
            return Page.empty();
        } else {
            logger.info("Searching for '{}', {} result(s) with assets id(s): {}",
                    query,
                    results.getTotalElements(),
                    results.stream()
                            .map(AssetDTO::getId)
                            .map(Objects::toString)
                            .collect(joining(", ")));
            return results;
        }

    }

}
