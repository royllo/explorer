package org.royllo.explorer.core.service.proof;

import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;

/**
 * Proof service.
 */
public interface ProofService {

    /**
     * Add a new proof to the database.
     * This will create the asset if it's not already on our database.
     *
     * @param rawProof     raw proof
     * @param proofIndex   proof index
     * @param decodedProof decoded proof
     * @return the proof created
     */
    ProofDTO addProof(String rawProof,
                      long proofIndex,
                      DecodedProofResponse decodedProof);

}
