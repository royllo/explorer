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
import org.royllo.test.TestAssets;
import org.royllo.test.tapd.DecodedProofValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static graphql.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.royllo.explorer.api.configuration.APIConfiguration.MAXIMUM_PAGE_SIZE;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.test.TestAssets.ROYLLO_COIN_ASSET_ID;

@SpringBootTest
@DisplayName("AssetDataFetcher tests")
public class AssetDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("queryAssets()")
    public void queryAssets() {
        // Searching an asset - Page 1.
        AssetPage assetPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
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
                                .totalPages()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                });

        // Testing results.
        assertEquals(9, assetPage.getTotalElements());
        assertEquals(2, assetPage.getTotalPages());
        assertEquals("asset_id_0", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_1", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_2", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_3", assetPage.getContent().get(3).getAssetId());
        assertEquals("asset_id_4", assetPage.getContent().get(4).getAssetId());

        // Searching an asset - Page 2.
        assetPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
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
                                .totalPages())
                        .serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                });

        // Testing results.
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
        // Searching an asset - Page 1 with 4 as a page size.
        AssetPage assetPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
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
                                .totalPages()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                });

        // Testing results.
        assertEquals(9, assetPage.getTotalElements());
        assertEquals(3, assetPage.getTotalPages());
        assertEquals("asset_id_0", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_1", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_2", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_3", assetPage.getContent().get(3).getAssetId());

        // Searching an asset - Page 2 with 4 as a page size.
        assetPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
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
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                });

        // Testing results.
        assertEquals(9, assetPage.getTotalElements());
        assertEquals(3, assetPage.getTotalPages());
        assertEquals("asset_id_4", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_5", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_6", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_7", assetPage.getContent().get(3).getAssetId());

        // Searching an asset - Page 3 with 4 as a page size.
        assetPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
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
                                .totalPages()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                });

        // Testing results.
        assertEquals(9, assetPage.getTotalElements());
        assertEquals(3, assetPage.getTotalPages());
        assertEquals("asset_id_8", assetPage.getContent().get(0).getAssetId());
    }

    @Test
    @DisplayName("queryAssets() without page number")
    public void queryAssetsWithoutPageNumber() {
        // Searching an asset without setting page size.
        AssetPage assetPage = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
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
                                .totalPages()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                });

        // Testing results.
        assertEquals(9, assetPage.getTotalElements());
        assertEquals(2, assetPage.getTotalPages());
        assertEquals("asset_id_0", assetPage.getContent().get(0).getAssetId());
        assertEquals("asset_id_1", assetPage.getContent().get(1).getAssetId());
        assertEquals("asset_id_2", assetPage.getContent().get(2).getAssetId());
        assertEquals("asset_id_3", assetPage.getContent().get(3).getAssetId());
        assertEquals("asset_id_4", assetPage.getContent().get(4).getAssetId());
    }


    @Test
    @DisplayName("queryAssets() with invalid page size")
    public void queryAssetsWithInvalidPageSize() {
        // Searching an asset with an invalid page size.
        QueryException e = assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
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
                                .totalPages()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                }));
        assertEquals("Page size can't be superior to " + MAXIMUM_PAGE_SIZE, e.getMessage());
    }

    @Test
    @DisplayName("queryAssets() with negative number")
    public void queryAssetsWithNegativePageNumber() {
        // Searching an asset with a negative page number.
        QueryException e = assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(
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
                                .totalPages()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<>() {
                }));

        // Checking the exception message.
        assertEquals("Page number starts at page 1", e.getMessage());
    }

    @Test
    @DisplayName("assetByAssetId()")
    public void assetByAssetId() {
        Asset asset = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
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
                                .tweakedGroupKey()
                ).serialize(),
                "data." + DgsConstants.QUERY.AssetByAssetId,
                new TypeRef<>() {
                });

        // Testing results.
        assertNotNull(asset);
        final DecodedProofValueResponse.DecodedProof assetFromTestData = TestAssets.findAssetValueByAssetId(ROYLLO_COIN_ASSET_ID).getDecodedProof(0);

        // Asset.
        assertEquals(ANONYMOUS_USER_ID, asset.getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, asset.getCreator().getUsername());
        assertEquals(ROYLLO_COIN_ASSET_ID, asset.getAssetId());
        assertEquals(assetFromTestData.getAsset().getAssetGenesis().getGenesisPoint(), asset.getGenesisPoint().getTxId() + ":" + asset.getGenesisPoint().getVout());
        assertEquals(assetFromTestData.getAsset().getAssetGenesis().getMetaDataHash(), asset.getMetaDataHash());
        assertEquals(assetFromTestData.getAsset().getAssetGenesis().getName(), asset.getName());
        assertEquals(assetFromTestData.getAsset().getAssetGenesis().getOutputIndex(), asset.getOutputIndex());
        assertEquals(assetFromTestData.getAsset().getAssetGenesis().getVersion(), asset.getVersion());
        assertEquals(assetFromTestData.getAsset().getAssetType(), asset.getType().toString());
        assertEquals(0, assetFromTestData.getAsset().getAmount().compareTo(asset.getAmount()));

        // Asset group.
        assertEquals(ANONYMOUS_USER_ID, asset.getAssetGroup().getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, asset.getAssetGroup().getCreator().getUsername());
        assertEquals(assetFromTestData.getAsset().getAssetGroup().getAssetIdSig(), asset.getAssetGroup().getAssetIdSig());
        assertEquals(assetFromTestData.getAsset().getAssetGroup().getRawGroupKey(), asset.getAssetGroup().getRawGroupKey());
        assertEquals(assetFromTestData.getAsset().getAssetGroup().getTweakedGroupKey(), asset.getAssetGroup().getTweakedGroupKey());

        // Get asset when asset group is empty (Asset is in the liquibase test script).
        asset = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        AssetByAssetIdGraphQLQuery.newRequest().assetId("NO_GROUP_ASSET_ASSET_ID").build(),
                        new AssetByAssetIdProjectionRoot<>()
                                .assetId()
                                .assetGroup()
                                .assetIdSig()
                                .rawGroupKey()
                                .tweakedGroupKey()
                ).serialize(),
                "data." + DgsConstants.QUERY.AssetByAssetId,
                new TypeRef<>() {
                });
        assertNotNull(asset);
        assertNull(asset.getAssetGroup());
    }

}
