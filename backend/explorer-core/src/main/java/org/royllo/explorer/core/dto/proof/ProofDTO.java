package org.royllo.explorer.core.dto.proof;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.dto.user.UserDTO;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * Proof DTO.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ProofDTO {

    /** Unique identifier. */
    Long id;

    /** Proof id (rawProof + ":" + proofIndex). */
    String proofId;

    /** The proof creator. */
    UserDTO creator;

    /** Raw proof. */
    String rawProof;

    /** Proof index. */
    long proofIndex;

    /** Transaction merkle proof. */
    String txMerkleProof;

    /** Inclusion proof. */
    String inclusionProof;

    /** Exclusion proofs. */
    List<String> exclusionProofs;

    // TODO Add author!

}
