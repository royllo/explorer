package org.royllo.explorer.web.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Add proof request form.
 */
@Data
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddProofRequestForm {

    /** Proof. */
    @NotBlank
    String proof;

}
