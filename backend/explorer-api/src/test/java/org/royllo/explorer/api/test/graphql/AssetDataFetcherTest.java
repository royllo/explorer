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
import org.royllo.explorer.api.graphql.generated.types.AssetPage;
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
        // Looking at page 1.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().value("TestPaginationCoin").pageNumber(0).build(),
                new QueryAssetsProjectionRoot().content()
                        .id()
                        .creator().id().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaData()
                        .assetId()
                        .outputIndex()
                        .parent()
                        .totalElements()
                        .totalPages());

        AssetPage assetPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                });

        assertEquals(9, assetPage.getTotalElements());
        assertEquals(2, assetPage.getTotalPages());
        assertEquals("1009", assetPage.getContent().get(0).getId());
        assertEquals("1001", assetPage.getContent().get(1).getId());
        assertEquals("1002", assetPage.getContent().get(2).getId());
        assertEquals("1003", assetPage.getContent().get(3).getId());
        assertEquals("1004", assetPage.getContent().get(4).getId());

        // Looking at page 2
        graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().value("TestPaginationCoin").pageNumber(1).build(),
                new QueryAssetsProjectionRoot().content()
                        .id()
                        .creator().id().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaData()
                        .assetId()
                        .outputIndex()
                        .parent()
                        .totalElements()
                        .totalPages());

        assetPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                });

        assertEquals(9, assetPage.getTotalElements());
        assertEquals(2, assetPage.getTotalPages());
        assertEquals("1005", assetPage.getContent().get(0).getId());
        assertEquals("1006", assetPage.getContent().get(1).getId());
        assertEquals("1007", assetPage.getContent().get(2).getId());
        assertEquals("1008", assetPage.getContent().get(3).getId());
    }

    @Test
    @DisplayName("queryAssets() without page number")
    public void queryAssetsWithoutPageNumber() {
        // Looking at page 1.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().value("TestPaginationCoin").build(),
                new QueryAssetsProjectionRoot().content()
                        .id()
                        .creator().id().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaData()
                        .assetId()
                        .outputIndex()
                        .parent()
                        .totalElements()
                        .totalPages());

        AssetPage assetPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                });

        assertEquals(9, assetPage.getTotalElements());
        assertEquals(2, assetPage.getTotalPages());
        assertEquals("1009", assetPage.getContent().get(0).getId());
        assertEquals("1001", assetPage.getContent().get(1).getId());
        assertEquals("1002", assetPage.getContent().get(2).getId());
        assertEquals("1003", assetPage.getContent().get(3).getId());
        assertEquals("1004", assetPage.getContent().get(4).getId());
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
        assertEquals("0", asset.getCreator().getId());
        assertEquals("anonymous", asset.getCreator().getUsername());
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
        // TODO, there should be a test with another user than anonymous
        assertEquals("0", asset.getCreator().getId());
        assertEquals("anonymous", asset.getCreator().getUsername());
        assertEquals("starbackrcoin", asset.getName());
        assertEquals("737461726261636b72206d6f6e6579", asset.getMetaData() );
        assertEquals("b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e", asset.getAssetId());
        assertEquals(0, asset.getOutputIndex());
    }

}
