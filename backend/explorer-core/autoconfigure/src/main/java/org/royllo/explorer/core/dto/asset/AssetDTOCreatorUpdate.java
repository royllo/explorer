package org.royllo.explorer.core.dto.asset;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static org.royllo.explorer.core.dto.asset.AssetDTO.ASSET_ID_ALIAS_MAX_SIZE;
import static org.royllo.explorer.core.dto.asset.AssetDTO.ASSET_ID_ALIAS_MIN_SIZE;
import static org.royllo.explorer.core.dto.asset.AssetDTO.README_MAX_SIZE;

/**
 * Asset creator data update.
 *
 * @param assetIdAlias asset id alias to set
 * @param readme       readme
 */
@Builder
public record AssetDTOCreatorUpdate(
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{validation.asset.assetIdAlias.invalid}")
        @Size(min = ASSET_ID_ALIAS_MIN_SIZE, max = ASSET_ID_ALIAS_MAX_SIZE, message = "{validation.asset.assetIdAlias.size}")
        String assetIdAlias,
        @Size(max = README_MAX_SIZE, message = "{validation.asset.readme.size}")
        String readme) {
}
