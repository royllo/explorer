package org.royllo.explorer.core.repository.proof;

import org.royllo.explorer.core.domain.proof.ProofFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link ProofFile} repository.
 */
@Repository
public interface ProofFileRepository extends JpaRepository<ProofFile, Long> {

    /**
     * Find a proof file by its proofId.
     *
     * @param proofFileId proof file id
     * @return proof file
     */
    Optional<ProofFile> findByProofFileId(String proofFileId);

    /**
     * Returns all the proof files of an Asset.
     *
     * @param assetId  asset id
     * @param pageable page parameters
     * @return proof files
     */
    Page<ProofFile> findByAssetAssetIdOrderByCreatedOn(String assetId, Pageable pageable);

}
