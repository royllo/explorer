package org.royllo.explorer.api.graphql.proof;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsInvalidInputArgumentException;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.proof.ProofFileDTO;
import org.royllo.explorer.core.service.proof.ProofFileService;
import org.springframework.data.domain.Page;

import java.util.Objects;

import static org.royllo.explorer.api.configuration.APIConfiguration.DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.api.configuration.APIConfiguration.FIRST_PAGE;
import static org.royllo.explorer.api.configuration.APIConfiguration.MAXIMUM_PAGE_SIZE;

/**
 * Proof file data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class ProofFileDataFetcher extends BaseDataFetcher {

    /** Proof file service. */
    private final ProofFileService proofService;

    /**
     * Returns the proof files of a specific asset.
     *
     * @param assetId  asset id
     * @param page     the page number you want
     * @param pageSize the page size you want
     * @return list of assets corresponding to the search
     */
    @DgsQuery
    public final Page<ProofFileDTO> proofFilesByAssetId(final @InputArgument String assetId,
                                                        final @InputArgument Integer page,
                                                        final @InputArgument Integer pageSize) {
        // Checking maximum page size.
        if (Objects.requireNonNullElse(pageSize, DEFAULT_PAGE_SIZE) > MAXIMUM_PAGE_SIZE) {
            throw new DgsInvalidInputArgumentException("Page size can't be superior to " + MAXIMUM_PAGE_SIZE, null);
        }

        // Return the results.
        return proofService.getProofFilesByAssetId(assetId,
                Objects.requireNonNullElse(page, FIRST_PAGE),
                Objects.requireNonNullElse(pageSize, DEFAULT_PAGE_SIZE));
    }

}
