package org.royllo.explorer.core.service.statistics;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.statistics.GlobalStatisticsDTO;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.universe.UniverseServerRepository;
import org.royllo.explorer.core.repository.user.UserRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * {@link StatisticService} implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class StatisticServiceImplementation extends BaseService implements StatisticService {

    /** Universe server repository. */
    private final UniverseServerRepository universeServerRepository;

    /** Asset repository. */
    private final AssetRepository assetRepository;

    /** User repository. */
    private final UserRepository userRepository;

    @Override
    @Cacheable("globalStatistics")
    public GlobalStatisticsDTO getGlobalStatistics() {
        return GlobalStatisticsDTO.builder()
                .universeCount(universeServerRepository.count())
                .assetCount(assetRepository.count())
                .userCount(userRepository.count())
                .build();
    }

    @Scheduled(fixedDelayString = "PT1H")
    @CacheEvict(value = "globalStatistics", allEntries = true)
    public void evictGlobalStatisticsCache() {
        logger.info("Clearing global statistics cache");
    }

}
