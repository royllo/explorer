package org.royllo.explorer.core.domain.proof;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.base.BaseDomain;
import org.royllo.explorer.core.util.enums.ProofType;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;

/**
 * Taproot asset proof.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor(access = PACKAGE)
@Builder
@Entity
@Table(name = "PROOF")
public class Proof extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** Proof creator. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_USER_CREATOR", nullable = false)
    private User creator;

    /** Target asset. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_ASSET", nullable = false)
    private Asset asset;

    /** The proof ID that uniquely identifies the proof - sha256(proof). */
    @Column(name = "PROOF_ID", nullable = false, updatable = false)
    private String proofId;

    /** Proof type. */
    @Enumerated(STRING)
    @Column(name = "PROOF_TYPE", updatable = false)
    private ProofType type;

}
