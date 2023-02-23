package org.royllo.explorer.core.repository.proof;

import org.royllo.explorer.core.domain.proof.Proof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Proof repository.
 */
@Repository
public interface ProofRepository extends JpaRepository<Proof, Long> {


}
