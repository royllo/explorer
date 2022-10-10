package org.royllo.explorer.api.domain.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * A request to add asset to royllo explorer.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "REQUESTS_ADD_ASSET")
@DiscriminatorValue("ADD_ASSET")
public class AddAssetRequest extends Request {

    /** The full genesis information encoded in a portable manner, so it can be easily copied and pasted for address creation. */
    @NotBlank(message = "Genesis bootstrap information is required")
    @Column(name = "GENESIS_BOOTSTRAP_INFORMATION", updatable = false)
    private String genesisBootstrapInformation;

    /** Proof that validates the asset information. */
    @NotBlank(message = "Proof is required")
    @Column(name = "PROOF", updatable = false)
    private String proof;

}
