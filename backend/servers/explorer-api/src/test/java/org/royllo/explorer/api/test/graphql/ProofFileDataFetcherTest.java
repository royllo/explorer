package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.ProofFilesByAssetIdGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.ProofFilesByAssetIdProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.ProofFile;
import org.royllo.explorer.api.graphql.generated.types.ProofFilePage;
import org.royllo.explorer.api.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_ID;

@SpringBootTest
@DisplayName("ProofFileDataFetcher tests")
public class ProofFileDataFetcherTest extends BaseTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("proofFilesByAssetId()")
    public void proofFilesByAssetId() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                ProofFilesByAssetIdGraphQLQuery.newRequest().assetId(ACTIVE_ROYLLO_COIN_ASSET_ID).page(1).pageSize(10).build(),
                new ProofFilesByAssetIdProjectionRoot<>().content()
                        .creator().userId().username().parent()
                        .asset().assetId().parent()
                        .proofFileId()
                        .rawProof()
                        .parent()
                        .totalElements()
                        .totalPages());

        ProofFilePage proofPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.ProofFilesByAssetId,
                new TypeRef<>() {
                });

        assertEquals(3, proofPage.getTotalElements());
        assertEquals(1, proofPage.getTotalPages());

        // Proof 1.
        final Optional<ProofFile> proof1 = proofPage.getContent()
                .stream()
                .filter(proof -> proof.getProofFileId().equals(ACTIVE_ROYLLO_COIN_PROOF_1_PROOF_ID))
                .findFirst();
        assertTrue(proof1.isPresent());
        assertEquals(ANONYMOUS_USER_ID, proof1.get().getCreator().getUserId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof1.get().getAsset().getAssetId());
        assertEquals(ACTIVE_ROYLLO_COIN_PROOF_1_RAW_PROOF, proof1.get().getRawProof());

        // Proof 2.
        final Optional<ProofFile> proof2 = proofPage.getContent().stream()
                .filter(proof -> proof.getProofFileId().equals(ACTIVE_ROYLLO_COIN_PROOF_2_PROOF_ID))
                .findFirst();
        assertTrue(proof2.isPresent());
        assertEquals(ANONYMOUS_USER_ID, proof2.get().getCreator().getUserId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof2.get().getAsset().getAssetId());
        assertEquals(ACTIVE_ROYLLO_COIN_PROOF_2_RAW_PROOF, proof2.get().getRawProof());

        // Proof 3.
        final Optional<ProofFile> proof3 = proofPage.getContent().stream()
                .filter(proof -> proof.getProofFileId().equals(ACTIVE_ROYLLO_COIN_PROOF_3_PROOF_ID))
                .findFirst();
        assertTrue(proof3.isPresent());
        assertEquals(ANONYMOUS_USER_ID, proof3.get().getCreator().getUserId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof3.get().getAsset().getAssetId());
        assertEquals(ACTIVE_ROYLLO_COIN_PROOF_3_RAW_PROOF, proof3.get().getRawProof());

        // Checking page management results.
        graphQLQueryRequest = new GraphQLQueryRequest(
                ProofFilesByAssetIdGraphQLQuery.newRequest().assetId(ACTIVE_ROYLLO_COIN_ASSET_ID).page(1).pageSize(1).build(),
                new ProofFilesByAssetIdProjectionRoot<>().content()
                        .creator().userId().username().parent()
                        .asset().assetId().parent()
                        .proofFileId()
                        .rawProof()
                        .parent()
                        .totalElements()
                        .totalPages());

        proofPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.ProofFilesByAssetId,
                new TypeRef<>() {
                });

        assertEquals(3, proofPage.getTotalElements());
        assertEquals(3, proofPage.getTotalPages());
    }

}