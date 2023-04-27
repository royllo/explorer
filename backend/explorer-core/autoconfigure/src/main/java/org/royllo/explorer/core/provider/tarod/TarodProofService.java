package org.royllo.explorer.core.provider.tarod;

import reactor.core.publisher.Mono;

/**
 * Tarod proof service.
 * <a href="https://lightning.engineering/api-docs/api/taro/rest-endpoints/index.html">Tarod API documentation</a>
 */
public interface TarodProofService {

    /**
     * Decode proof.
     *
     * @param rawProof   raw proof
     * @param proofIndex proof index
     * @return decoded proof
     */
    Mono<DecodedProofResponse> decode(String rawProof, long proofIndex);

}
