package org.royllo.explorer.core.provider.tapd;

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
     * @param rawProof raw proof
     * @return decoded proof
     */
    Mono<DecodedProofResponse> decode(String rawProof);

    /**
     * Decode proof at a certain depth.
     * If there are several proofs, is the latest proof is returned.
     *
     * @param rawProof     raw proof
     * @param proofAtDepth proof at depth
     * @return decoded proof
     */
    Mono<DecodedProofResponse> decode(String rawProof, int proofAtDepth);

    /**
     * Get universe roots.
     *
     * @param serverAddress server address
     * @return universe roots
     */
    Mono<UniverseRootsResponse> getUniverseRoots(String serverAddress);

    /**
     * Get universe leaves.
     *
     * @param serverAddress server address
     * @param assetId       asset ID
     * @return universe leaves
     */
    Mono<UniverseLeavesResponse> getUniverseLeaves(String serverAddress,
                                                   String assetId);

}
