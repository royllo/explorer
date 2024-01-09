package org.royllo.explorer.core.service.search;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * {@link SearchService} Redis implementation.
 */
@Service
@RequiredArgsConstructor
@Profile("redis-search-engine")
@SuppressWarnings("checkstyle:DesignForExtension")
public class RedisSearchServiceImplementation extends BaseService implements SearchService {

    @Override
    public Page<AssetDTO> queryAssets(final String query, final int page, final int pageSize) {
        return null;
    }

}
