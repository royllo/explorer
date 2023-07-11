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
 * Taproot proof.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ProofDTO {

    /** Raw proof minimum size - Under this size, the raw proof is displayed without preview. */
    public static final int RAW_PROOF_MINIMUM_SIZE = 6;

    /** Raw proof preview size - The size of the preview on both ends. */
    public static final int RAW_PROOF_PREVIEW_SIZE = 3;

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
    @ToString.Exclude
    @NotBlank(message = "Raw proof is required")
    String rawProof;

    /**
     * Returns an abstract of proof (for logs).
     * Three first characters, three dots, three last characters.
     *
     * @return proof abstract
     */
    @ToString.Include(name = "rawProofAbstract")
    public String getRawProofAbstract() {
        // If raw proof is null, return null.
        if (rawProof == null) {
            return null;
        }
        // If raw proof is too small for substring, return raw proof.
        if (rawProof.length() <= RAW_PROOF_MINIMUM_SIZE) {
            return rawProof;
        }
        return rawProof.substring(0, RAW_PROOF_PREVIEW_SIZE) + "..." + rawProof.substring(rawProof.length() - RAW_PROOF_PREVIEW_SIZE);
    }

}
