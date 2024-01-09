package org.royllo.explorer.core.service.search;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * {@link SearchService} SQL implementation.
 */
@Service
@RequiredArgsConstructor
@Profile("!redis-search-engine")
@SuppressWarnings("checkstyle:DesignForExtension")
public class SQLSearchServiceImplementation extends BaseService implements SearchService {

    /** Assert repository. */
    private final AssetRepository assetRepository;

    /** Asset group service. */
    private final AssetGroupService assetGroupService;

    @Override
    public Page<AssetDTO> queryAssets(@NonNull final String query,
                                      final int page,
                                      final int pageSize) {
        logger.info("Searching for {}", query);

        // Checking constraints.
        assert page >= 1 : "Page number starts at page 1";

        // Results.
        Page<AssetDTO> results = Page.empty();

        // Search if the "query" parameter is a tweaked group key (asset group) > returns all assets of this asset group.
        final Optional<AssetGroupDTO> assetGroup = assetGroupService.getAssetGroupByAssetGroupId(query);
        if (assetGroup.isPresent()) {
            results = assetRepository.findByAssetGroup_AssetGroupId(assetGroup.get().getAssetGroupId(),
                            PageRequest.of(page - 1, pageSize))
                    .map(ASSET_MAPPER::mapToAssetDTO);
        }

        // If nothing found, we search if there is this asset in database with this exact asset id.
        if (results.isEmpty()) {
            final Optional<Asset> assetIdSearch = assetRepository.findByAssetId(query);
            if (assetIdSearch.isPresent()) {
                results = new PageImpl<>(assetIdSearch.stream()
                        .map(ASSET_MAPPER::mapToAssetDTO)
                        .toList());
            }
        }

        // If nothing found, we will search on asset id alias.
        if (results.isEmpty()) {
            final Optional<Asset> assetIdAliasSearch = assetRepository.findByAssetIdAlias(query);
            if (assetIdAliasSearch.isPresent()) {
                results = new PageImpl<>(assetIdAliasSearch.stream()
                        .map(ASSET_MAPPER::mapToAssetDTO)
                        .toList());
            }
        }

        // If nothing found, we search if there is an asset with "query" parameter as complete or partial asset name.
        if (results.isEmpty()) {
            results = assetRepository.findByNameContainsIgnoreCaseOrderByName(query,
                    PageRequest.of(page - 1, pageSize)).map(ASSET_MAPPER::mapToAssetDTO);
        }

        // Displaying logs and return results.
        if (results.isEmpty()) {
            logger.info("For '{}', there is no results", query);
        } else {
            logger.info("For '{}', {} result(s) with assets id(s): {}",
                    query,
                    results.getTotalElements(),
                    results.stream()
                            .map(AssetDTO::getId)
                            .map(Objects::toString)
                            .collect(joining(", ")));
        }
        return results;
    }

}
