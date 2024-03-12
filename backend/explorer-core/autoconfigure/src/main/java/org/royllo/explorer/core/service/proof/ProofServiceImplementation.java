package org.royllo.explorer.core.service.proof;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.domain.proof.Proof;
import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.provider.storage.ContentService;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.proof.ProofRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.enums.ProofType;
import org.royllo.explorer.core.util.exceptions.proof.ProofCreationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static org.royllo.explorer.core.dto.proof.ProofDTO.PROOF_FILE_NAME_EXTENSION;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER;

/**
 * {@link ProofService} implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class ProofServiceImplementation extends BaseService implements ProofService {

    /** Proof minimum size - Under this size, the proof is displayed without preview. */
    private static final int PROOF_MINIMUM_SIZE = 6;

    /** Proof preview size - The size of the preview on both ends. */
    private static final int PROOF_PREVIEW_SIZE = 3;

    /** Asset repository. */
    private final AssetRepository assetRepository;

    /** Proof repository. */
    private final ProofRepository proofRepository;

    /** Content service. */
    private final ContentService contentService;

    @Override
    public ProofDTO addProof(@NonNull final String proof,
                             @NonNull final ProofType proofType,
                             @NonNull final DecodedProofResponse decodedProof) {
        logger.info("Adding {} with {}", getProofAbstract(proof), decodedProof);

        // We check that the proof is not in our database.
        proofRepository.findByProofId(sha256(proof)).ifPresent(existingProof -> {
            logger.info("Proof {} is already registered", getProofAbstract(proof));
            throw new ProofCreationException("This proof is already registered with proof id: " + existingProof.getProofId());
        });

        // We check that the asset exists in our database.
        final String assetId = decodedProof.getDecodedProof().getAsset().getAssetGenesis().getAssetId();
        final Optional<Asset> asset = assetRepository.findByAssetId(assetId);
        if (asset.isEmpty()) {
            // Asset does not exists.
            logger.info("Asset {} is not registered in our database", assetId);
            throw new ProofCreationException("Asset " + assetId + " is not registered in our database");
        } else {
            // Asset exists, we create the proof (in content service and database).
            contentService.storeFile(proof.getBytes(), sha256(proof) + PROOF_FILE_NAME_EXTENSION);

            final Proof proofToCreate = proofRepository.save(Proof.builder()
                    .proofId(sha256(proof))
                    .creator(ANONYMOUS_USER)
                    .asset(asset.get())
                    .type(proofType)
                    .build());
            final ProofDTO proofDTO = PROOF_MAPPER.mapToProofDTO(proofToCreate);
            logger.info("Proof '{}' with id {}", getProofAbstract(proof), proofDTO.getId());
            return proofDTO;
        }
    }

    @Override
    public Optional<ProofDTO> getProofByProofId(@NonNull final String proofId) {
        logger.info("Getting proof with proof id {}", proofId);

        final Optional<Proof> proof = proofRepository.findByProofId(proofId);
        if (proof.isEmpty()) {
            logger.info("Proof with proof id {} not found", proofId);
            return Optional.empty();
        } else {
            logger.info("Proof with proof id {} found: {}", proofId, proof.get());
            return proof.map(PROOF_MAPPER::mapToProofDTO);
        }
    }

    @Override
    public Page<ProofDTO> getProofByAssetId(@NonNull final String assetId,
                                            final int page,
                                            final int pageSize) {
        logger.info("Getting proofs for assetId {}", assetId);

        // Checking constraints.
        assert page >= 1 : "Page number starts at page 1";
        assert assetRepository.findByAssetId(assetId).isPresent() : "Asset ID not found";

        // Getting results.
        final Page<ProofDTO> results = proofRepository.findByAssetAssetIdOrderByCreatedOn(assetId,
                        PageRequest.of(page - 1, pageSize))
                .map(PROOF_MAPPER::mapToProofDTO);

        // Displaying logs.
        if (results.isEmpty()) {
            logger.info("For assetId '{}', there is no proof", assetId);
        } else {
            logger.info("For assetId '{}', there are {} proof(s): {}",
                    assetId,
                    results.getTotalElements(),
                    results.stream()
                            .map(ProofDTO::getId)
                            .map(Objects::toString)
                            .collect(joining(", ")));
        }

        return results;
    }

    /**
     * Returns an abstract of proof (for logs).
     *
     * @param proof proof
     * @return proof abstract
     */
    private String getProofAbstract(final String proof) {
        // If proof is null, return null.
        if (proof == null) {
            return null;
        }
        // If proof is too small for substring, return proof.
        if (proof.length() <= PROOF_MINIMUM_SIZE) {
            return proof;
        }
        return proof.substring(0, PROOF_PREVIEW_SIZE) + "..." + proof.substring(proof.length() - PROOF_PREVIEW_SIZE);
    }

}
