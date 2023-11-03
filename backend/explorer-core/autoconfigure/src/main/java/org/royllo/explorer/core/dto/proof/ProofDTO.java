package org.royllo.explorer.core.dto.proof;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.user.UserDTO;

import static lombok.AccessLevel.PRIVATE;

/**
 * Taproot asset proof.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ProofDTO {

    /** Proof minimum size - Under this size, the raw proof is displayed without preview. */
    private static final int PROOF_MINIMUM_SIZE = 6;

    /** Proof preview size - The size of the preview on both ends. */
    private static final int PROOF_PREVIEW_SIZE = 3;

    /** Unique identifier. */
    Long id;

    /** The proof creator. */
    @NotNull(message = "Proof creator is required")
    UserDTO creator;

    /** Target asset. */
    @NotNull(message = "Target asset is required")
    AssetDTO asset;

    /** Proof id - sha256(proof). */
    @NotBlank(message = "Proof file ID is required")
    String proofId;

    /** Proof. */
    @ToString.Exclude
    @NotBlank(message = "Proof is required")
    String proof;

    /**
     * Returns an abstract of proof (for logs).
     * Three first characters, three dots, three last characters.
     *
     * @return proof abstract
     */
    @ToString.Include(name = "proofAbstract")
    public String getProofAbstract() {
        // If raw proof is null, return null.
        if (proof == null) {
            return null;
        }
        // If raw proof is too small for substring, return raw proof.
        if (proof.length() <= PROOF_MINIMUM_SIZE) {
            return proof;
        }
        return proof.substring(0, PROOF_PREVIEW_SIZE) + "..." + proof.substring(proof.length() - PROOF_PREVIEW_SIZE);
    }

}
