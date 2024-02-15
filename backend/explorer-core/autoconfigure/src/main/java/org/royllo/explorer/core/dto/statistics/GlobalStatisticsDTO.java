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
    long universeCount;

    /** Total number of assets. */
    long assetCount;

    /** Total number of users. */
    long userCount;

}
