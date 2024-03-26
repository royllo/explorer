package org.royllo.explorer.core.service.proof;

import io.netty.util.internal.StringUtil;
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
import org.royllo.explorer.core.util.validator.PageNumber;
import org.royllo.explorer.core.util.validator.PageSize;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import static org.royllo.explorer.core.dto.proof.ProofDTO.PROOF_FILE_NAME_EXTENSION;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER;

/**
 * {@link ProofService} implementation.
 */
@Service
@Validated
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
    public ProofDTO addProof(final @NonNull String proof,
                             final @NonNull ProofType proofType,
                             final @NonNull DecodedProofResponse decodedProof) {
        logger.info("Adding a new proof {}", getProofAbstract(proof));
        final String assetId = decodedProof.getDecodedProof().getAsset().getAssetGenesis().getAssetId();

        // =============================================================================================================
        // Checking constraints.

        // We check that the proof is not already in our database.
        proofRepository.findByProofId(sha256(proof)).ifPresent(existingProof -> {
            throw new ProofCreationException("This proof is already registered with proof id: " + existingProof.getProofId());
        });

        // We check that the asset exists in our database.
        Asset asset = assetRepository.findByAssetId(assetId)
                .orElseThrow(() -> new ProofCreationException("Asset " + assetId + " is not registered in our database"));

        // =============================================================================================================
        // We create the asset.

        // Asset exists, we create the proof (in content service and database).
        contentService.storeFile(proof.getBytes(), sha256(proof) + PROOF_FILE_NAME_EXTENSION);
        final Proof proofToCreate = proofRepository.save(Proof.builder()
                .proofId(sha256(proof))
                .creator(ANONYMOUS_USER)
                .asset(asset)
                .type(proofType)
                .build());
        final ProofDTO proofDTO = PROOF_MAPPER.mapToProofDTO(proofToCreate);
        logger.info("Proof '{}' with id {}", getProofAbstract(proof), proofDTO.getId());
        return proofDTO;
    }

    @Override
    public Optional<ProofDTO> getProofByProofId(final String proofId) {
        logger.info("Getting proof with proof id {}", proofId);

        return proofRepository.findByProofId(proofId)
                .map(PROOF_MAPPER::mapToProofDTO)
                .map(proof -> {
                    logger.info("Asset with id {} found: {}", proofId, proof);
                    return proof;
                })
                .or(() -> {
                    logger.info("Proof with proof id {} not found", proofId);
                    return Optional.empty();
                });
    }

    @Override
    public Page<ProofDTO> getProofsByAssetId(final String assetId,
                                             final @PageNumber int pageNumber,
                                             final @PageSize int pageSize) {
        logger.info("Getting proofs for assetId {}", assetId);

        return proofRepository.findByAssetAssetIdOrderByCreatedOn(assetId, getPageRequest(pageNumber, pageSize))
                .map(PROOF_MAPPER::mapToProofDTO);
    }

    /**
     * Returns an abstract of a proof file (for logs).
     *
     * @param proof proof
     * @return proof abstract
     */
    private String getProofAbstract(final String proof) {
        // If proof is too small for substring, return proof.
        if (StringUtil.length(proof) <= PROOF_MINIMUM_SIZE) {
            return proof;
        } else {
            return proof.substring(0, PROOF_PREVIEW_SIZE)
                    + "..."
                    + proof.substring(proof.length() - PROOF_PREVIEW_SIZE);
        }
    }

}
