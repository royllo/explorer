package org.royllo.explorer.core.domain.proof;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.core.util.base.BaseDomain;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Proof.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "PROOFS")
public class Proof extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** The proof ID that uniquely identifies the proof. */
    @NotBlank(message = "Proof ID is required")
    @Column(name = "PROOF_ID", updatable = false)
    private String proofId;

    /** Raw proof. */
    @NotBlank(message = "Raw proof is required")
    @Column(name = "RAW_PROOF", updatable = false)
    private String rawProof;

    /** Proof index. */
    @NotNull(message = "Proof index is required")
    @Column(name = "PROOF_INDEX", updatable = false)
    private Long proofIndex;

}
