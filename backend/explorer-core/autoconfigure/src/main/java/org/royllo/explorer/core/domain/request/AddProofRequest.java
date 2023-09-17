package org.royllo.explorer.core.domain.request;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.domain.asset.Asset;

import static jakarta.persistence.FetchType.EAGER;

/**
 * Request to add proof to royllo database.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "REQUEST_ADD_PROOF")
@DiscriminatorValue("ADD_PROOF")
public class AddProofRequest extends Request {

    /** Proof that validates the asset information. */
    @Column(name = "RAW_PROOF", updatable = false)
    private String rawProof;

    /** The asset created/updated by this request. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_ASSET")
    private Asset asset;

}
