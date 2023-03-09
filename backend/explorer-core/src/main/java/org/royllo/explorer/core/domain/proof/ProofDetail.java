package org.royllo.explorer.core.domain.proof;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Proof detail.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Entity
@Table(name = "PROOFS_DETAILS")
public class ProofDetail {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** Target proof. */
    @NotNull(message = "Target asset is required")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "FK_PROOF", nullable = false)
    private Proof asset;


    /** Proof index. */
    @NotNull(message = "Proof index is required")
    @Column(name = "PROOF_INDEX", updatable = false)
    private Long proofIndex;

    /** Transaction merkle proof. */
    @NotNull(message = "Transaction merkle proof is required")
    @Column(name = "TX_MERKLE_PROOF", updatable = false)
    private String txMerkleProof;

    /** Inclusion proof. */
    @NotNull(message = "Inclusion proof is required")
    @Column(name = "INCLUSION_PROOF", updatable = false)
    private String inclusionProof;

    /** Exclusion proofs. */
    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "PROOFS_EXCLUSION_PROOFS", joinColumns = @JoinColumn(name = "FK_PROOF"))
    @Column(name = "EXCLUSION_PROOF", updatable = false)
    private List<String> exclusionProofs;

}
