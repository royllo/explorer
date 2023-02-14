package org.royllo.explorer.core.dto.request;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A request to add proof to royllo explorer.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddProofRequestDTO extends RequestDTO {

    /** Proof that validates the asset information. */
    String rawProof;

}
