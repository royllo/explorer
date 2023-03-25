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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
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
                             @NonNull final DecodedProofResponse decodedProof) {
        // TODO remove rawProof parameter and add it to decodedProof ?
        logger.info("addProof - Adding {} with {}", rawProof, decodedProof);

        // We check that the proof is not in our database.
        proofRepository.findByProofId(sha256(rawProof)).ifPresent(proof -> {
            logger.info("addProof - Proof {} is already registered", rawProof);
            throw new ProofCreationException("This proof is already registered with proof id: " + proof.getProofId());
        });

        // We check that the asset exists in our database.
        final String assetId = decodedProof.getDecodedProof().getAsset().getAssetGenesis().getAssetId();
        final Optional<Asset> asset = assetRepository.findByAssetId(assetId);
        if (asset.isEmpty()) {
            // Asset does not exists.
            logger.info("addProof - Asset {} is not registered in our database", assetId);
            throw new ProofCreationException("Asset " + assetId + " is not registered in our database");
        } else {
            // Asset exists, we create the proof.
            final Proof proof = proofRepository.save(Proof.builder()
                    .proofId(sha256(rawProof))
                    .creator(ANONYMOUS_USER)
                    .asset(asset.get())
                    .rawProof(rawProof)
                    .build());
            final ProofDTO proofDTO = PROOF_MAPPER.mapToProofDTO(proof);
            logger.info("addProof - Proof created {}", proofDTO);
            return proofDTO;
        }
    }

    @Override
    public Page<ProofDTO> getProofsByAssetId(@NonNull final String assetId,
                                             final int page,
                                             final int pageSize) {
        logger.info("getProofsByAssetId - Getting proofs for assetId {}", assetId);

        // Checking constraints.
        assert page >= 1 : "Page number starts at page 1";
        assert assetRepository.findByAssetId(assetId).isPresent() : "Asset ID not found";

        // Getting results.
        final Page<ProofDTO> results = proofRepository.findByAssetAssetIdOrderByCreatedOn(assetId,
                        PageRequest.of(page - 1, pageSize))
                .map(PROOF_MAPPER::mapToProofDTO);

        // Displaying logs.
        if (results.isEmpty()) {
            logger.info("getProofsByAssetId - For assetId '{}', there is no proof", assetId);
        } else {
            logger.info("getProofsByAssetId - For assetId '{}', there are {} proofs(s): {}",
                    assetId,
                    results.getTotalElements(),
                    results.stream()
                            .map(ProofDTO::getId)
                            .map(Objects::toString)
                            .collect(joining(", ")));
        }

        return results;
    }

    @Override
    public Optional<ProofDTO> getProofByProofId(@NonNull final String proofId) {
        logger.info("getProofByProofId - Getting proof with proofId {}", proofId);

        final Optional<Proof> proof = proofRepository.findByProofId(proofId);
        if (proof.isEmpty()) {
            logger.info("getProofByProofId - Proof with proofId {} not found", proofId);
            return Optional.empty();
        } else {
            logger.info("getProofByProofId - Proof with proofId {} found: {}", proofId, proof.get());
            return proof.map(PROOF_MAPPER::mapToProofDTO);
        }
    }

    /**
     * Returns the sha256 value calculated with the parameter.
     *
     * @param value value
     * @return sha256 of value
     */
    private static String sha256(@NonNull final String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available: " + e.getMessage());
        }
    }

}
