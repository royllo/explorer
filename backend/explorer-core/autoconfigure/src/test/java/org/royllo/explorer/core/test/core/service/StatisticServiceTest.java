package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.statistics.GlobalStatisticsDTO;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.repository.universe.UniverseServerRepository;
import org.royllo.explorer.core.service.statistics.StatisticServiceImplementation;
import org.royllo.explorer.core.service.universe.UniverseServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("StatisticService tests")
public class StatisticServiceTest {

    @Autowired
    UniverseServerRepository universeServerRepository;

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    AssetStateRepository assetStateRepository;

    @Autowired
    UniverseServerService universeServerService;

    @Autowired
    StatisticServiceImplementation statisticService;

    @Test
    @DisplayName("getGlobalStatistics()")
    public void getGlobalStatistics() {
        // Initial values of statistics.
        long initialUniverseCount = universeServerRepository.count();
        long initialAssetCount = assetRepository.count();
        long initialAssetStateCount = assetStateRepository.count();

        // We get the statistics from the database.
        GlobalStatisticsDTO globalStatistics = statisticService.getGlobalStatistics();
        assertEquals(initialUniverseCount, globalStatistics.getUniverseCount());
        assertEquals(initialAssetCount, globalStatistics.getAssetCount());
        assertEquals(initialAssetStateCount, globalStatistics.getAssetStateCount());

        // We add a new universe server.
        universeServerService.addUniverseServer("1.1.1.1");

        // We added a universe server => the statistics should not be updated as we have cache.
        globalStatistics = statisticService.getGlobalStatistics();
        assertEquals(initialUniverseCount, globalStatistics.getUniverseCount());
        assertEquals(initialAssetCount, globalStatistics.getAssetCount());
        assertEquals(initialAssetStateCount, globalStatistics.getAssetStateCount());

        // We clear the cache.
        statisticService.evictStatisticsCache();

        // We should now see the universe server we added.
        globalStatistics = statisticService.getGlobalStatistics();
        assertEquals(initialUniverseCount + 1, globalStatistics.getUniverseCount());
        assertEquals(initialAssetCount, globalStatistics.getAssetCount());
        assertEquals(initialAssetStateCount, globalStatistics.getAssetStateCount());
    }

}