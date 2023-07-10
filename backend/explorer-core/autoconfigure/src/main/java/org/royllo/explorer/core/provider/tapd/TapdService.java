package org.royllo.explorer.core.provider.tapd;

import reactor.core.publisher.Mono;

/**
 * TAPD service.
 * <a href="https://lightning.engineering/api-docs/api/taproot-assets/">TAPD API documentation</a>
 */
public interface TapdService {

    /**
     * Decode proof.
     *
     * @param rawProof raw proof
     * @return decoded proof
     */
    Mono<DecodedProofResponse> decode(String rawProof);

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
