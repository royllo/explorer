package org.royllo.explorer.core.provider.tapd;

import reactor.core.publisher.Mono;

/**
 * TAPD proof service.
 * <a href="https://lightning.engineering/api-docs/api/taproot-assets/">TAPD API documentation</a>
 */
public interface TapdProofService {

    /**
     * Decode proof.
     *
     * @param rawProof   raw proof
     * @param proofIndex proof index
     * @return decoded proof
     */
    Mono<DecodedProofResponse> decode(String rawProof, long proofIndex);

}
