package org.royllo.explorer.core.dto.statistics;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Global statistics DTO.
 */
@Getter
@SuperBuilder
@SuppressWarnings("checkstyle:VisibilityModifier")
public class GlobalStatisticsDTO {

    /** Total number of universes. */
    int universeCount;

    /** Total number of assets. */
    int assetCount;

    /** Total number of asset states. */
    int assetStateCount;

}
