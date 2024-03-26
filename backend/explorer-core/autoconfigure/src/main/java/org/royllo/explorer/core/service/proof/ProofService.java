package org.royllo.explorer.core.service.proof;

import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.util.enums.ProofType;
import org.royllo.explorer.core.util.validator.PageNumber;
import org.royllo.explorer.core.util.validator.PageSize;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Proof service.
 */
public interface ProofService {

    /**
     * Add a new proof.
     * The asset must already be existing in our database.
     *
     * @param proof        proof
     * @param proofType    proof type
     * @param decodedProof decoded proof
     * @return the proof created
     */
    ProofDTO addProof(String proof,
                      ProofType proofType,
                      DecodedProofResponse decodedProof);

    /**
     * Get a proof by its proof id.
     *
     * @param proofId proof id
     * @return proof
     */
    Optional<ProofDTO> getProofByProofId(String proofId);

    /**
     * Get proofs of a specific asset.
     *
     * @param assetId    asset id
     * @param pageNumber the page number we want to retrieve (First page is page 1)
     * @param pageSize   the page size
     * @return proofs
     */
    Page<ProofDTO> getProofsByAssetId(String assetId,
                                      @PageNumber int pageNumber,
                                      @PageSize int pageSize);

}
