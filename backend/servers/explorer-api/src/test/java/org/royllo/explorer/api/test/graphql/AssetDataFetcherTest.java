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

import static graphql.Assert.assertNull;
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
                new QueryAssetsProjectionRoot<>().content()
                        .creator().userId().username().getParent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaDataHash()
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
        assertEquals("asset_id_0", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_1", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_2", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_3", assetPage.getContent().get(3).getAssetId());
        assertEquals("asset_id_4", assetPage.getContent().get(4).getAssetId());

        // Looking at page 2
        graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(2).build(),
                new QueryAssetsProjectionRoot<>().content()
                        .creator().userId().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaDataHash()
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
        assertEquals("asset_id_5", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_6", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_7", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_8", assetPage.getContent().get(3).getAssetId());
    }

    @Test
    @DisplayName("queryAssets() with page size")
    public void queryAssetsWithPageSize() {
        // Looking at page 1.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(1).pageSize(4).build(),
                new QueryAssetsProjectionRoot<>().content()
                        .creator().userId().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaDataHash()
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
        assertEquals("asset_id_0", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_1", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_2", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_3", assetPage.getContent().get(3).getAssetId());

        // Looking at page 2
        graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(2).pageSize(4).build(),
                new QueryAssetsProjectionRoot<>().content()
                        .creator().userId().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaDataHash()
                        .assetId()
                        .outputIndex()
                        .parent()
                        .totalElements()
                        .totalPages()
        );

        assetPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                });

        assertEquals(9, assetPage.getTotalElements());
        assertEquals(3, assetPage.getTotalPages());
        assertEquals("asset_id_4", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_5", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_6", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_7", assetPage.getContent().get(3).getAssetId());

        // Looking at page 3
        graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(3).pageSize(4).build(),
                new QueryAssetsProjectionRoot<>().content()
                        .creator().userId().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaDataHash()
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
        assertEquals("asset_id_8", assetPage.getContent().get(0).getAssetId());
    }


    @Test
    @DisplayName("queryAssets() with invalid page size")
    public void queryAssetsWithInvalidPageSize() {
        // Looking at page 1.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").pageSize(MAXIMUM_PAGE_SIZE + 1).build(),
                new QueryAssetsProjectionRoot<>().content()
                        .creator().userId().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaDataHash()
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
                new QueryAssetsProjectionRoot<>().content()
                        .creator().userId().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaDataHash()
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

        assetPage.getContent().forEach(asset -> System.out.println("Asset: " + asset.getName()));
        assertEquals(9, assetPage.getTotalElements());
        assertEquals(2, assetPage.getTotalPages());
        assertEquals("asset_id_0", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_1", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_2", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_3", assetPage.getContent().get(3).getAssetId());
        assertEquals("asset_id_4", assetPage.getContent().get(4).getAssetId());
    }

    @Test
    @DisplayName("queryAssets() with negative number")
    public void queryAssetsWithNegativePageNumber() {
        try {
            // Looking at page -1.
            new GraphQLQueryRequest(
                    QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(-1).build(),
                    new QueryAssetsProjectionRoot<>().content()
                            .creator().userId().username().parent()
                            .genesisPoint().txId().vout().parent()
                            .name()
                            .metaDataHash()
                            .assetId()
                            .outputIndex()
                            .parent()
                            .totalElements()
                            .totalPages());
        } catch (QueryException e) {
            assertEquals("Page number starts at page 1", e.getMessage());
        }
    }

    @Test
    @DisplayName("assetByAssetId()")
    public void assetByAssetId() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                AssetByAssetIdGraphQLQuery.newRequest().assetId(ROYLLO_COIN_ASSET_ID).build(),
                new AssetByAssetIdProjectionRoot<>()
                        .creator().userId().username().parent()
                        .assetId()
                        .genesisPoint().txId().vout().parent()
                        .metaDataHash()
                        .name()
                        .outputIndex()
                        .version()
                        .type().parent()
                        .amount()
                        .assetGroup()
                        .creator().userId().username().parent()
                        .assetIdSig()
                        .rawGroupKey()
                        .tweakedGroupKey());

        Asset asset = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.AssetByAssetId,
                new TypeRef<>() {
                });

        assertNotNull(asset);

        // Asset.
        assertEquals(ANONYMOUS_USER_ID, asset.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, asset.getCreator().getUsername());
        assertEquals(ROYLLO_COIN_ASSET_ID, asset.getAssetId());
        assertEquals(ROYLLO_COIN_GENESIS_POINT_TXID, asset.getGenesisPoint().getTxId());
        assertEquals(ROYLLO_COIN_GENESIS_POINT_VOUT, asset.getGenesisPoint().getVout());
        assertEquals(ROYLLO_COIN_META_DATA_HASH, asset.getMetaDataHash());
        assertEquals(ROYLLO_COIN_NAME, asset.getName());
        assertEquals(ROYLLO_COIN_OUTPUT_INDEX, asset.getOutputIndex());
        assertEquals(ROYLLO_COIN_VERSION, asset.getVersion());
        assertEquals(ROYLLO_COIN_ASSET_TYPE.toString(), asset.getType().toString());
        assertEquals(0, ROYLLO_COIN_AMOUNT.compareTo(asset.getAmount()));

        // Asset group.
        assertEquals(ANONYMOUS_USER_ID, asset.getAssetGroup().getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, asset.getAssetGroup().getCreator().getUsername());
        assertEquals(ROYLLO_COIN_ASSET_ID_SIG, asset.getAssetGroup().getAssetIdSig());
        assertEquals(ROYLLO_COIN_RAW_GROUP_KEY, asset.getAssetGroup().getRawGroupKey());
        assertEquals(ROYLLO_COIN_TWEAKED_GROUP_KEY, asset.getAssetGroup().getTweakedGroupKey());

        // get asset when asset group is empty
        graphQLQueryRequest = new GraphQLQueryRequest(
                AssetByAssetIdGraphQLQuery.newRequest().assetId("NO_GROUP_ASSET_ASSET_ID").build(),
                new AssetByAssetIdProjectionRoot<>()
                        .assetId()
                        .assetGroup()
                        .assetIdSig()
                        .rawGroupKey()
                        .tweakedGroupKey());
        asset = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.AssetByAssetId,
                new TypeRef<>() {
                });
        assertNotNull(asset);
        assertNull(asset.getAssetGroup());
    }

}
