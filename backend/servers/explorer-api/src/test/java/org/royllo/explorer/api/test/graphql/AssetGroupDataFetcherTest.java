package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.AssetGroupByAssetGroupIdGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.AssetGroupByAssetGroupIdProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.AssetGroup;
import org.royllo.test.tapd.asset.DecodedProofValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("AssetGroupDataFetcher tests")
public class AssetGroupDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("assetGroupByAssetGroupId()")
    public void assetGroupByAssetGroupId() {
        final String assetGroupId = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey();

        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        AssetGroupByAssetGroupIdGraphQLQuery.newRequest().assetGroupId(assetGroupId).build(),
                        new AssetGroupByAssetGroupIdProjectionRoot<>()
                                .assetGroupId()
                                .creator().userId().username().parent()
                                .assetWitness()
                                .rawGroupKey()
                                .tweakedGroupKey()
                ).serialize(),
                "data." + DgsConstants.QUERY.AssetGroupByAssetGroupId,
                new TypeRef<AssetGroup>() {
                }))
                .isNotNull()
                .satisfies(assetGroup -> {
                    final DecodedProofValueResponse.DecodedProof.Asset.AssetGroup assetGroupFromTest = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup();
                    assertEquals(ANONYMOUS_USER_ID, assetGroup.getCreator().getUserId());
                    assertEquals(ANONYMOUS_USER_USERNAME, assetGroup.getCreator().getUsername());
                    assertEquals(assetGroupFromTest.getTweakedGroupKey(), assetGroup.getAssetGroupId());
                    assertEquals(assetGroupFromTest.getRawGroupKey(), assetGroup.getRawGroupKey());
                    assertEquals(assetGroupFromTest.getTweakedGroupKey(), assetGroup.getAssetGroupId());
                    assertEquals(assetGroupFromTest.getAssetWitness(), assetGroup.getAssetWitness());
                });
    }

}
