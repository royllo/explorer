package org.royllo.explorer.core.service.statistics;

import org.royllo.explorer.core.dto.statistics.GlobalStatisticsDTO;

/**
 * Statistic service.
 */
public interface StatisticService {

    /**
     * Returns the global statistics.
     *
     * @return global statistics
     */
    GlobalStatisticsDTO getGlobalStatistics();

}
