package org.royllo.explorer.batch.batch.universe;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.batch.util.base.BaseBatch;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.provider.tapd.UniverseLeavesResponse;
import org.royllo.explorer.core.repository.proof.ProofRepository;
import org.royllo.explorer.core.repository.universe.UniverseServerRepository;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.ZonedDateTime.now;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_ISSUANCE;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_TRANSFER;

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
    private static final int DELAY_BETWEEN_TWO_PROCESS_IN_MILLISECONDS = 60_000;

    /** Universe roots results limit. */
    public static final int UNIVERSE_ROOTS_LIMIT = 100;

    /** Proof repository. */
    private final ProofRepository proofRepository;

    /** Universe server repository. */
    private final UniverseServerRepository universeServerRepository;

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
            universeServerRepository.findFirstByOrderByLastSynchronizationAttemptAsc().ifPresent(universeServer -> {
                // For each server we have in our databases.
                logger.info("Processing universe server: {}", universeServer.getServerAddress());

                // We indicate that we are working on this universe server by updating its last sync date.
                universeServer.setLastSynchronizationAttempt(now());
                universeServerRepository.save(universeServer);

                // We retrieve all the roots.
                IntStream.iterate(0, offset -> offset + UNIVERSE_ROOTS_LIMIT)
                        .peek(offset -> logger.info("Processing universe roots with offset: {}", offset))
                        // For each server, we retrieve the roots.
                        .mapToObj(offset -> tapdService.getUniverseRoots(universeServer.getServerAddress(), offset, UNIVERSE_ROOTS_LIMIT).block())
                        // We check that we have a result, if not, we stop (this tells us when the offset is too high).
                        .takeWhile(universeRoots -> universeRoots != null && !universeRoots.getUniverseRoots().isEmpty())
                        // We retrieve all the roots.
                        .flatMap(universeRoots -> universeRoots.getUniverseRoots().values().stream())
                        // We select only the roots that have an asset id.
                        .filter(universeRoot -> universeRoot.getId() != null)
                        .filter(universeRoot -> universeRoot.getId().getAssetId() != null)
                        // We retrieve the asset id.
                        .map(universeRoot -> universeRoot.getId().getAssetId())
                        .peek(assetId -> logger.info("Found asset id: {}", assetId))
                        .forEach(assetId -> {
                            // For each proof type, we retrieve the leaves.
                            Stream.of(PROOF_TYPE_ISSUANCE, PROOF_TYPE_TRANSFER)
                                    .forEach(proofType -> {
                                        try {
                                            final UniverseLeavesResponse leaves = tapdService.getUniverseLeaves(universeServer.getServerAddress(), assetId, proofType).block();
                                            if (leaves == null) {
                                                logger.error("No universe leaves found for asset id - null result: {}", assetId);
                                                return;
                                            }
                                            if (leaves.getLeaves().isEmpty()) {
                                                logger.error("No universe leaves found for asset id - empty result: {}", assetId);
                                                return;
                                            }

                                            // We retrieve the proofs for each asset.
                                            leaves.getLeaves()
                                                    .stream()
                                                    .map(UniverseLeavesResponse.Leaf::getProof)
                                                    .filter(proof -> proofRepository.findByProofId(sha256(proof)).isEmpty())
                                                    .forEach(proof -> {
                                                        final AddProofRequestDTO addProofRequest = requestService.createAddProofRequest(proof, proofType);
                                                        logger.info("Request created {} for asset: {} ({})", addProofRequest.getId(), assetId, proofType);
                                                    });

                                        } catch (Exception e) {
                                            logger.error("Error while retrieving leaves for asset id {} on {} ({})",
                                                    assetId,
                                                    universeServer.getServerAddress(),
                                                    proofType,
                                                    e);
                                        }
                                    });
                        });

                // We indicate that the server was successfully synchronized.
                universeServer.setLastSynchronizationSuccess(now());
                universeServerRepository.save(universeServer);
            });
        }
    }

}
