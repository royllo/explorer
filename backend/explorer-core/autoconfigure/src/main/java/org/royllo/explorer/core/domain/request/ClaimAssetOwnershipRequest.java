package org.royllo.explorer.core.domain.request;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Request to claim ownership of an existing asset in royllo database.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "REQUEST_CLAIM_ASSET_OWNERSHIP")
@DiscriminatorValue("CLAIM_ASSET_OWNERSHIP")
public class ClaimAssetOwnershipRequest extends Request {

    /** Proof with witness. */
    @Column(name = "PROOF_WITH_WITNESS")
    private String proofWithWitness;

}
