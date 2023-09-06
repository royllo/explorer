package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.QueryAssetStatesGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.QueryAssetStatesProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.AssetStatePage;
import org.royllo.explorer.api.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.royllo.explorer.core.service.asset.AssetStateServiceImplementation.SEARCH_PARAMETER_ASSET_ID;

@SpringBootTest
@DisplayName("AssetDataFetcher tests")
public class AssetStateDataFetcherTest extends BaseTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("queryAssetStates()")
    public void queryAssetStates() {
        // Getting one page.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetStatesGraphQLQuery.newRequest().query(SEARCH_PARAMETER_ASSET_ID + "asset_id_0").page(1).build(),
                new QueryAssetStatesProjectionRoot<>().content()
                        .assetStateId()
                        .creator().userId().username().getParent()
                        .asset().assetId().name().getParent()
                        .anchorBlockHash()
                        .anchorOutpoint().txId().vout().getParent()
                        .anchorTx()
                        .internalKey()
                        .merkleRoot()
                        .tapscriptSibling()
                        .scriptVersion()
                        .scriptKey()
                        .getParent()
                        .totalElements()
                        .totalPages());

        AssetStatePage assetStatePage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.QueryAssetStates,
                new TypeRef<>() {
                });

        assertEquals(3, assetStatePage.getTotalElements());
        assertEquals(1, assetStatePage.getTotalPages());
        assertEquals("ASSET_STATE_ID_10091", assetStatePage.getContent().get(0).getAssetStateId());
        assertEquals("ASSET_STATE_ID_10092", assetStatePage.getContent().get(1).getAssetStateId());
        assertEquals("ASSET_STATE_ID_10093", assetStatePage.getContent().get(2).getAssetStateId());
    }

    @Test
    @DisplayName("queryAssetStates() with page size")
    public void queryAssetStatesWithPageSize() {
        // Looking at page 2.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetStatesGraphQLQuery.newRequest().query(SEARCH_PARAMETER_ASSET_ID + "asset_id_0").page(2).pageSize(1).build(),
                new QueryAssetStatesProjectionRoot<>().content()
                        .assetStateId()
                        .creator().userId().username().getParent()
                        .asset().assetId().name().getParent()
                        .anchorBlockHash()
                        .anchorOutpoint().txId().vout().getParent()
                        .anchorTx()
                        .internalKey()
                        .merkleRoot()
                        .tapscriptSibling()
                        .scriptVersion()
                        .scriptKey()
                        .getParent()
                        .totalElements()
                        .totalPages());

        AssetStatePage assetStatePage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.QueryAssetStates,
                new TypeRef<>() {
                });

        assertEquals(3, assetStatePage.getTotalElements());
        assertEquals(3, assetStatePage.getTotalPages());
        assertEquals("ASSET_STATE_ID_10092", assetStatePage.getContent().get(0).getAssetStateId());
    }

    @Test
    @DisplayName("queryAssetStates() with invalid page size")
    public void queryAssetStatesWithInvalidPageSize() {
        try {
            // Looking at page 2.
            GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                    QueryAssetStatesGraphQLQuery.newRequest().query(SEARCH_PARAMETER_ASSET_ID + "asset_id_0").page(2).pageSize(1).build(),
                    new QueryAssetStatesProjectionRoot<>().content()
                            .assetStateId());

            AssetStatePage assetStatePage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                    graphQLQueryRequest.serialize(),
                    "data." + DgsConstants.QUERY.QueryAssetStates,
                    new TypeRef<>() {
                    });
        } catch (
                QueryException e) {
            assertEquals("Page number starts at page 1", e.getMessage());
        }
    }

    @Test
    @DisplayName("queryAssetStates() without page number")
    public void queryAssetStatesWithoutPageNumber() {
        // Getting one page.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetStatesGraphQLQuery.newRequest().query(SEARCH_PARAMETER_ASSET_ID + "asset_id_0").build(),
                new QueryAssetStatesProjectionRoot<>().content()
                        .assetStateId()
                        .creator().userId().username().getParent()
                        .asset().assetId().name().getParent()
                        .anchorBlockHash()
                        .anchorOutpoint().txId().vout().getParent()
                        .anchorTx()
                        .internalKey()
                        .merkleRoot()
                        .tapscriptSibling()
                        .scriptVersion()
                        .scriptKey()
                        .getParent()
                        .totalElements()
                        .totalPages());

        AssetStatePage assetStatePage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.QueryAssetStates,
                new TypeRef<>() {
                });

        assertEquals(3, assetStatePage.getTotalElements());
        assertEquals(1, assetStatePage.getTotalPages());
        assertEquals("ASSET_STATE_ID_10091", assetStatePage.getContent().get(0).getAssetStateId());
        assertEquals("ASSET_STATE_ID_10092", assetStatePage.getContent().get(1).getAssetStateId());
        assertEquals("ASSET_STATE_ID_10093", assetStatePage.getContent().get(2).getAssetStateId());
    }

    @Test
    @DisplayName("queryAssetStates() with negative number")
    public void queryAssetStatesWithNegativePageNumber() {
        try {
            // Looking at page 2.
            new GraphQLQueryRequest(
                    QueryAssetStatesGraphQLQuery.newRequest().query(SEARCH_PARAMETER_ASSET_ID + "asset_id_0").page(-1).build(),
                    new QueryAssetStatesProjectionRoot<>().content()
                            .assetStateId());
        } catch (QueryException e) {
            assertEquals("Page number starts at page 1", e.getMessage());
        }
    }

}