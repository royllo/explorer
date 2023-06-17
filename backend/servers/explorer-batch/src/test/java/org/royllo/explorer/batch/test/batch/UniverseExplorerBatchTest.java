package org.royllo.explorer.batch.test.batch;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.universe.UniverseExplorerBatch;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("Universe explorer batch test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"mempoolTransactionServiceMock", "tapdProofServiceMock", "scheduler-disabled"})
public class UniverseExplorerBatchTest {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    TapdService tapdService;

    @Autowired
    UniverseExplorerBatch universeExplorerBatch;

    @Test
    @Disabled
    @DisplayName("Mock test")
    public void batch() throws IOException {
        long count = requestRepository.count();

        // In database, we have two universe servers.
        // The first server lists three assets : asset_id_1, asset_id_2 and asset_id_3
        // The second server lists two assets : asset_id_4 and asset_id_1 (already defined by the first server).
        universeExplorerBatch.processUniverseServers();

        // We should have 4 more requests in database.
        assertEquals(count + 4, requestRepository.count());

    }

}
