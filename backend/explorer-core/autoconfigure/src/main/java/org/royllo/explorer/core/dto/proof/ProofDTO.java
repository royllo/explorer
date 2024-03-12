package org.royllo.explorer.core.dto.proof;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.util.enums.ProofType;

import static lombok.AccessLevel.PRIVATE;

/**
 * Taproot asset proof.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ProofDTO {

    /** Proof file name extension. */
    public static final String PROOF_FILE_NAME_EXTENSION = ".proof";

    /** Unique identifier. */
    Long id;

    /** The proof creator. */
    @NotNull(message = "{validation.proof.creator.required}")
    UserDTO creator;

    /** Target asset. */
    @NotNull(message = "{validation.proof.asset.required}")
    AssetDTO asset;

    /** Proof id - sha256(proof). */
    @NotBlank(message = "{validation.proof.proofId.required}")
    String proofId;

    /** Proof type. */
    @NotNull(message = "{validation.proof.proofType.required}")
    ProofType type;

    /**
     * Returns the proof file name.
     *
     * @return the proof file name
     */
    public String getProofFileName() {
        return proofId + PROOF_FILE_NAME_EXTENSION;
    }

}
