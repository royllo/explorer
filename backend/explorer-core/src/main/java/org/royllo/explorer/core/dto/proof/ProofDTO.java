package org.royllo.explorer.core.dto.proof;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.user.UserDTO;

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

    /** The proof creator. */
    UserDTO creator;

    /** Target asset. */
    AssetDTO asset;

    /** Proof id  - sha256(rawProof). */
    String proofId;

    /** Raw proof. */
    String rawProof;

}
