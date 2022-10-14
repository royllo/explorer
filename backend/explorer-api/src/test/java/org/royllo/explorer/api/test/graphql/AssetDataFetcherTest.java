package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.AssetByAssetIdGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.AssetByAssetIdProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.AssetGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.AssetProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.QueryAssetsGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.QueryAssetsProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.royllo.explorer.api.test.BaseTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("AssetDataFetcher tests")
public class AssetDataFetcherTest extends BaseTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("queryAssets()")
    public void queryAssets() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().value("royllo").build(),
                new QueryAssetsProjectionRoot().id()
                        .creator().id().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaData()
                        .assetId()
                        .outputIndex());

        List<Asset> assets = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.QueryAssets + "[*]",
                new TypeRef<>() {
                });

        assertEquals(1, assets.size());
        assertEquals("2", assets.get(0).getId());
    }

    @Test
    @DisplayName("getAsset()")
    public void getAsset() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                AssetGraphQLQuery.newRequest().id("1").build(),
                new AssetProjectionRoot().id()
                        .creator().id().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaData()
                        .assetId()
                        .outputIndex());

        Asset asset = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.Asset,
                new TypeRef<>() {
                });

        assertNotNull(asset);
        assertEquals("1", asset.getId());
        assertEquals("2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea", asset.getGenesisPoint().getTxId());
        assertEquals(0, asset.getGenesisPoint().getVout());
        assertEquals("1", asset.getCreator().getId());
        assertEquals("straumat", asset.getCreator().getUsername());
        assertEquals("starbackrcoin", asset.getName());
        assertEquals("737461726261636b72206d6f6e6579", asset.getMetaData() );
        assertEquals("b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e", asset.getAssetId());
        assertEquals(0, asset.getOutputIndex());
    }

    @Test
    @DisplayName("assetByAssetId()")
    public void assetByAssetId() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                AssetByAssetIdGraphQLQuery.newRequest().assetId(ASSET_ID_NUMBER_01).build(),
                new AssetByAssetIdProjectionRoot().id()
                        .creator().id().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaData()
                        .assetId()
                        .outputIndex());

        Asset asset = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.AssetByAssetId,
                new TypeRef<>() {
                });

        assertNotNull(asset);
        assertEquals("1", asset.getId());
        assertEquals("2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea", asset.getGenesisPoint().getTxId());
        assertEquals(0, asset.getGenesisPoint().getVout());
        assertEquals("1", asset.getCreator().getId());
        assertEquals("straumat", asset.getCreator().getUsername());
        assertEquals("starbackrcoin", asset.getName());
        assertEquals("737461726261636b72206d6f6e6579", asset.getMetaData() );
        assertEquals("b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e", asset.getAssetId());
        assertEquals(0, asset.getOutputIndex());
    }

}
