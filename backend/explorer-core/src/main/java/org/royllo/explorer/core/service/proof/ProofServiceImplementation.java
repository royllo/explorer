package org.royllo.explorer.core.service.proof;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.proof.Proof;
import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.repository.proof.ProofRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * Proof implementation service.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class ProofServiceImplementation extends BaseService implements ProofService {

    /** Proof repository. */
    private final ProofRepository proofRepository;

    @Override
    public ProofDTO createProof(final ProofDTO newProof) {
        assert newProof.getId() == null : "Your proof already has an ID, this method can only be used to create a proof";

        // Creating the proof.
        Proof savedProof = proofRepository.save(PROOF_MAPPER.mapToProof(newProof));

        return PROOF_MAPPER.mapToProofDTO(savedProof);
    }

}
