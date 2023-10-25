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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.royllo.explorer.api.configuration.APIConfiguration.MAXIMUM_PAGE_SIZE;
import static org.royllo.explorer.core.service.asset.AssetStateServiceImplementation.SEARCH_PARAMETER_ASSET_ID;

@SpringBootTest
@DisplayName("AssetStateDataFetcher tests")
public class AssetStateDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("queryAssetStates()")
    public void queryAssetStates() {
        // Getting one page.
        AssetStatePage assetStatePage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetStatesGraphQLQuery.newRequest().query(SEARCH_PARAMETER_ASSET_ID + "asset_id_0").page(1).build(),
                        new QueryAssetStatesProjectionRoot<>().content()
                                .assetStateId()
                                .creator().userId().username().getParent()
                                .asset().assetId().name().getParent()
                                .anchorBlockHash()
                                .anchorOutpoint().txId().vout().getParent()
                                .internalKey()
                                .merkleRoot()
                                .tapscriptSibling()
                                .scriptVersion()
                                .scriptKey()
                                .getParent()
                                .totalElements()
                                .totalPages()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssetStates,
                new TypeRef<>() {
                });

        // Testing the results.
        assertEquals(3, assetStatePage.getTotalElements());
        assertEquals(1, assetStatePage.getTotalPages());
        assertEquals("ASSET_STATE_ID_10091", assetStatePage.getContent().get(0).getAssetStateId());
        assertEquals("ASSET_STATE_ID_10092", assetStatePage.getContent().get(1).getAssetStateId());
        assertEquals("ASSET_STATE_ID_10093", assetStatePage.getContent().get(2).getAssetStateId());
    }

    @Test
    @DisplayName("queryAssetStates() with page size")
    public void queryAssetStatesWithPageSize() {
        // Gztting page 2.
        AssetStatePage assetStatePage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetStatesGraphQLQuery.newRequest().query(SEARCH_PARAMETER_ASSET_ID + "asset_id_0").page(2).pageSize(1).build(),
                        new QueryAssetStatesProjectionRoot<>().content()
                                .assetStateId()
                                .creator().userId().username().getParent()
                                .asset().assetId().name().getParent()
                                .anchorBlockHash()
                                .anchorOutpoint().txId().vout().getParent()
                                .internalKey()
                                .merkleRoot()
                                .tapscriptSibling()
                                .scriptVersion()
                                .scriptKey()
                                .getParent()
                                .totalElements()
                                .totalPages()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssetStates,
                new TypeRef<>() {
                });

        // Testing the results.
        assertEquals(3, assetStatePage.getTotalElements());
        assertEquals(3, assetStatePage.getTotalPages());
        assertEquals("ASSET_STATE_ID_10092", assetStatePage.getContent().get(0).getAssetStateId());
    }

    @Test
    @DisplayName("queryAssetStates() without page number")
    public void queryAssetStatesWithoutPageNumber() {
        // Getting a page without setting page number.
        AssetStatePage assetStatePage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetStatesGraphQLQuery.newRequest().query(SEARCH_PARAMETER_ASSET_ID + "asset_id_0").build(),
                        new QueryAssetStatesProjectionRoot<>().content()
                                .assetStateId()
                                .creator().userId().username().getParent()
                                .asset().assetId().name().getParent()
                                .anchorBlockHash()
                                .anchorOutpoint().txId().vout().getParent()
                                .internalKey()
                                .merkleRoot()
                                .tapscriptSibling()
                                .scriptVersion()
                                .scriptKey()
                                .getParent()
                                .totalElements()
                                .totalPages()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssetStates,
                new TypeRef<>() {
                });

        // Testing the results.
        assertEquals(3, assetStatePage.getTotalElements());
        assertEquals(1, assetStatePage.getTotalPages());
        assertEquals("ASSET_STATE_ID_10091", assetStatePage.getContent().get(0).getAssetStateId());
        assertEquals("ASSET_STATE_ID_10092", assetStatePage.getContent().get(1).getAssetStateId());
        assertEquals("ASSET_STATE_ID_10093", assetStatePage.getContent().get(2).getAssetStateId());
    }

    @Test
    @DisplayName("queryAssetStates() with invalid page size")
    public void queryAssetStatesWithInvalidPageSize() {
        QueryException e = assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetStatesGraphQLQuery.newRequest().query(SEARCH_PARAMETER_ASSET_ID + "asset_id_0").pageSize(MAXIMUM_PAGE_SIZE + 1).build(),
                        new QueryAssetStatesProjectionRoot<>().content()
                                .assetStateId()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssetStates,
                new TypeRef<>() {
                }));
        assertEquals("Page size can't be superior to " + MAXIMUM_PAGE_SIZE, e.getMessage());
    }

    @Test
    @DisplayName("queryAssetStates() with negative number")
    public void queryAssetStatesWithNegativePageNumber() {
        QueryException e = assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetStatesGraphQLQuery.newRequest().query(SEARCH_PARAMETER_ASSET_ID + "asset_id_0").page(-1).build(),
                        new QueryAssetStatesProjectionRoot<>().content()
                                .assetStateId()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssetStates,
                new TypeRef<>() {
                }));
        assertEquals("Page number starts at page 1", e.getMessage());
    }

}
