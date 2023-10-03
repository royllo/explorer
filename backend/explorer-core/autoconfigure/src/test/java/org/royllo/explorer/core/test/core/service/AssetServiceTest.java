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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_DTO;
import static org.royllo.explorer.core.util.enums.AssetType.NORMAL;
import static org.royllo.test.TestAssets.ACTIVE_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TestAssets.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TestTransactions.BITCOIN_TRANSACTION_1_TXID;
import static org.royllo.test.TestTransactions.BITCOIN_TRANSACTION_3_TXID;

@SpringBootTest
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
        Page<AssetDTO> results = assetService.queryAssets("NON-EXISTING", 1, 5);
        assertEquals(0, results.getTotalElements());
        assertEquals(0, results.getTotalPages());

        // Searching for an asset with its asset id.
        results = assetService.queryAssets(ROYLLO_COIN_ASSET_ID, 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(1, results.getContent().get(0).getId());

        // Searching for an asset with its partial name (activeRoylloCoin) - only 1 result.
        results = assetService.queryAssets("veR", 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, results.getContent().get(0).getAssetId());

        // Searching for an asset with its partial name uppercase (activeRoylloCoin) - only 1 result.
        results = assetService.queryAssets("VER", 1, 5);
        assertEquals(1, results.getTotalElements());
        assertEquals(1, results.getTotalPages());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, results.getContent().get(0).getAssetId());

        // Searching for an asset with its partial name corresponding to two assets.
        // The last coin we insert in database has "TestPaginationCoin0" as name, so it appears first in results.
        results = assetService.queryAssets("PaginationCoin", 1, 5);
        assertEquals(9, results.getTotalElements());
        assertEquals(2, results.getTotalPages());
        Set<Long> ids = results.stream()
                .map(AssetDTO::getId)
                .collect(Collectors.toSet());
        assertTrue(ids.contains(1009L));
        assertTrue(ids.contains(1001L));
        assertTrue(ids.contains(1002L));
        assertTrue(ids.contains(1003L));
        assertTrue(ids.contains(1004L));

        // =============================================================================================================
        // We have 9 assets to tests pagination.

        // Searching for the 9 assets with a page size of 4.
        results = assetService.queryAssets("TestPaginationCoin", 1, 4);
        assertEquals(9, results.getTotalElements());
        assertEquals(3, results.getTotalPages());

        // Searching for the 9 assets with a page size of 5 - Page 0.
        results = assetService.queryAssets("TestPaginationCoin", 1, 5);
        assertEquals(5, results.getNumberOfElements());
        assertEquals(9, results.getTotalElements());
        assertEquals(2, results.getTotalPages());
        assertEquals(1009, results.getContent().get(0).getId());
        assertEquals(1001, results.getContent().get(1).getId());
        assertEquals(1002, results.getContent().get(2).getId());
        assertEquals(1003, results.getContent().get(3).getId());
        assertEquals(1004, results.getContent().get(4).getId());

        // Searching for the 9 assets with a page size of 5 - Page 1.
        results = assetService.queryAssets("TestPaginationCoin", 2, 5);
        assertEquals(4, results.getNumberOfElements());
        assertEquals(9, results.getTotalElements());
        assertEquals(2, results.getTotalPages());
        assertEquals(1005, results.getContent().get(0).getId());
        assertEquals(1006, results.getContent().get(1).getId());
        assertEquals(1007, results.getContent().get(2).getId());
        assertEquals(1008, results.getContent().get(3).getId());
    }

    @Test
    @DisplayName("addAsset()")
    public void addAsset() {
        // We retrieve a bitcoin transaction output from database for our test.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(BITCOIN_TRANSACTION_1_TXID, 0);
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
                .assetId(ACTIVE_ROYLLO_COIN_ASSET_ID)
                .genesisPoint(bto.get())
                .metaDataHash("metadata")
                .name("name")
                .outputIndex(0)
                .version(0)
                .type(NORMAL)
                .amount(BigInteger.ONE)
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
                        .txId(BITCOIN_TRANSACTION_3_TXID)
                        .vout(0)
                        .build())
                .metaDataHash("my meta data hash")
                .name("testCoin")
                .outputIndex(8)
                .version(0)
                .type(NORMAL)
                .amount(new BigInteger("1"))
                .build());

        // Testing asset value.
        assertNotNull(asset1.getId());
        assertEquals(ANONYMOUS_USER_DTO.getId(), asset1.getCreator().getId());
        assertEquals("my asset id", asset1.getAssetId());
        // Genesis.
        assertNotNull(asset1.getGenesisPoint());
        assertNotNull(asset1.getGenesisPoint().getId());
        verifyTransaction(asset1.getGenesisPoint(), BITCOIN_TRANSACTION_3_TXID);
        // Asset value data.
        assertEquals("my meta data hash", asset1.getMetaDataHash());
        assertEquals("testCoin", asset1.getName());
        assertEquals(8, asset1.getOutputIndex());
        assertEquals(0, asset1.getVersion());
        assertEquals(NORMAL, asset1.getType());
        assertEquals(0, asset1.getAmount().compareTo(new BigInteger("1")));
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
                        .assetIdSig("assetIdSig-1")
                        .rawGroupKey("rawGroupKey-1")
                        .tweakedGroupKey("tweakedGroupKey-1")
                        .build())
                .build());

        // Testing asset value.
        assertNotNull(asset2.getId());
        assertEquals(ANONYMOUS_USER_DTO.getId(), asset2.getCreator().getId());
        assertEquals("assetId2", asset2.getAssetId());
        // Genesis.
        assertNotNull(asset2.getGenesisPoint());
        assertEquals(6, asset2.getGenesisPoint().getId());
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
        assertEquals("assetIdSig-1", asset2.getAssetGroup().getAssetIdSig());
        assertEquals("rawGroupKey-1", asset2.getAssetGroup().getRawGroupKey());
        assertEquals("tweakedGroupKey-1", asset2.getAssetGroup().getTweakedGroupKey());

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
                        .assetIdSig("assetIdSig-1")
                        .rawGroupKey("rawGroupKey-1")
                        .tweakedGroupKey("tweakedGroupKey-1")
                        .build())
                .build());
        // Asset group.
        assertNotNull(asset3.getAssetGroup());
        assertEquals(assetGroupCount + 1, assetGroupRepository.findAll().size());
        assertNotNull(asset3.getAssetGroup().getId());
        assertEquals("assetIdSig-1", asset3.getAssetGroup().getAssetIdSig());
        assertEquals("rawGroupKey-1", asset3.getAssetGroup().getRawGroupKey());
        assertEquals("tweakedGroupKey-1", asset3.getAssetGroup().getTweakedGroupKey());
    }

    @Test
    @DisplayName("getAsset()")
    public void getAsset() {
        // Non-existing asset.
        Optional<AssetDTO> asset = assetService.getAsset(0);
        assertFalse(asset.isPresent());

        // Existing asset on testnet and in our database initialization script ("My Royllo coin") .
        // Asset id is 1 as My Royllo Coin is the only coin inserted in default database.
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertEquals(ROYLLO_COIN_ASSET_ID, asset.get().getAssetId());
        assertNotNull(asset.get().getCreator());
        assertEquals(ANONYMOUS_USER_DTO.getId(), asset.get().getCreator().getId());
        verifyAsset(asset.get(), ROYLLO_COIN_ASSET_ID);

        // getAsset() on an asset that has no asset group
        asset = assetService.getAsset(999);
        assertTrue(asset.isPresent());
        assertNull(asset.get().getAssetGroup());
    }

    @Test
    @DisplayName("getAssetByAssetId()")
    public void getAssetByAssetId() {
        // Non-existing asset.
        Optional<AssetDTO> asset = assetService.getAssetByAssetId("NON-EXISTING");
        assertFalse(asset.isPresent());

        // Existing asset on testnet and in our database initialization script ("My Royllo coin") .
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertEquals(ROYLLO_COIN_ASSET_ID, asset.get().getAssetId());
        assertNotNull(asset.get().getCreator());
        assertEquals(ANONYMOUS_USER_DTO.getId(), asset.get().getCreator().getId());
        verifyAsset(asset.get(), ROYLLO_COIN_ASSET_ID);

        // getAsset() on an asset that has no asset group
        asset = assetService.getAsset(999);
        assertTrue(asset.isPresent());
        assertNull(asset.get().getAssetGroup());
    }

}
