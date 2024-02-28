package org.royllo.explorer.core.dto.request;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Request to claim ownership of an existing asset in royllo database.
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ClaimOwnershipRequestDTO extends RequestDTO {

    /** Proof with witness. */
    private String proofWithWitness;

}
