package org.royllo.explorer.core.dto.statistics;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Global statistics DTO.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class GlobalStatisticsDTO {

    /** Total number of universes. */
    long universeCount;

    /** Total number of assets. */
    long assetCount;

    /** Total number of asset states. */
    long assetStateCount;

}
