package org.royllo.explorer.web.controller.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Claim asset ownership request form.
 */
@Data
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ClaimAssetOwnershipRequestForm {

    /** Proof with witness. */
    @NotBlank
    String proofWithWitness;

}
