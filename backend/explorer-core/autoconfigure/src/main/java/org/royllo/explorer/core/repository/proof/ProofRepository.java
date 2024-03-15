package org.royllo.explorer.core.repository.proof;

import org.royllo.explorer.core.domain.proof.Proof;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link Proof} repository.
 */
@Repository
public interface ProofRepository extends JpaRepository<Proof, Long> {

    /**
     * Find a proof by its proof id.
     *
     * @param proofId proof id
     * @return proof
     */
    Optional<Proof> findByProofId(String proofId);

    /**
     * Returns all proofs of an asset.
     *
     * @param assetId  asset id
     * @param pageable pagination parameters
     * @return proofs
     */
    Page<Proof> findByAssetAssetIdOrderByCreatedOn(String assetId, Pageable pageable);

}
