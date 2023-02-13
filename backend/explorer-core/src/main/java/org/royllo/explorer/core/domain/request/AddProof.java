package org.royllo.explorer.core.domain.request;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A request to add proof to royllo explorer.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "REQUESTS_ADD_PROOF")
@DiscriminatorValue("ADD_ASSET")
public class AddProof extends Request {

    /** Proof that validates the asset information. */
    @NotBlank(message = "Raw proof is required")
    @Column(name = "RAW_PROOF", updatable = false)
    private String rawProof;

}
