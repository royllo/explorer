package org.royllo.explorer.web.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Proof form command.
 */
@Data
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ProofFormCommand {

    /** Raw proof. */
    @NotBlank
    String rawProof;

}
