package org.royllo.explorer.core.dto.proof;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * Proof detail DTO.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ProofDetailDTO {

    /** Proof index. */
    long proofIndex;

    /** Transaction merkle proof. */
    String txMerkleProof;

    /** Inclusion proof. */
    String inclusionProof;

    /** Exclusion proofs. */
    List<String> exclusionProofs;

}
