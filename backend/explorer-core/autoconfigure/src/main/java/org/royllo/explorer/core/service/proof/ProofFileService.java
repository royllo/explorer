package org.royllo.explorer.core.service.proof;

import org.royllo.explorer.core.dto.proof.ProofFileDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Proof file service.
 */
public interface ProofFileService {

    /**
     * Add a new proof to the database.
     *
     * @param rawProof     raw proof
     * @param decodedProof decoded proof
     * @return the proof created
     */
    ProofFileDTO addProof(String rawProof,
                          DecodedProofResponse decodedProof);

    /**
     * Get a proof file by its proof file id.
     *
     * @param proofFileId proof file id
     * @return proof
     */
    Optional<ProofFileDTO> getProofFileByProofFileId(String proofFileId);

    /**
     * Returns the proof files of a specific asset.
     *
     * @param assetId  asset id
     * @param page     the page number we want to retrieve (First page is page 1)
     * @param pageSize the page size
     * @return proofs
     */
    Page<ProofFileDTO> getProofFilesByAssetId(String assetId, int page, int pageSize);

}
