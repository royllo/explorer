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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.base.BaseDomain;

import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
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

    /** Asset creator. */
    @NotNull(message = "Proof creator is required")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_USER_CREATOR", nullable = false)
    private User creator;

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
    @Column(name = "EXCLUSION_PROOFS", updatable = false)
    private List<String> exclusionProofs;

}
