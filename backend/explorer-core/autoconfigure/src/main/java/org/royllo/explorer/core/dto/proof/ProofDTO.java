package org.royllo.explorer.core.dto.proof;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.user.UserDTO;

import static lombok.AccessLevel.PRIVATE;

/**
 * Taproot proof.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ProofDTO {

    /** Unique identifier. */
    Long id;

    /** The proof creator. */
    @NotNull(message = "Proof creator is required")
    UserDTO creator;

    /** Target asset. */
    @NotNull(message = "Target asset is required")
    AssetDTO asset;

    /** Proof id  - sha256(rawProof). */
    @NotBlank(message = "Proof ID is required")
    String proofId;

    /** Raw proof. */
    @NotBlank(message = "Raw proof is required")
    String rawProof;

}
