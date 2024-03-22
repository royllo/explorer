package org.royllo.explorer.core.test.core.service.asset;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_DTO;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.enums.AssetType.NORMAL;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_COIN_FROM_TEST;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;

@SpringBootTest
@DirtiesContext
@DisplayName("AssetStateService tests")
public class AssetStateServiceTest extends TestWithMockServers {

    @Autowired
    private AssetGroupRepository assetGroupRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetStateRepository assetStateRepository;

    @Autowired
    private BitcoinService bitcoinService;

    @Autowired
    private AssetStateService assetStateService;

    /*** Royllo coin genesis transaction output. */
    BitcoinTransactionOutputDTO roylloCoinTransactionOutput;

    /** Non-existing transaction. */
    BitcoinTransactionOutputDTO nonExistingTransaction;

    @BeforeEach
    void setUp() {
        // We retrieve a bitcoin transaction output from database for our test.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 0);
        assertTrue(bto.isPresent());
        this.roylloCoinTransactionOutput = bto.get();

        // Non-existing transaction.
        nonExistingTransaction = BitcoinTransactionOutputDTO.builder()
                .txId("NON_EXISTING_TXID")
                .vout(0)
                .build();
    }

    @Test
    @DisplayName("addAssetState()")
    public void addAssetState() {
        // =============================================================================================================
        // Constraint tests.

        // Asset group parameter is null.
        assertThrows(IllegalArgumentException.class, () -> assetStateService.addAssetState(null));

        // Asset state are invalid.
        ConstraintViolationException violations = assertThrows(ConstraintViolationException.class, () -> {
            assetStateService.addAssetState(AssetStateDTO.builder().build());
        });
        assertEquals(9, violations.getConstraintViolations().size());
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("amount")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("asset")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("anchorBlockHash")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("version")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("scriptVersion")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("anchorTx")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("anchorOutpoint")));
        assertTrue(violations.getConstraintViolations().stream().anyMatch(violation -> violation.getPropertyPath().toString().contains("scriptKey")));

        // =============================================================================================================
        // Normal behavior tests.

        // We create an asset state from scratch (The asset doesn't exist in database).
        int assetGroupCount = assetGroupRepository.findAll().size();
        int assetCount = assetRepository.findAll().size();
        int assetStateCount = assetStateRepository.findAll().size();

        final AssetStateDTO firstAssetStateCreated = assetStateService.addAssetState(AssetStateDTO.builder()
                .id(1L)
                .creator(ANONYMOUS_USER_DTO)
                .asset(AssetDTO.builder()
                        .assetId("TEST_COIN_ASSET_ID_000000000000000000000000000000000000000000000")
                        .name("TEST_COIN_ASSET_NAME_000000000000000000000000000000000000000000000")
                        .creator(ANONYMOUS_USER_DTO)
                        .genesisPoint(roylloCoinTransactionOutput)
                        .type(NORMAL)
                        .outputIndex(1)
                        .version(1)
                        .build())
                .anchorBlockHash("TEST_ANCHOR_BLOCK_HASH")
                .anchorOutpoint(roylloCoinTransactionOutput)
                .anchorTx("TEST_ANCHOR_TX")
                .internalKey("TEST_INTERNAL_KEY")
                .merkleRoot("TEST_MERKLE_ROOT")
                .tapscriptSibling("TEST_TAPSCRIPT_SIBLING")
                .scriptVersion(0)
                .scriptKey("TEST_SCRIPT_KEY")
                .version("1")
                .amount(new BigInteger("1000"))
                .build());

        // We check what has been created.
        assertEquals(assetGroupCount, assetGroupRepository.findAll().size());
        assertEquals(assetCount + 1, assetRepository.findAll().size());
        assertEquals(assetStateCount + 1, assetStateRepository.findAll().size());

        // We check what was created.
        assertNotNull(firstAssetStateCreated.getId());
        assertNotEquals(1, firstAssetStateCreated.getId());
        // Asset state id is calculated from the asset state data.
        // TEST_COIN_ASSET_ID_000000000000000000000000000000000000000000000_d22de9a2de657c262a2c20c14500b8adca593c96f3fb85bfd756ba66626b9fcb:0_TEST_SCRIPT_KEY
        assertEquals("94652875cf23319515f01f416f390803c46a2129b1ae38a97e16d33fcf3578d7", firstAssetStateCreated.getAssetStateId());
        // User.
        assertNotNull(firstAssetStateCreated.getCreator());
        assertEquals(ANONYMOUS_USER_ID, firstAssetStateCreated.getCreator().getUserId());
        // Asset.
        assertNotNull(firstAssetStateCreated.getAsset());
        assertNotNull(firstAssetStateCreated.getAsset().getId());
        assertEquals("TEST_COIN_ASSET_ID_000000000000000000000000000000000000000000000", firstAssetStateCreated.getAsset().getAssetId());
        // Asset group.
        assertNull(firstAssetStateCreated.getAsset().getAssetGroup());
        // Asset state data.
        assertEquals("TEST_ANCHOR_BLOCK_HASH", firstAssetStateCreated.getAnchorBlockHash());
        assertEquals("d22de9a2de657c262a2c20c14500b8adca593c96f3fb85bfd756ba66626b9fcb", firstAssetStateCreated.getAnchorOutpoint().getTxId());
        assertEquals(0, firstAssetStateCreated.getAnchorOutpoint().getVout());
        assertEquals("TEST_ANCHOR_TX", firstAssetStateCreated.getAnchorTx());
        assertEquals("TEST_INTERNAL_KEY", firstAssetStateCreated.getInternalKey());
        assertEquals("TEST_MERKLE_ROOT", firstAssetStateCreated.getMerkleRoot());
        assertEquals("TEST_TAPSCRIPT_SIBLING", firstAssetStateCreated.getTapscriptSibling());
        assertEquals(0, firstAssetStateCreated.getScriptVersion());
        assertEquals("TEST_SCRIPT_KEY", firstAssetStateCreated.getScriptKey());

        // Test if the asset exists in database.
        final Optional<AssetDTO> assetCreated = assetService.getAssetByAssetIdOrAlias("TEST_COIN_ASSET_ID_000000000000000000000000000000000000000000000");
        assertTrue(assetCreated.isPresent());
        assertNotNull(assetCreated.get().getId());

        // =============================================================================================================
        // We try to create the same asset state again.
        IllegalArgumentException i = assertThrows(IllegalArgumentException.class, () -> assetStateService.addAssetState(AssetStateDTO.builder()
                .creator(ANONYMOUS_USER_DTO)
                .asset(AssetDTO.builder()
                        .assetId("TEST_COIN_ASSET_ID_000000000000000000000000000000000000000000000")
                        .name("TEST_COIN_ASSET_NAME_000000000000000000000000000000000000000000000")
                        .creator(ANONYMOUS_USER_DTO)
                        .genesisPoint(roylloCoinTransactionOutput)
                        .type(NORMAL)
                        .outputIndex(1)
                        .version(1)
                        .build())
                .anchorBlockHash("TEST_ANCHOR_BLOCK_HASH")
                .anchorOutpoint(roylloCoinTransactionOutput)
                .anchorTx("TEST_ANCHOR_TX")
                .internalKey("TEST_INTERNAL_KEY")
                .merkleRoot("TEST_MERKLE_ROOT")
                .tapscriptSibling("TEST_TAPSCRIPT_SIBLING")
                .scriptVersion(0)
                .scriptKey("TEST_SCRIPT_KEY")
                .version("1")
                .amount(new BigInteger("1000"))
                .build()));
        assertEquals("Asset state already exists", i.getMessage());

        // We check that nothing has been created.
        assertEquals(assetGroupCount, assetGroupRepository.findAll().size());
        assertEquals(assetCount + 1, assetRepository.findAll().size());
        assertEquals(assetStateCount + 1, assetStateRepository.findAll().size());

        // =============================================================================================================
        // We create a second asset state from scratch (on the asset we created previously).
        assetStateService.addAssetState(AssetStateDTO.builder()
                .creator(ANONYMOUS_USER_DTO)
                .asset(AssetDTO.builder()
                        .assetId("TEST_COIN_ASSET_ID_000000000000000000000000000000000000000000000")
                        .genesisPoint(roylloCoinTransactionOutput)
                        .build())
                .anchorBlockHash("TEST_ANCHOR_BLOCK_HASH_2")
                .anchorOutpoint(roylloCoinTransactionOutput)
                .anchorTx("TEST_ANCHOR_TX_2")
                .internalKey("TEST_INTERNAL_KEY_2")
                .merkleRoot("TEST_MERKLE_ROOT_2")
                .tapscriptSibling("TEST_TAPSCRIPT_SIBLING_2")
                .scriptVersion(1)
                .scriptKey("TEST_SCRIPT_KEY_2")
                .version("1")
                .amount(new BigInteger("1000"))
                .build());

        // We check what has been created.
        assertEquals(assetGroupCount, assetGroupRepository.findAll().size());
        assertEquals(assetCount + 1, assetRepository.findAll().size());
        assertEquals(assetStateCount + 2, assetStateRepository.findAll().size());

        // We check what was created (this time we used the getByAssetId() method).
        // TEST_COIN_ASSET_ID_000000000000000000000000000000000000000000000_d22de9a2de657c262a2c20c14500b8adca593c96f3fb85bfd756ba66626b9fcb:0_TEST_SCRIPT_KEY_2

        final Optional<AssetStateDTO> secondAssetStateCreated = assetStateService.getAssetStateByAssetStateId("34fece2920b1526dad88b80e8a554e3f21b0d57670d182b06cb077547a2ff203");
        assertTrue(secondAssetStateCreated.isPresent());
        assertNotNull(secondAssetStateCreated.get().getId());
        // Asset state id is calculated from the asset state data.
        // TEST_COIN_ASSET_ID_000000000000000000000000000000000000000000000_d22de9a2de657c262a2c20c14500b8adca593c96f3fb85bfd756ba66626b9fcb:0_TEST_SCRIPT_KEY
        assertEquals("34fece2920b1526dad88b80e8a554e3f21b0d57670d182b06cb077547a2ff203", secondAssetStateCreated.get().getAssetStateId());
        // User.
        assertNotNull(secondAssetStateCreated.get().getCreator());
        assertEquals(ANONYMOUS_USER_ID, secondAssetStateCreated.get().getCreator().getUserId());
        // Asset.
        assertNotNull(secondAssetStateCreated.get().getAsset());
        assertNotNull(secondAssetStateCreated.get().getAsset().getId());
        assertEquals("TEST_COIN_ASSET_ID_000000000000000000000000000000000000000000000", secondAssetStateCreated.get().getAsset().getAssetId());
        // Asset group.
        assertNull(secondAssetStateCreated.get().getAsset().getAssetGroup());
        // Asset state data.
        assertEquals("TEST_ANCHOR_BLOCK_HASH_2", secondAssetStateCreated.get().getAnchorBlockHash());
        assertEquals("d22de9a2de657c262a2c20c14500b8adca593c96f3fb85bfd756ba66626b9fcb", secondAssetStateCreated.get().getAnchorOutpoint().getTxId());
        assertEquals(0, secondAssetStateCreated.get().getAnchorOutpoint().getVout());
        assertEquals("TEST_ANCHOR_TX_2", secondAssetStateCreated.get().getAnchorTx());
        assertEquals("TEST_INTERNAL_KEY_2", secondAssetStateCreated.get().getInternalKey());
        assertEquals("TEST_MERKLE_ROOT_2", secondAssetStateCreated.get().getMerkleRoot());
        assertEquals("TEST_TAPSCRIPT_SIBLING_2", secondAssetStateCreated.get().getTapscriptSibling());
        assertEquals(1, secondAssetStateCreated.get().getScriptVersion());
        assertEquals("TEST_SCRIPT_KEY_2", secondAssetStateCreated.get().getScriptKey());
    }

    @Test
    @DisplayName("getAssetStateByAssetStateId()")
    public void getAssetStateByAssetStateId() {
        // =============================================================================================================
        // Non-existing asset group.
        AssetStateDTO assetState = assetStateService.getAssetStateByAssetStateId("NON_EXISTING_ASSET_STATE_ID").orElse(null);
        assertNull(assetState);

        // =============================================================================================================
        // Existing asset state on testnet and in our database initialization script ("roylloCoin").
        assetState = assetStateService
                .getAssetStateByAssetStateId(ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId())
                .orElse(null);
        assertNotNull(assetState);
        assertEquals(1, assetState.getId());
        assertEquals(ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId(), assetState.getAssetStateId());
        // User.
        assertNotNull(assetState.getCreator());
        assertEquals(ANONYMOUS_ID, assetState.getCreator().getId());
        // Asset & asset group.
        verifyAsset(assetState.getAsset(), ROYLLO_COIN_ASSET_ID);
        // Asset state data.
        verifyAssetState(assetState,
                ROYLLO_COIN_ASSET_ID,
                assetState.getAnchorOutpoint().getTxId(),
                assetState.getAnchorOutpoint().getVout(),
                assetState.getScriptKey());
    }

    @Test
    @DisplayName("getAssetStatesByAssetId()")
    public void getAssetStatesByAssetId() {
        // Searching for asset states for an asset state id that doesn't exist.
        Page<AssetStateDTO> results = assetStateService.getAssetStatesByAssetId("NON_EXISTING_ASSET_STATE_ID", 1, 5);
        assertEquals(0, results.getTotalElements());
        assertEquals(0, results.getTotalPages());

        // Searching for the asset states of an existing asset.
        results = assetStateService.getAssetStatesByAssetId(TRICKY_ROYLLO_COIN_ASSET_ID, 1, 2);
        assertEquals(4, results.getTotalElements());
        assertEquals(2, results.getSize());
        assertEquals(2, results.getTotalPages());
    }

}
