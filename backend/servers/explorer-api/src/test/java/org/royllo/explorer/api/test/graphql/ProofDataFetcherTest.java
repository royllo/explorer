package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.ProofsByAssetIdGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.ProofsByAssetIdProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.Proof;
import org.royllo.explorer.api.graphql.generated.types.ProofPage;
import org.royllo.explorer.api.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_ID;

@SpringBootTest
@DisplayName("AssetDataFetcher tests")
public class ProofDataFetcherTest extends BaseTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("proofsByAssetId()")
    public void proofsByAssetId() {
        // TODO Review this test
        // Checking all results.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                ProofsByAssetIdGraphQLQuery.newRequest().assetId(ACTIVE_ROYLLO_COIN_ASSET_ID).page(1).pageSize(10).build(),
                new ProofsByAssetIdProjectionRoot<>().content()
                        .creator().userId().username().parent()
                        .asset().assetId().parent()
                        .proofId()
                        .rawProof()
                        .parent()
                        .totalElements()
                        .totalPages());

        ProofPage proofPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.ProofsByAssetId,
                new TypeRef<>() {
                });

        assertEquals(3, proofPage.getTotalElements());
        assertEquals(1, proofPage.getTotalPages());

        // Proof 1.
        final Optional<Proof> proof1 = proofPage.getContent()
                .stream()
                .filter(proof -> proof.getProofId().equals(ACTIVE_ROYLLO_COIN_PROOF_1_PROOF_ID))
                .findFirst();
        assertTrue(proof1.isPresent());
        assertEquals(ANONYMOUS_USER_ID, proof1.get().getCreator().getUserId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof1.get().getAsset().getAssetId());
        assertEquals(ACTIVE_ROYLLO_COIN_PROOF_1_RAW_PROOF, proof1.get().getRawProof());

        // Proof 2.
        final Optional<Proof> proof2 = proofPage.getContent().stream()
                .filter(proof -> proof.getProofId().equals(ACTIVE_ROYLLO_COIN_PROOF_2_PROOF_ID))
                .findFirst();
        assertTrue(proof2.isPresent());
        assertEquals(ANONYMOUS_USER_ID, proof2.get().getCreator().getUserId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof2.get().getAsset().getAssetId());
        assertEquals(ACTIVE_ROYLLO_COIN_PROOF_2_RAW_PROOF, proof2.get().getRawProof());

        // Proof 3.
        final Optional<Proof> proof3 = proofPage.getContent().stream()
                .filter(proof -> proof.getProofId().equals(ACTIVE_ROYLLO_COIN_PROOF_3_PROOF_ID))
                .findFirst();
        assertTrue(proof3.isPresent());
        assertEquals(ANONYMOUS_USER_ID, proof3.get().getCreator().getUserId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof3.get().getAsset().getAssetId());
        assertEquals(ACTIVE_ROYLLO_COIN_PROOF_3_RAW_PROOF, proof3.get().getRawProof());

        // Checking page management results.
        graphQLQueryRequest = new GraphQLQueryRequest(
                ProofsByAssetIdGraphQLQuery.newRequest().assetId(ACTIVE_ROYLLO_COIN_ASSET_ID).page(1).pageSize(1).build(),
                new ProofsByAssetIdProjectionRoot<>().content()
                        .creator().userId().username().parent()
                        .asset().assetId().parent()
                        .proofId()
                        .rawProof()
                        .parent()
                        .totalElements()
                        .totalPages());

        proofPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.ProofsByAssetId,
                new TypeRef<>() {
                });

        assertEquals(3, proofPage.getTotalElements());
        assertEquals(3, proofPage.getTotalPages());
    }

}
