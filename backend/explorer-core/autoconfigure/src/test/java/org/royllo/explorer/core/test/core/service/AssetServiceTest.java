package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.math.BigInteger.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_DTO;
import static org.royllo.explorer.core.util.enums.AssetType.NORMAL;
import static org.royllo.explorer.core.util.mapper.AssetMapperDecorator.ALIAS_LENGTH;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_NFT_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_NFT_ASSET_ID_ALIAS;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_FROM_TEST;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID_ALIAS;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("AssetService tests")
public class AssetServiceTest extends TestWithMockServers {

    @Autowired
    private BitcoinService bitcoinService;

    @Autowired
    private AssetGroupRepository assetGroupRepository;

    @Autowired
    private AssetService assetService;

    @Test
    @DisplayName("queryAssets()")
    public void queryAssets() {
        // Searching for an asset that doesn't exist.
        Page<AssetDTO> results = assetService.queryAssets("NON_EXISTING_ASSET_ID", 1, 5);
        assertEquals(0, results.getTotalElements());
        assertEquals(0, results.getTotalPages());

        // Searching for an asset with its group asset id
        results = assetService.queryAssets(SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey(), 1, 5);
        assertEquals(3, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(SET_OF_ROYLLO_NFT_1_ASSET_ID, results.getContent().get(0).getAssetId());
        assertEquals(SET_OF_ROYLLO_NFT_2_ASSET_ID, results.getContent().get(1).getAssetId());
        assertEquals(SET_OF_ROYLLO_NFT_3_ASSET_ID, results.getContent().get(2).getAssetId());

        // Searching for an asset with its asset id.
        results = assetService.queryAssets(ROYLLO_COIN_ASSET_ID, 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(1, results.getContent().get(0).getId());

        // Searching for an asset with its asset id alias.
        results = assetService.queryAssets(TRICKY_ROYLLO_COIN_ASSET_ID_ALIAS, 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, results.getContent().get(0).getAssetId());

        // Searching for an asset with its partial name (trickyCoin) - only 1 result.
        results = assetService.queryAssets("ky", 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, results.getContent().get(0).getAssetId());

        // Searching for an asset with its partial name uppercase (trickyRoylloCoin) - only 1 result.
        results = assetService.queryAssets("kyR", 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, results.getContent().get(0).getAssetId());

        // Searching for an asset with its partial name corresponding to eight assets.
        results = assetService.queryAssets("royllo", 1, 4);
        assertEquals(8, results.getTotalElements());
        assertEquals(2, results.getTotalPages());
        Set<Long> ids = results.stream()
                .map(AssetDTO::getId)
                .collect(Collectors.toSet());
        assertTrue(ids.contains(1L));
        assertTrue(ids.contains(2L));
        assertTrue(ids.contains(3L));
        assertTrue(ids.contains(4L));
    }

    @Test
    @DisplayName("addAsset()")
    public void addAsset() {
        // We retrieve a bitcoin transaction output from database for our test.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 0);
        assertTrue(bto.isPresent());

        // =============================================================================================================
        // First test - Trying to save an existing asset.
        AssertionError e = assertThrows(AssertionError.class, () -> assetService.addAsset(AssetDTO.builder().id(1L).build()));
        assertEquals("Asset already exists", e.getMessage());

        // =============================================================================================================
        // Second test - Bitcoin transaction is null.
        e = assertThrows(AssertionError.class, () -> assetService.addAsset(AssetDTO.builder().build()));
        assertEquals("Bitcoin transaction is required", e.getMessage());

        // =============================================================================================================
        // Third test - AssetId is already in the database.
        e = assertThrows(AssertionError.class, () -> assetService.addAsset(AssetDTO.builder()
                .creator(ANONYMOUS_USER_DTO)
                .assetId(ROYLLO_COIN_ASSET_ID)
                .genesisPoint(bto.get())
                .metaDataHash("metadata")
                .name("name")
                .outputIndex(0)
                .version(0)
                .type(NORMAL)
                .amount(ONE)
                .build()));
        assertTrue(e.getMessage().endsWith("already registered"));

        // =============================================================================================================
        // We add a first asset.
        // The bitcoin transaction output doesn't exist in database, the mock returns it and transaction is created.
        // There is no asset group, no one should be created.
        int assetGroupCount = assetGroupRepository.findAll().size();
        final AssetDTO asset1 = assetService.addAsset(AssetDTO.builder()
                .creator(ANONYMOUS_USER_DTO)
                .assetId("my asset id")
                .genesisPoint(BitcoinTransactionOutputDTO.builder()
                        .txId(ROYLLO_COIN_GENESIS_TXID)
                        .vout(0)
                        .build())
                .metaDataHash("my meta data hash")
                .name("testCoin")
                .outputIndex(8)
                .version(0)
                .type(NORMAL)
                .amount(ONE)
                .build());

        // Testing asset value.
        assertNotNull(asset1.getId());
        assertEquals(ANONYMOUS_USER_DTO.getId(), asset1.getCreator().getId());
        assertEquals("my asset id", asset1.getAssetId());
        assertNotNull(asset1.getAssetIdAlias());
        assertEquals(ALIAS_LENGTH, asset1.getAssetIdAlias().length());
        // Genesis.
        assertNotNull(asset1.getGenesisPoint());
        assertNotNull(asset1.getGenesisPoint().getId());
        verifyTransaction(asset1.getGenesisPoint(), ROYLLO_COIN_GENESIS_TXID);
        // Asset value data.
        assertEquals("my meta data hash", asset1.getMetaDataHash());
        assertEquals("testCoin", asset1.getName());
        assertEquals(8, asset1.getOutputIndex());
        assertEquals(0, asset1.getVersion());
        assertEquals(NORMAL, asset1.getType());
        assertEquals(0, asset1.getAmount().compareTo(ONE));
        // Asset group.
        assertNull(asset1.getAssetGroup());
        assertEquals(assetGroupCount, assetGroupRepository.findAll().size());

        // =============================================================================================================
        // We add a second asset.
        // Creator is not set.
        // The transaction already exists in database.
        // An asset group is set, but it doesn't exist in database for the moment.
        final AssetDTO asset2 = assetService.addAsset(AssetDTO.builder()
                .assetId("assetId2")
                .genesisPoint(bto.get())
                .metaDataHash("metaData2")
                .name("testCoin2")
                .outputIndex(9)
                .version(1)
                .type(NORMAL)
                .amount(new BigInteger("11"))
                .assetGroup(AssetGroupDTO.builder()
                        .assetGroupId("tweakedGroupKey-1")
                        .rawGroupKey("rawGroupKey-1")
                        .tweakedGroupKey("tweakedGroupKey-1")
                        .assetWitness("assetIdSig-1")
                        .build())
                .build());

        // Testing asset value.
        assertNotNull(asset2.getId());
        assertEquals(ANONYMOUS_USER_DTO.getId(), asset2.getCreator().getId());
        assertEquals("assetId2", asset2.getAssetId());
        assertNotNull(asset2.getAssetIdAlias());
        assertEquals(ALIAS_LENGTH, asset2.getAssetIdAlias().length());
        // Genesis.
        assertNotNull(asset2.getGenesisPoint());
        assertNotNull(asset2.getGenesisPoint().getId());
        // Asset value data.
        assertEquals("metaData2", asset2.getMetaDataHash());
        assertEquals("testCoin2", asset2.getName());
        assertEquals(9, asset2.getOutputIndex());
        assertEquals(1, asset2.getVersion());
        assertEquals(NORMAL, asset2.getType());
        assertEquals(0, asset2.getAmount().compareTo(new BigInteger("11")));
        // Asset group.
        assertNotNull(asset2.getAssetGroup());
        assertEquals(assetGroupCount + 1, assetGroupRepository.findAll().size());
        assertNotNull(asset2.getAssetGroup().getId());
        assertEquals("tweakedGroupKey-1", asset2.getAssetGroup().getAssetGroupId());
        assertEquals("rawGroupKey-1", asset2.getAssetGroup().getRawGroupKey());
        assertEquals("tweakedGroupKey-1", asset2.getAssetGroup().getTweakedGroupKey());
        assertEquals("assetIdSig-1", asset2.getAssetGroup().getAssetWitness());

        // =============================================================================================================
        // We add a third asset.
        // The transaction already exists in database.
        // An asset group is set, but it already exists in the database.
        // We check that a new asset group is not created.
        final AssetDTO asset3 = assetService.addAsset(AssetDTO.builder()
                .assetId("assetId3")
                .genesisPoint(bto.get())
                .metaDataHash("metaData3")
                .name("testCoin3")
                .outputIndex(9)
                .version(1)
                .type(NORMAL)
                .amount(new BigInteger("111"))
                .assetGroup(AssetGroupDTO.builder()
                        .assetGroupId("tweakedGroupKey-1")
                        .tweakedGroupKey("tweakedGroupKey-1")
                        .rawGroupKey("rawGroupKey-1")
                        .assetWitness("assetIdSig-1")
                        .build())
                .build());
        // Asset group.
        assertNotNull(asset3.getAssetGroup());
        assertEquals(assetGroupCount + 1, assetGroupRepository.findAll().size());
        assertEquals("assetId3", asset3.getAssetId());
        assertNotNull(asset3.getAssetIdAlias());
        assertEquals(ALIAS_LENGTH, asset3.getAssetIdAlias().length());
        assertEquals("tweakedGroupKey-1", asset3.getAssetGroup().getTweakedGroupKey());
        assertEquals("assetIdSig-1", asset3.getAssetGroup().getAssetWitness());
        assertEquals("rawGroupKey-1", asset3.getAssetGroup().getRawGroupKey());
        assertEquals("tweakedGroupKey-1", asset3.getAssetGroup().getTweakedGroupKey());
    }

    @Test
    @DisplayName("addAsset() with asset group")
    public void addAssetWithAssetGroup() {
        // We retrieve a bitcoin transaction output from database for our test.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 0);
        assertTrue(bto.isPresent());

        // 4 assets : 1 with no asset group, 2 with the same asset group and 1 with another asset group.
        AssetDTO asset1 = AssetDTO.builder()
                .assetId("asset1")
                .genesisPoint(bto.get())
                .build();
        AssetDTO asset2 = AssetDTO.builder()
                .assetId("asset2")
                .genesisPoint(bto.get())
                .assetGroup(AssetGroupDTO.builder()
                        .assetGroupId("assetGroupId1")
                        .tweakedGroupKey("assetGroup1").build())
                .build();
        AssetDTO asset3 = AssetDTO.builder()
                .assetId("asset3")
                .genesisPoint(bto.get())
                .assetGroup(AssetGroupDTO.builder()
                        .assetGroupId("assetGroupId1")
                        .tweakedGroupKey("assetGroup1").build())
                .build();
        AssetDTO asset4 = AssetDTO.builder()
                .assetId("asset4")
                .genesisPoint(bto.get())
                .assetGroup(AssetGroupDTO.builder()
                        .assetGroupId("assetGroupId2")
                        .tweakedGroupKey("assetGroup2").build())
                .build();

        // Asset creation.
        assetService.addAsset(asset1);
        assetService.addAsset(asset2);
        assetService.addAsset(asset3);
        assetService.addAsset(asset4);

        // Asset retrieval.
        AssetDTO asset1Created = assetService.getAssetByAssetId("asset1").orElse(null);
        AssetDTO asset2Created = assetService.getAssetByAssetId("asset2").orElse(null);
        AssetDTO asset3Created = assetService.getAssetByAssetId("asset3").orElse(null);
        AssetDTO asset4Created = assetService.getAssetByAssetId("asset4").orElse(null);

        // Verification.
        assertNotNull(asset1Created);
        assertNotNull(asset1Created.getId());
        assertNull(asset1Created.getAssetGroup());

        assertNotNull(asset2Created);
        assertNotNull(asset2Created.getId());
        assertNotNull(asset2Created.getAssetGroup());
        assertNotNull(asset2Created.getAssetGroup().getId());
        assertEquals("assetGroup1", asset2Created.getAssetGroup().getTweakedGroupKey());

        assertNotNull(asset3Created);
        assertNotNull(asset3Created.getId());
        assertNotNull(asset3Created.getAssetGroup());
        assertNotNull(asset3Created.getAssetGroup().getId());
        assertEquals("assetGroup1", asset3Created.getAssetGroup().getTweakedGroupKey());

        assertNotNull(asset4Created);
        assertNotNull(asset4Created.getId());
        assertNotNull(asset4Created.getAssetGroup());
        assertNotNull(asset4Created.getAssetGroup().getId());
        assertEquals("assetGroup2", asset4Created.getAssetGroup().getTweakedGroupKey());
    }

    @Test
    @DisplayName("getAsset()")
    public void getAsset() {
        // =============================================================================================================
        // Non-existing asset.
        Optional<AssetDTO> asset = assetService.getAsset(0);
        assertFalse(asset.isPresent());

        // =============================================================================================================
        // Existing asset on testnet and in our database initialization script ("My Royllo coin") .
        // Asset id is 1 as My Royllo Coin is the only coin inserted in default database.
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertEquals(ROYLLO_COIN_ASSET_ID, asset.get().getAssetId());
        assertEquals("roylloCoin", asset.get().getAssetIdAlias());
        assertNotNull(asset.get().getCreator());
        assertEquals(ANONYMOUS_ID, asset.get().getCreator().getId());
        verifyAsset(asset.get(), ROYLLO_COIN_ASSET_ID);

        // getAsset() on an asset that has no asset group
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertNull(asset.get().getAssetGroup());
    }

    @Test
    @DisplayName("getAssetByAssetId()")
    public void getAssetByAssetId() {
        // =============================================================================================================
        // Non-existing asset.
        Optional<AssetDTO> asset = assetService.getAssetByAssetId("NON_EXISTING_ASSET_ID");
        assertFalse(asset.isPresent());

        // =============================================================================================================
        // Existing asset on testnet and in our database initialization script ("roylloCoin") .
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertEquals(ROYLLO_COIN_ASSET_ID, asset.get().getAssetId());
        assertNotNull(asset.get().getCreator());
        assertEquals(ANONYMOUS_ID, asset.get().getCreator().getId());
        verifyAsset(asset.get(), ROYLLO_COIN_ASSET_ID);

        // getAsset() on an asset that has no asset group
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertNull(asset.get().getAssetGroup());

        // Testing another asset in test data.
        asset = assetService.getAssetByAssetId(ROYLLO_NFT_ASSET_ID);
        assertTrue(asset.isPresent());
        assertNotNull(asset.get().getAssetId());
        assertEquals(ROYLLO_NFT_ASSET_ID_ALIAS, asset.get().getAssetIdAlias());

        // Testing with an asset id alias
        asset = assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_2_ASSET_ID_ALIAS);
        assertTrue(asset.isPresent());
        assertNotNull(asset.get().getAssetId());
        assertEquals(SET_OF_ROYLLO_NFT_2_ASSET_ID, asset.get().getAssetId());
        assertEquals(SET_OF_ROYLLO_NFT_2_ASSET_ID_ALIAS, asset.get().getAssetIdAlias());
    }

    @Test
    @DisplayName("getAssetByAssetId()")
    public void getAssetsByAssetGroupId() {
        // Test with an asset group that doesn't exist.
        assertEquals(0, assetService.getAssetsByAssetGroupId("NON_EXISTING_ASSET_GROUP_ID", 1, 5).getTotalElements());

        // Test with an asset group with three assets.
        final String tweakedGroupKey = SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey();
        // with a page size containing everything.
        assertEquals(3, assetService.getAssetsByAssetGroupId(tweakedGroupKey, 1, 5).getTotalElements());
        assertEquals(1, assetService.getAssetsByAssetGroupId(tweakedGroupKey, 1, 5).getTotalPages());
    }

}
