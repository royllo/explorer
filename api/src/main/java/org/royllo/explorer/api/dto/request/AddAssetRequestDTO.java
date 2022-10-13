package org.royllo.explorer.api.dto.request;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A request to add asset to royllo explorer.
 */
@Getter
@SuperBuilder
@ToString
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddAssetRequestDTO extends RequestDTO {

    /** The full genesis information encoded in a portable manner, so it can be easily copied and pasted for address creation. */
    String genesisBootstrapInformation;

    /** Proof that validates the asset information. */
    String proof;

}
