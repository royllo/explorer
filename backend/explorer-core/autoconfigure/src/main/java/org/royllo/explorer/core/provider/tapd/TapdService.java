package org.royllo.explorer.core.provider.tapd;

import org.royllo.explorer.core.util.enums.ProofType;
import reactor.core.publisher.Mono;

/**
 * TAPD service.
 * <a href="https://lightning.engineering/api-docs/api/taproot-assets/">TAPD API documentation</a>
 */
public interface TapdService {

    /**
     * Decode proof.
     * If there are several proofs, is the latest proof is returned.
     *
     * @param proof proof
     * @return decoded proof
     */
    Mono<DecodedProofResponse> decode(String proof);

    /**
     * Decode proof at a certain depth.
     * If there are several proofs, the latest proof is returned.
     *
     * @param proof        proof
     * @param proofAtDepth proof at depth
     * @param metaReveal   meta reveal
     * @return decoded proof
     */
    Mono<DecodedProofResponse> decode(String proof, long proofAtDepth, boolean metaReveal);

    /**
     * Get universe roots.
     *
     * @param serverAddress server address
     * @param offset        The offset for the page.
     * @param limit         The length limit for the page.
     * @return universe roots
     */
    Mono<UniverseRootsResponse> getUniverseRoots(String serverAddress,
                                                 int offset,
                                                 int limit);

    /**
     * Get universe leaves.
     *
     * @param serverAddress server address
     * @param assetId       asset ID
     * @param proofType     proof type
     * @return universe leaves
     */
    Mono<UniverseLeavesResponse> getUniverseLeaves(String serverAddress,
                                                   String assetId,
                                                   ProofType proofType);

}
