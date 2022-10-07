package org.royllo.explorer.api.test.core.web.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.client.generated.DgsConstants;
import org.royllo.explorer.api.client.generated.client.AssetGraphQLQuery;
import org.royllo.explorer.api.client.generated.client.AssetProjectionRoot;
import org.royllo.explorer.api.client.generated.types.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("AssetDataFetcher tests")
public class AssetDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("Get asset")
    public void getAsset() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                AssetGraphQLQuery.newRequest().id("1").build(),
                new AssetProjectionRoot().id()
                        .creator().id().parent()
                        .creator().username().parent()
                        .genesisPoint().txId().parent()
                        .genesisPoint().vout().parent()
                        .name()
                        .meta()
                        .assetId()
                        .outputIndex());

        Asset asset = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.Asset,
                new TypeRef<>() {
                });
        System.out.println("==> " + asset);

        assertNotNull(asset);
        assertEquals("1", asset.getId());
        assertEquals("2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea", asset.getGenesisPoint().getTxId());
        assertEquals(0, asset.getGenesisPoint().getVout());
        assertEquals("0", asset.getCreator().getId());
        assertEquals("anonymous", asset.getCreator().getUsername());
        assertEquals("starbackrcoin", asset.getName());
        assertEquals("737461726261636b72206d6f6e6579", asset.getMeta() );
        assertEquals("b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e", asset.getAssetId());
        assertEquals(0, asset.getOutputIndex());

        // TODO test error found if user not found
    }

}
