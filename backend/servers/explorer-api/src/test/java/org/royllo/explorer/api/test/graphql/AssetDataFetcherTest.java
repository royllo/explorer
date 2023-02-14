package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.AssetByAssetIdGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.AssetByAssetIdProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.QueryAssetsGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.QueryAssetsProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.Asset;
import org.royllo.explorer.api.graphql.generated.types.AssetPage;
import org.royllo.explorer.api.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.royllo.explorer.api.configuration.APIConfiguration.MAXIMUM_PAGE_SIZE;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_USERNAME;

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
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(1).build(),
                new QueryAssetsProjectionRoot().content()
                        .id()
                        .creator().userId().username().parent()
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
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(2).build(),
                new QueryAssetsProjectionRoot().content()
                        .id()
                        .creator().userId().username().parent()
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
    @DisplayName("queryAssets() with page size")
    public void queryAssetsWithPageSize() {
        // Looking at page 1.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(1).pageSize(4).build(),
                new QueryAssetsProjectionRoot().content()
                        .id()
                        .creator().userId().username().parent()
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
        assertEquals(3, assetPage.getTotalPages());
        assertEquals("1009", assetPage.getContent().get(0).getId());
        assertEquals("1001", assetPage.getContent().get(1).getId());
        assertEquals("1002", assetPage.getContent().get(2).getId());
        assertEquals("1003", assetPage.getContent().get(3).getId());

        // Looking at page 2
        graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(2).pageSize(4).build(),
                new QueryAssetsProjectionRoot().content()
                        .id()
                        .creator().userId().username().parent()
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
        assertEquals(3, assetPage.getTotalPages());
        assertEquals("1004", assetPage.getContent().get(0).getId());
        assertEquals("1005", assetPage.getContent().get(1).getId());
        assertEquals("1006", assetPage.getContent().get(2).getId());
        assertEquals("1007", assetPage.getContent().get(3).getId());

        // Looking at page 3
        graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(3).pageSize(4).build(),
                new QueryAssetsProjectionRoot().content()
                        .id()
                        .creator().userId().username().parent()
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
        assertEquals(3, assetPage.getTotalPages());
        assertEquals("1008", assetPage.getContent().get(0).getId());
    }


    @Test
    @DisplayName("queryAssets() with invalid page size")
    public void queryAssetsWithInvalidPageSize() {
        // Looking at page 1.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").pageSize(MAXIMUM_PAGE_SIZE + 1).build(),
                new QueryAssetsProjectionRoot().content()
                        .id()
                        .creator().userId().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaData()
                        .assetId()
                        .outputIndex()
                        .parent()
                        .totalElements()
                        .totalPages());

        try {
            dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                    graphQLQueryRequest.serialize(),
                    "data." + DgsConstants.QUERY.QueryAssets,
                    new TypeRef<>() {
                    });
            // Exception was not thrown!
            fail("An exception should have occurred because of the page size");
        } catch (QueryException e) {
            assertEquals("Page size can't be superior to " + MAXIMUM_PAGE_SIZE, e.getMessage());
        }
    }

    @Test
    @DisplayName("queryAssets() without page number")
    public void queryAssetsWithoutPageNumber() {
        // Looking at page 1.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").build(),
                new QueryAssetsProjectionRoot().content()
                        .id()
                        .creator().userId().username().parent()
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
    @DisplayName("assetByAssetId()")
    public void assetByAssetId() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                AssetByAssetIdGraphQLQuery.newRequest().assetId(ASSET_ID_NUMBER_01).build(),
                new AssetByAssetIdProjectionRoot().id()
                        .creator().userId().username().parent()
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
        assertEquals(ANONYMOUS_USER_ID, asset.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, asset.getCreator().getUsername());
        assertEquals("starbackrcoin", asset.getName());
        assertEquals("737461726261636b72206d6f6e6579", asset.getMetaData());
        assertEquals("b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e", asset.getAssetId());
        assertEquals(1, asset.getOutputIndex());
    }

}
