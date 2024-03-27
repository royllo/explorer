package org.royllo.explorer.api.graphql.proof;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.api.util.base.BaseDataFetcher;
import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.service.proof.ProofService;
import org.springframework.data.domain.Page;

/**
 * Proof data fetcher.
 */
@DgsComponent
@RequiredArgsConstructor
public class ProofDataFetcher extends BaseDataFetcher {

    /** Proof service. */
    private final ProofService proofService;

    /**
     * Returns the proofs of a specific asset.
     *
     * @param assetId    asset id
     * @param pageNumber the page number you want
     * @param pageSize   the page size you want
     * @return list of assets corresponding to the search
     */
    @DgsQuery
    public final Page<ProofDTO> proofsByAssetId(final @InputArgument String assetId,
                                                final @InputArgument Integer pageNumber,
                                                final @InputArgument Integer pageSize) {
        return proofService.getProofsByAssetId(assetId,
                getCleanedPageNumber(pageNumber),
                getCleanedPageSize(pageSize));
    }

}
