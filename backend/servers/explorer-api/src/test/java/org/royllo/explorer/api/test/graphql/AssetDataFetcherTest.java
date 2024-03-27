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
import org.royllo.test.tapd.asset.DecodedProofValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static graphql.Assert.assertNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.royllo.explorer.api.configuration.APIConfiguration.FIRST_PAGE;
import static org.royllo.explorer.api.configuration.APIConfiguration.MAXIMUM_PAGE_SIZE;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_ASSET_ID_ALIAS;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_FROM_TEST;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID_ALIAS;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_ASSET_ID_ALIAS;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_ASSET_ID_ALIAS;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_1_FROM_TEST;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_2_ASSET_ID;
import static org.royllo.test.TapdData.UNLIMITED_ROYLLO_COIN_2_ASSET_ID_ALIAS;

@SpringBootTest
@DirtiesContext
@DisplayName("AssetDataFetcher tests")
public class AssetDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("queryAssets()")
    public void queryAssets() {
        // Searching an asset - Page 1.
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetsGraphQLQuery.newRequest().query("coin").pageNumber(1).build(),
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
                new TypeRef<AssetPage>() {
                }))
                .isNotNull()
                .satisfies(assetPage -> {
                    assertEquals(4, assetPage.getTotalElements());
                    assertEquals(1, assetPage.getTotalPages());
                    assertThat(assetPage.getContent())
                            .extracting(Asset::getAssetId)
                            .contains(ROYLLO_COIN_ASSET_ID,
                                    TRICKY_ROYLLO_COIN_ASSET_ID,
                                    UNLIMITED_ROYLLO_COIN_1_ASSET_ID,
                                    UNLIMITED_ROYLLO_COIN_2_ASSET_ID);
                });

        // Query assets with an asset group id.
        final String assetGroupId = SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey();
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetsGraphQLQuery.newRequest().query(assetGroupId).pageNumber(1).build(),
                        new QueryAssetsProjectionRoot<>().content()
                                .creator().userId().username().getParent()
                                .genesisPoint().txId().vout().parent()
                                .name()
                                .metaDataHash()
                                .assetId()
                                .assetIdAlias()
                                .outputIndex()
                                .parent()
                                .totalElements()
                                .totalPages()
                ).serialize(),
                "data." + DgsConstants.QUERY.QueryAssets,
                new TypeRef<AssetPage>() {
                }))
                .isNotNull()
                .satisfies(assetPage -> {
                    assertEquals(3, assetPage.getTotalElements());
                    assertEquals(1, assetPage.getTotalPages());
                    assertThat(assetPage.getContent())
                            .extracting(Asset::getAssetId)
                            .contains(SET_OF_ROYLLO_NFT_1_ASSET_ID,
                                    SET_OF_ROYLLO_NFT_2_ASSET_ID,
                                    SET_OF_ROYLLO_NFT_3_ASSET_ID);
                    assertThat(assetPage.getContent())
                            .extracting(Asset::getAssetIdAlias)
                            .contains(SET_OF_ROYLLO_NFT_1_ASSET_ID_ALIAS,
                                    SET_OF_ROYLLO_NFT_2_ASSET_ID_ALIAS,
                                    SET_OF_ROYLLO_NFT_3_ASSET_ID_ALIAS);
                });
    }

    @Test
    @DisplayName("queryAssets() with page size")
    public void queryAssetsWithPageSize() {
        // Searching an asset - Page 1 with 2 as a page size.
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetsGraphQLQuery.newRequest().query("coin").pageNumber(1).pageSize(2).build(),
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
                new TypeRef<AssetPage>() {
                }))
                .isNotNull()
                .satisfies(assetPage -> {
                    assertEquals(4, assetPage.getTotalElements());
                    assertEquals(2, assetPage.getTotalPages());
                    assertThat(assetPage.getContent())
                            .extracting(Asset::getAssetId)
                            .contains(ROYLLO_COIN_ASSET_ID, TRICKY_ROYLLO_COIN_ASSET_ID);
                });

        // Searching an asset - Page 2 with 2 as a page size.
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetsGraphQLQuery.newRequest().query("coin").pageNumber(2).pageSize(2).build(),
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
                new TypeRef<AssetPage>() {
                }))
                .isNotNull()
                .satisfies(assetPage -> {
                    assertEquals(4, assetPage.getTotalElements());
                    assertEquals(2, assetPage.getTotalPages());
                    assertThat(assetPage.getContent())
                            .extracting(Asset::getAssetId)
                            .contains(UNLIMITED_ROYLLO_COIN_1_ASSET_ID,
                                    UNLIMITED_ROYLLO_COIN_2_ASSET_ID);
                });
    }

    @Test
    @DisplayName("queryAssets() without page number")
    public void queryAssetsWithoutPageNumber() {
        // Searching an asset without setting page size (should set page to 1 - Default value).
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetsGraphQLQuery.newRequest().query("coin").build(),
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
                new TypeRef<AssetPage>() {
                }))
                .isNotNull()
                .satisfies(assetPage -> {
                    assertEquals(4, assetPage.getTotalElements());
                    assertEquals(1, assetPage.getTotalPages());
                    assertThat(assetPage.getContent())
                            .extracting(Asset::getAssetId)
                            .containsExactly(ROYLLO_COIN_ASSET_ID,
                                    TRICKY_ROYLLO_COIN_ASSET_ID,
                                    UNLIMITED_ROYLLO_COIN_1_ASSET_ID,
                                    UNLIMITED_ROYLLO_COIN_2_ASSET_ID);
                });
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
    @DisplayName("queryAssets() with invalid first page")
    public void queryAssetsWithInvalidFirstPage() {
        // Searching an asset with a negative page number.
        QueryException e = assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        QueryAssetsGraphQLQuery.newRequest().query("TestPaginationCoin").pageNumber(FIRST_PAGE - 1).build(),
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
        assertEquals("Page number starts at page " + FIRST_PAGE, e.getMessage());
    }

    @Test
    @DisplayName("assetByAssetId()")
    public void assetByAssetId() {

        // Searching with an asset id.
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        AssetByAssetIdGraphQLQuery.newRequest().assetId(UNLIMITED_ROYLLO_COIN_1_ASSET_ID).build(),
                        new AssetByAssetIdProjectionRoot<>()
                                .creator().userId().username().parent()
                                .assetId()
                                .assetIdAlias()
                                .genesisPoint().txId().vout().parent()
                                .metaDataHash()
                                .metaDataFileName()
                                .name()
                                .outputIndex()
                                .version()
                                .type().parent()
                                .amount()
                                .assetGroup()
                                .creator().userId().username().parent()
                                .assetWitness()
                                .rawGroupKey()
                                .tweakedGroupKey()
                ).serialize(),
                "data." + DgsConstants.QUERY.AssetByAssetId,
                new TypeRef<Asset>() {
                }))
                .isNotNull()
                .satisfies(asset -> {
                            final DecodedProofValueResponse.DecodedProof assetFromTestData = UNLIMITED_ROYLLO_COIN_1_FROM_TEST.getDecodedProofResponse(0);

                            // Asset.
                            assertEquals(ANONYMOUS_USER_ID, asset.getCreator().getUserId());
                            assertEquals(ANONYMOUS_USER_USERNAME, asset.getCreator().getUsername());
                            assertEquals(UNLIMITED_ROYLLO_COIN_1_ASSET_ID, asset.getAssetId());
                            assertEquals(UNLIMITED_ROYLLO_COIN_1_ASSET_ID_ALIAS, asset.getAssetIdAlias());
                            assertEquals(assetFromTestData.getAsset().getAssetGenesis().getGenesisPoint(), asset.getGenesisPoint().getTxId() + ":" + asset.getGenesisPoint().getVout());
                            assertEquals(assetFromTestData.getAsset().getAssetGenesis().getMetaDataHash(), asset.getMetaDataHash());
                            assertEquals(assetFromTestData.getAsset().getAssetGenesis().getName(), asset.getName());
                            assertEquals(assetFromTestData.getAsset().getAssetGenesis().getOutputIndex(), asset.getOutputIndex());
                            assertEquals(assetFromTestData.getAsset().getAssetGenesis().getVersion(), asset.getVersion());
                            assertEquals(assetFromTestData.getAsset().getAssetGenesis().getAssetType(), asset.getType().toString());
                            assertEquals(0, assetFromTestData.getAsset().getAmount().compareTo(asset.getAmount()));

                            // Asset group.
                            assertNotNull(asset.getAssetGroup());
                            assertEquals(ANONYMOUS_USER_ID, asset.getAssetGroup().getCreator().getUserId());
                            assertEquals(ANONYMOUS_USER_USERNAME, asset.getAssetGroup().getCreator().getUsername());
                            assertEquals(assetFromTestData.getAsset().getAssetGroup().getRawGroupKey(), asset.getAssetGroup().getRawGroupKey());
                            assertEquals(assetFromTestData.getAsset().getAssetGroup().getTweakedGroupKey(), asset.getAssetGroup().getTweakedGroupKey());
                            assertEquals(assetFromTestData.getAsset().getAssetGroup().getAssetWitness(), asset.getAssetGroup().getAssetWitness());
                        }
                );


        // Get asset when asset group is empty.
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        AssetByAssetIdGraphQLQuery.newRequest().assetId(ROYLLO_COIN_ASSET_ID).build(),
                        new AssetByAssetIdProjectionRoot<>()
                                .assetId()
                                .assetGroup()
                                .rawGroupKey()
                                .tweakedGroupKey()
                                .assetWitness()
                ).serialize(),
                "data." + DgsConstants.QUERY.AssetByAssetId,
                new TypeRef<Asset>() {
                })).isNotNull()
                .satisfies(asset -> assertNull(asset.getAssetGroup()));

        // Searching with an asset id alias.
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        AssetByAssetIdGraphQLQuery.newRequest().assetId(UNLIMITED_ROYLLO_COIN_2_ASSET_ID_ALIAS).build(),
                        new AssetByAssetIdProjectionRoot<>()
                                .assetId()
                                .assetIdAlias()
                ).serialize(),
                "data." + DgsConstants.QUERY.AssetByAssetId,
                new TypeRef<Asset>() {
                }))
                .isNotNull()
                .satisfies(asset -> {
                    assertEquals(UNLIMITED_ROYLLO_COIN_2_ASSET_ID, asset.getAssetId());
                    assertEquals(UNLIMITED_ROYLLO_COIN_2_ASSET_ID_ALIAS, asset.getAssetIdAlias());
                });
    }

}
