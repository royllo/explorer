package org.royllo.explorer.core.service.proof;

import org.royllo.explorer.core.dto.proof.ProofDTO;

/**
 * Proof service.
 */
public interface ProofService {

    /**
     * Create a new proof.
     *
     * @param newProof new proof
     * @return the proof created
     */
    ProofDTO createProof(ProofDTO newProof);

}
