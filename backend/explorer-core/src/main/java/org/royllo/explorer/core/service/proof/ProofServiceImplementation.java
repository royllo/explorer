package org.royllo.explorer.core.service.proof;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.domain.proof.Proof;
import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.proof.ProofRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.exceptions.proof.ProofCreationException;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;

/**
 * Proof implementation service.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class ProofServiceImplementation extends BaseService implements ProofService {

    /** Asset repository. */
    private final AssetRepository assetRepository;

    /** Proof repository. */
    private final ProofRepository proofRepository;

    @Override
    public ProofDTO addProof(@NonNull final String rawProof,
                             final long proofIndex,
                             @NonNull final DecodedProofResponse decodedProof) {
        logger.info("addProof - Adding {}", decodedProof);

        // We check that the proof is not in our database.
        proofRepository.findByProofId(calculateProofId(rawProof, proofIndex)).ifPresent(proof -> {
            throw new ProofCreationException("This proof is already registered with proof id: " + proof.getProofId());
        });

        // We check that the asset exists in our database.
        final String assetId = decodedProof.getDecodedProof().getAsset().getAssetGenesis().getAssetId();
        final Optional<Asset> asset = assetRepository.findByAssetId(assetId);
        if (asset.isEmpty()) {
            // Asset does not exists.
            throw new ProofCreationException("Asset " + assetId + " is not registered in our database");
        } else {
            // We create the proof.
            final Proof proof = proofRepository.save(Proof.builder()
                    .proofId(calculateSHA256(rawProof + ":" + proofIndex))
                    .creator(ANONYMOUS_USER)
                    .asset(asset.get())
                    .rawProof(rawProof)
                    .proofIndex(proofIndex)
                    .txMerkleProof(decodedProof.getDecodedProof().getTxMerkleProof())
                    .inclusionProof(decodedProof.getDecodedProof().getInclusionProof())
                    .exclusionProofs(decodedProof.getDecodedProof().getExclusionProofs())
                    .build());
            return PROOF_MAPPER.mapToProofDTO(proof);
        }
    }

    @Override
    public Optional<ProofDTO> getProofByProofId(@NonNull final String proofId) {
        return proofRepository.findByProofId(proofId).map(PROOF_MAPPER::mapToProofDTO);
    }

    /**
     * Returns the proof id calculated from raw proof and proof index.
     * SHA256(rawProof + ":" + proofIndex).
     *
     * @param rawProof   raw proof
     * @param proofIndex proof index
     * @return calculated proof id
     */
    private String calculateProofId(@NonNull final String rawProof,
                                    final long proofIndex) {
        return calculateSHA256(rawProof + ":" + proofIndex);
    }

    /**
     * Returns the sha256 value calculated with the parameter.
     *
     * @param value value
     * @return sha256 of value
     */
    private String calculateSHA256(@NonNull final String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available: " + e.getMessage());
        }
    }

}
