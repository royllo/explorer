package org.royllo.explorer.batch.batch.universe;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.provider.tapd.UniverseLeavesResponse;
import org.royllo.explorer.core.provider.tapd.UniverseRootsResponse;
import org.royllo.explorer.core.repository.proof.ProofRepository;
import org.royllo.explorer.core.repository.universe.UniverseServerRepository;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.service.universe.UniverseServerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static java.time.ZonedDateTime.now;

/**
 * Batch retrieving data from know universe servers.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class UniverseExplorerBatch extends BaseBatch {

    /** Start delay in milliseconds (1 000 ms = 1 second). */
    private static final int START_DELAY_IN_MILLISECONDS = 1_000;

    /** Delay between two calls to process requests (1 000 ms = 1 second). */
    private static final int DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS = 10_000;

    /** Proof repository. */
    private final ProofRepository proofRepository;

    /** Universe server repository. */
    private final UniverseServerRepository universeServerRepository;

    /** Universe server service. */
    private final UniverseServerService universeServerService;

    /** Tapd service. */
    private final TapdService tapdService;

    /** Request service. */
    private final RequestService requestService;

    /**
     * Retrieving all universe servers data.
     */
    @Scheduled(initialDelay = START_DELAY_IN_MILLISECONDS, fixedDelay = DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS)
    public void processUniverseServers() {
        if (enabled.get()) {
            universeServerRepository.findTop3ByOrderByLastSynchronizedOnAsc().forEach(universeServer -> {
                // For each server we have in our databases.
                logger.info("Processing universe server: {}", universeServer);

                // We indicate that we are working on this universe server by updating its last sync date.
                universeServer.setLastSynchronizedOn(now());
                universeServerRepository.save(universeServer);

                // We retrieve the universe roots.
                final UniverseRootsResponse universeRoots = tapdService.getUniverseRoots(universeServer.getServerAddress()).block();
                if (universeRoots == null) {
                    logger.error("No universe roots found for server: {}", universeServer);
                    return;
                }

                universeRoots.getUniverseRoots()
                        .values()
                        .stream()
                        .filter(universeRoot -> universeRoot.getId() != null)
                        .filter(universeRoot -> universeRoot.getId().getAssetId() != null)
                        .map(universeRoot -> universeRoot.getId().getAssetId())
                        .distinct()
                        .forEach(assetId -> {
                            logger.info("Processing asset id: {}", assetId);
                            final UniverseLeavesResponse leaves = tapdService.getUniverseLeaves(universeServer.getServerAddress(), assetId).block();
                            if (leaves == null) {
                                logger.error("No universe leaves found for asset id: {}", assetId);
                                return;
                            }

                            // We retrieve the proofs for each asset.
                            leaves.getLeaves()
                                    .stream()
                                    .map(UniverseLeavesResponse.Leaf::getIssuanceProof)
                                    .filter(proof -> proofRepository.findByProofId(sha256(proof)).isEmpty())
                                    .forEach(proof -> {
                                        final AddProofRequestDTO addProofRequest = requestService.createAddProofRequest(proof);
                                        logger.info("Request created {} for asset: {}", addProofRequest.getId(), addProofRequest.getRawProof());
                                    });

                            // TODO Replace with a bucket.
                            try {
                                TimeUnit.SECONDS.sleep(2);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });

            });
        }
    }

}
