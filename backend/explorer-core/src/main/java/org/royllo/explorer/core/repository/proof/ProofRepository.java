package org.royllo.explorer.core.repository.proof;

import org.royllo.explorer.core.domain.proof.Proof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Proof repository.
 */
@Repository
public interface ProofRepository extends JpaRepository<Proof, Long> {

    /**
     * Find a proof by its proofId.
     *
     * @param proofId proof id
     * @return proof if proof is found
     */
    Optional<Proof> findByProofId(String proofId);

}
