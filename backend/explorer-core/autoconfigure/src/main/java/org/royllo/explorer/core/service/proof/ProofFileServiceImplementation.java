package org.royllo.explorer.core.service.proof;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.domain.proof.ProofFile;
import org.royllo.explorer.core.dto.proof.ProofFileDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.proof.ProofFileRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.exceptions.proof.ProofCreationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;

/**
 * {@link ProofFileService} implementation.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension", "unused"})
public class ProofFileServiceImplementation extends BaseService implements ProofFileService {

    /** Asset repository. */
    private final AssetRepository assetRepository;

    /** Proof repository. */
    private final ProofFileRepository proofFileRepository;

    @Override
    public ProofFileDTO addProof(@NonNull final String rawProof,
                                 @NonNull final DecodedProofResponse decodedProof) {
        logger.info("Adding {} with {}", rawProof, decodedProof);

        // We check that the proof is not in our database.
        proofFileRepository.findByProofFileId(sha256(rawProof)).ifPresent(proof -> {
            logger.info("Proof file {} is already registered", rawProof);
            throw new ProofCreationException("This proof file is already registered with proof id: " + proof.getProofFileId());
        });

        // We check that the asset exists in our database.
        final String assetId = decodedProof.getDecodedProof().getAsset().getAssetGenesis().getAssetId();
        final Optional<Asset> asset = assetRepository.findByAssetId(assetId);
        if (asset.isEmpty()) {
            // Asset does not exists.
            logger.info("Asset {} is not registered in our database", assetId);
            throw new ProofCreationException("Asset " + assetId + " is not registered in our database");
        } else {
            // Asset exists, we create the proof.
            final ProofFile proofFile = proofFileRepository.save(ProofFile.builder()
                    .proofFileId(sha256(rawProof))
                    .creator(ANONYMOUS_USER)
                    .asset(asset.get())
                    .rawProof(rawProof)
                    .build());
            final ProofFileDTO proofFileDTO = PROOF_FILE_MAPPER.mapToProofFileDTO(proofFile);
            logger.info("Proof file created with id {} : {}", proofFileDTO.getId(), proofFileDTO);
            return proofFileDTO;
        }
    }

    @Override
    public Optional<ProofFileDTO> getProofFileByProofFileId(@NonNull final String proofFileId) {
        logger.info("Getting proof file with proofFileId {}", proofFileId);

        final Optional<ProofFile> proof = proofFileRepository.findByProofFileId(proofFileId);
        if (proof.isEmpty()) {
            logger.info("Proof file with proofFileId {} not found", proofFileId);
            return Optional.empty();
        } else {
            logger.info("Proof file with proofFileId {} found: {}", proofFileId, proof.get());
            return proof.map(PROOF_FILE_MAPPER::mapToProofFileDTO);
        }
    }

    @Override
    public Page<ProofFileDTO> getProofFilesByAssetId(@NonNull final String assetId,
                                                     final int page,
                                                     final int pageSize) {
        logger.info("Getting proof files for assetId {}", assetId);

        // Checking constraints.
        assert page >= 1 : "Page number starts at page 1";
        assert assetRepository.findByAssetId(assetId).isPresent() : "Asset ID not found";

        // Getting results.
        final Page<ProofFileDTO> results = proofFileRepository.findByAssetAssetIdOrderByCreatedOn(assetId,
                        PageRequest.of(page - 1, pageSize))
                .map(PROOF_FILE_MAPPER::mapToProofFileDTO);

        // Displaying logs.
        if (results.isEmpty()) {
            logger.info("For assetId '{}', there is no proof file", assetId);
        } else {
            logger.info("For assetId '{}', there are {} proof file(s): {}",
                    assetId,
                    results.getTotalElements(),
                    results.stream()
                            .map(ProofFileDTO::getId)
                            .map(Objects::toString)
                            .collect(joining(", ")));
        }

        return results;
    }

}
