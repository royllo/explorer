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
        assertEquals("asset_id_0", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_1", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_2", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_3", assetPage.getContent().get(3).getAssetId());
        assertEquals("asset_id_4", assetPage.getContent().get(4).getAssetId());

        // Looking at page 2
        graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(2).build(),
                new QueryAssetsProjectionRoot().content()
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
                new QueryAssetsProjectionRoot().content()
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
        assertEquals("asset_id_0", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_1", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_2", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_3", assetPage.getContent().get(3).getAssetId());

        // Looking at page 2
        graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(2).pageSize(4).build(),
                new QueryAssetsProjectionRoot().content()
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
        assertEquals("asset_id_4", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_5", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_6", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_7", assetPage.getContent().get(3).getAssetId());

        // Looking at page 3
        graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").page(3).pageSize(4).build(),
                new QueryAssetsProjectionRoot().content()
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
        assertEquals("asset_id_8", assetPage.getContent().get(0).getAssetId());
    }


    @Test
    @DisplayName("queryAssets() with invalid page size")
    public void queryAssetsWithInvalidPageSize() {
        // Looking at page 1.
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").pageSize(MAXIMUM_PAGE_SIZE + 1).build(),
                new QueryAssetsProjectionRoot().content()
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
        assertEquals("asset_id_0", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_1", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_2", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_3", assetPage.getContent().get(3).getAssetId());
        assertEquals("asset_id_4", assetPage.getContent().get(4).getAssetId());
    }

    @Test
    @DisplayName("assetByAssetId()")
    public void assetByAssetId() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                AssetByAssetIdGraphQLQuery.newRequest().assetId(MY_ROYLLO_COIN_ASSET_ID).build(),
                new AssetByAssetIdProjectionRoot()
                        .version()
                        .creator().userId().username().parent()
                        .genesisPoint().txId().vout().parent()
                        .name()
                        .metaData()
                        .assetId()
                        .outputIndex()
                        .genesisBootstrapInformation()
                        .genesisVersion()
                        .type().parent()
                        .amount()
                        .lockTime()
                        .relativeLockTime()
                        .scriptVersion()
                        .scriptKey()
                        .anchorTx()
                        .anchorTxId()
                        .anchorBlockHash()
                        .anchorOutpoint()
                        .anchorInternalKey());

        Asset asset = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.AssetByAssetId,
                new TypeRef<>() {
                });

        assertNotNull(asset);
        assertEquals(MY_ROYLLO_COIN_VERSION, asset.getVersion());

        // Genesis point.
        assertEquals(MY_ROYLLO_COIN_GENESIS_POINT_TXID, asset.getGenesisPoint().getTxId());
        assertEquals(MY_ROYLLO_COIN_GENESIS_POINT_VOUT, asset.getGenesisPoint().getVout());
        assertEquals(MY_ROYLLO_COIN_NAME, asset.getName());
        assertEquals(MY_ROYLLO_COIN_META, asset.getMetaData());
        assertEquals(MY_ROYLLO_COIN_ASSET_ID, asset.getAssetId());
        assertEquals(MY_ROYLLO_COIN_OUTPUT_INDEX, asset.getOutputIndex());
        assertEquals(MY_ROYLLO_COIN_GENESIS_BOOTSTRAP_INFORMATION, asset.getGenesisBootstrapInformation());
        assertEquals(MY_ROYLLO_COIN_GENESIS_VERSION, asset.getGenesisVersion());

        assertEquals(MY_ROYLLO_COIN_ASSET_TYPE.toString(), asset.getType().toString());
        assertEquals(0, MY_ROYLLO_COIN_AMOUNT.compareTo(asset.getAmount()));
        assertEquals(MY_ROYLLO_COIN_LOCK_TIME, asset.getLockTime());
        assertEquals(MY_ROYLLO_COIN_RELATIVE_LOCK_TIME, asset.getRelativeLockTime());
        assertEquals(MY_ROYLLO_COIN_SCRIPT_VERSION, asset.getScriptVersion());
        assertEquals(MY_ROYLLO_COIN_SCRIPT_KEY, asset.getScriptKey());

        // Chain anchor.
        assertEquals(MY_ROYLLO_COIN_ANCHOR_TX, asset.getAnchorTx());
        assertEquals(MY_ROYLLO_COIN_ANCHOR_TX_ID, asset.getAnchorTxId());
        assertEquals(MY_ROYLLO_COIN_ANCHOR_BLOCK_HASH, asset.getAnchorBlockHash());
        assertEquals(MY_ROYLLO_COIN_ANCHOR_OUTPOINT, asset.getAnchorOutpoint());
        assertEquals(MY_ROYLLO_COIN_ANCHRO_INTERNAL_KEY, asset.getAnchorInternalKey());
    }

}
