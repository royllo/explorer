package org.royllo.explorer.core.repository.proof;

import org.royllo.explorer.core.domain.proof.Proof;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * Returns all the proofs of an Asset.
     *
     * @param assetId  asset id
     * @param pageable page configuration
     * @return proofs
     */
    Page<Proof> findByAssetAssetIdOrderByCreatedOn(String assetId, Pageable pageable);

}
