package org.royllo.explorer.batch.test.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.batch.test.util.BaseTest;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.proof.ProofFileService;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("Add proof batch test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"mempoolTransactionServiceMock", "tapdProofServiceMock", "scheduler-disabled"})
public class AddProofRequestBatchTest extends BaseTest {

    @Autowired
    AssetGroupRepository assetGroupRepository;

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    AssetStateRepository assetStateRepository;

    @Autowired
    RequestService requestService;

    @Autowired
    AssetGroupService assetGroupService;

    @Autowired
    AssetService assetService;

    @Autowired
    AssetStateService assetStateService;

    @Autowired
    ProofFileService proofService;

    @Autowired
    AddProofBatch addProofBatch;

    @Test
    @DisplayName("Add proof request processing")
    public void batch() {
        // =============================================================================================================
        // We add an invalid proof that can't be decoded ("INVALID_PROOF").

        // Add the proof
        AddProofRequestDTO invalidProofRequest = requestService.createAddProofRequest("INVALID_PROOF");
        assertNotNull(invalidProofRequest);
        assertEquals(OPENED, invalidProofRequest.getStatus());

        // Process the request.
        addProofBatch.processRequests();
        final Optional<RequestDTO> invalidProofRequestTreated = requestService.getRequest(invalidProofRequest.getId());
        assertTrue(invalidProofRequestTreated.isPresent());
        assertFalse(invalidProofRequestTreated.get().isSuccessful());
        assertEquals(FAILURE, invalidProofRequestTreated.get().getStatus());
        assertEquals("An error occurred while decoding", invalidProofRequestTreated.get().getErrorMessage());

        // =============================================================================================================
        // "My Royllo coin": The asset is already in our database.
        // A request with "MY_ROYLLO_COIN_RAW_PROOF" is also in database (linked to the asset).
        // If we add a request to add this proof, we should get an error because the proof already exists.
        // (Because we check if the proof is already in database before adding it thanks to the proof_id field).

        // Add the proof
        AddProofRequestDTO myRoylloCoinRequest = requestService.createAddProofRequest(ROYLLO_COIN_RAW_PROOF);
        assertNotNull(myRoylloCoinRequest);
        assertEquals(OPENED, myRoylloCoinRequest.getStatus());

        // Process the request.
        addProofBatch.processRequests();
        final Optional<RequestDTO> myRoylloCoinRequestTreated = requestService.getRequest(myRoylloCoinRequest.getId());
        assertTrue(myRoylloCoinRequestTreated.isPresent());
        assertFalse(myRoylloCoinRequestTreated.get().isSuccessful());
        assertEquals(FAILURE, myRoylloCoinRequestTreated.get().getStatus());
        assertEquals("This proof file is already registered with proof id: " + ROYLLO_COIN_PROOF_ID, myRoylloCoinRequestTreated.get().getErrorMessage());

        // =============================================================================================================
        // "Unknown Royllo coin": The asset and the proof are not in our database.
        // After processing the request, the asset group, the asset, the asset state update and the proof must be added.
        // When we try to add again the same proof, we should get an error.

        // Check that the asset group, the asset, the asset state and the proof does not exist.
        assertFalse(assetGroupService.getAssetGroupByRawGroupKey(UNKNOWN_ROYLLO_COIN_RAW_GROUP_KEY).isPresent());
        assertFalse(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(UKNOWN_ROYLLO_COIN_ASSET_STATE_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID).isPresent());

        // Add the request
        AddProofRequestDTO unknownRoylloCoinRequest = requestService.createAddProofRequest(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(unknownRoylloCoinRequest);

        // Process the request and test its values after.
        addProofBatch.processRequests();
        final Optional<RequestDTO> unknownRoylloCoinRequestTreated = requestService.getRequest(unknownRoylloCoinRequest.getId());
        assertTrue(unknownRoylloCoinRequestTreated.isPresent());
        assertTrue(unknownRoylloCoinRequestTreated.get().isSuccessful());
        assertEquals(SUCCESS, unknownRoylloCoinRequestTreated.get().getStatus());
        assertNotNull(((AddProofRequestDTO) unknownRoylloCoinRequestTreated.get()).getAsset());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID, ((AddProofRequestDTO) unknownRoylloCoinRequestTreated.get()).getAsset().getAssetId());

        // Check that the asset group, the asset, the asset state and the proof now exists.
        assertTrue(assetGroupService.getAssetGroupByRawGroupKey(UNKNOWN_ROYLLO_COIN_RAW_GROUP_KEY).isPresent());
        assertTrue(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(UKNOWN_ROYLLO_COIN_ASSET_STATE_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID).isPresent());

        // We will now check the data created for Uknown royllo coin.
        final Optional<AssetStateDTO> unknownRoylloCoin = assetStateService.getAssetStateByAssetStateId(UKNOWN_ROYLLO_COIN_ASSET_STATE_ID);
        assertTrue(unknownRoylloCoin.isPresent());
        assertNotNull(unknownRoylloCoin.get().getId());
        // Asset state id is calculated from the asset state data.
        assertEquals(UKNOWN_ROYLLO_COIN_ASSET_STATE_ID, unknownRoylloCoin.get().getAssetStateId());
        // User.
        assertNotNull(unknownRoylloCoin.get().getCreator());
        assertEquals(ANONYMOUS_USER_ID, unknownRoylloCoin.get().getCreator().getUserId());
        // Asset.
        assertNotNull(unknownRoylloCoin.get().getAsset());
        assertNotNull(unknownRoylloCoin.get().getAsset().getId());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID, unknownRoylloCoin.get().getAsset().getAssetId());
        // Asset group.
        assertNotNull(unknownRoylloCoin.get().getAsset().getAssetGroup());
        assertNotNull(unknownRoylloCoin.get().getAsset().getAssetGroup().getId());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID_SIG, unknownRoylloCoin.get().getAsset().getAssetGroup().getAssetIdSig());
        assertEquals(UNKNOWN_ROYLLO_COIN_RAW_GROUP_KEY, unknownRoylloCoin.get().getAsset().getAssetGroup().getRawGroupKey());
        assertEquals(UNKNOWN_ROYLLO_COIN_TWEAKED_GROUP_KEY, unknownRoylloCoin.get().getAsset().getAssetGroup().getTweakedGroupKey());
        // Asset state data.
        assertEquals(UNKNOWN_ROYLLO_COIN_ANCHOR_BLOCK_HASH, unknownRoylloCoin.get().getAnchorBlockHash());
        assertEquals("db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839", unknownRoylloCoin.get().getAnchorOutpoint().getTxId());
        assertEquals(1, unknownRoylloCoin.get().getAnchorOutpoint().getVout());
        assertEquals(UNKNOWN_ROYLLO_COIN_ANCHOR_TX, unknownRoylloCoin.get().getAnchorTx());
        assertEquals(UNKNOWN_ROYLLO_COIN_ANCHOR_TX_ID, unknownRoylloCoin.get().getAnchorTxId());
        assertEquals(UNKNOWN_ROYLLO_COIN_ANCHOR_INTERNAL_KEY, unknownRoylloCoin.get().getInternalKey());
        assertEquals(UNKNOWN_ROYLLO_COIN_TX_MERKLE_ROOT, unknownRoylloCoin.get().getMerkleRoot());
        assertEquals(UNKNOWN_ROYLLO_COIN_TX_TAPSCRIPT_SIBLING, unknownRoylloCoin.get().getTapscriptSibling());
        assertEquals(UNKNOWN_ROYLLO_COIN_SCRIPT_VERSION, unknownRoylloCoin.get().getScriptVersion());
        assertEquals(UNKNOWN_ROYLLO_COIN_SCRIPT_KEY, unknownRoylloCoin.get().getScriptKey());
        // Test if the asset exists in database.
        final Optional<AssetDTO> assetCreated = assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID);
        assertTrue(assetCreated.isPresent());
        assertNotNull(assetCreated.get().getId());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID, assetCreated.get().getAssetId());
        assertEquals(UNKNOWN_ROYLLO_COIN_GENESIS_POINT_TXID, assetCreated.get().getGenesisPoint().getTxId());
        assertEquals(UNKNOWN_ROYLLO_COIN_GENESIS_POINT_VOUT, assetCreated.get().getGenesisPoint().getVout());
        assertEquals(UNKNOWN_ROYLLO_COIN_META, assetCreated.get().getMetaDataHash());
        assertEquals(UNKNOWN_ROYLLO_COIN_NAME, assetCreated.get().getName());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID, assetCreated.get().getAssetId());
        assertEquals(UNKNOWN_ROYLLO_COIN_OUTPUT_INDEX, assetCreated.get().getOutputIndex());
        assertEquals(UNKNOWN_ROYLLO_COIN_VERSION, assetCreated.get().getVersion());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_TYPE, assetCreated.get().getType());
        assertEquals(0, UNKNOWN_ROYLLO_COIN_AMOUNT.compareTo((assetCreated.get().getAmount())));
        // TODO getAssetGroup() should not be null! cf. getAssetByAssetId() test
        // assertNotNull(assetCreated.get().getAssetGroup());

        // We add again the same proof, we should get an error.
        AddProofRequestDTO unknownRoylloCoinRequestBis = requestService.createAddProofRequest(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        addProofBatch.processRequests();
        final Optional<RequestDTO> myRoylloCoinRequestTreatedBis = requestService.getRequest(unknownRoylloCoinRequestBis.getId());
        assertTrue(myRoylloCoinRequestTreatedBis.isPresent());
        assertFalse(myRoylloCoinRequestTreatedBis.get().isSuccessful());
        assertEquals(FAILURE, myRoylloCoinRequestTreatedBis.get().getStatus());
        assertEquals("This proof file is already registered with proof id: " + UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID, myRoylloCoinRequestTreatedBis.get().getErrorMessage());

        // =============================================================================================================
        // "Active Royllo coin" : The asset and the proofs are not in our database.
        // After processing the request, the asset and the proof must be present in database.
        // We should be able to add two other proofs for the same asset.

        // Check that the asset and the proofs does not exist.
        assertFalse(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());

        // Add the request for proof 1 and process it.
        AddProofRequestDTO activeRoylloCoinRequest1 = requestService.createAddProofRequest(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF);
        addProofBatch.processRequests();
        Optional<RequestDTO> activeRoylloCoinRequest1Treated = requestService.getRequest(activeRoylloCoinRequest1.getId());
        assertTrue(activeRoylloCoinRequest1Treated.isPresent());
        assertTrue(activeRoylloCoinRequest1Treated.get().isSuccessful());
        assertEquals(SUCCESS, activeRoylloCoinRequest1Treated.get().getStatus());

        // Check the asset and the proofs.
        assertTrue(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());

        // Add the request for proof 3 and process it.
        AddProofRequestDTO activeRoylloCoinRequest2 = requestService.createAddProofRequest(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF);
        addProofBatch.processRequests();
        Optional<RequestDTO> activeRoylloCoinRequest2Treated = requestService.getRequest(activeRoylloCoinRequest2.getId());
        assertTrue(activeRoylloCoinRequest2Treated.isPresent());
        assertTrue(activeRoylloCoinRequest2Treated.get().isSuccessful());
        assertEquals(SUCCESS, activeRoylloCoinRequest2Treated.get().getStatus());

        // Check the asset and the proofs.
        assertTrue(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());

        // Add the request for proof 2 and process it.
        AddProofRequestDTO activeRoylloCoinRequest3 = requestService.createAddProofRequest(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF);
        addProofBatch.processRequests();
        Optional<RequestDTO> activeRoylloCoinRequest3Treated = requestService.getRequest(activeRoylloCoinRequest3.getId());
        assertTrue(activeRoylloCoinRequest3Treated.isPresent());
        assertTrue(activeRoylloCoinRequest3Treated.get().isSuccessful());
        assertEquals(SUCCESS, activeRoylloCoinRequest3Treated.get().getStatus());

        // Check the asset and the proofs.
        assertTrue(assetService.getAssetByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID).isPresent());
    }

    @Test
    @DisplayName("Integrating TestCoin in order")
    public void testCoinInOrder() throws IOException {

        // =============================================================================================================
        // We retrieve the value to compare it with our DTO.
        final ClassPathResource testCoinDecodeProof1 = new ClassPathResource("tapd/TestCoin/TestCoin-decode-proof-1.json");
        final ClassPathResource testCoinDecodeProof2 = new ClassPathResource("tapd/TestCoin/TestCoin-decode-proof-2-depth-0.json");
        final ClassPathResource testCoinDecodeProof2Depth0 = new ClassPathResource("tapd/TestCoin/TestCoin-decode-proof-2-depth-0.json");
        final ClassPathResource testCoinDecodeProof2Depth1 = new ClassPathResource("tapd/TestCoin/TestCoin-decode-proof-2-depth-1.json");
        final ClassPathResource testCoinDecodeProof3 = new ClassPathResource("tapd/TestCoin/TestCoin-decode-proof-3-depth-0.json");
        DecodedProofResponse testCoinDecodedProof1 = new ObjectMapper().readValue(testCoinDecodeProof1.getInputStream(), DecodedProofResponse.class);
        DecodedProofResponse testCoinDecodedProof2 = new ObjectMapper().readValue(testCoinDecodeProof2.getInputStream(), DecodedProofResponse.class);
        DecodedProofResponse testCoinDecodedProof3 = new ObjectMapper().readValue(testCoinDecodeProof3.getInputStream(), DecodedProofResponse.class);

        // =============================================================================================================
        // We retrieve the number of asset groups, assets and asset states before starting the test.
        long assetGroups = assetGroupRepository.count();
        long assets = assetRepository.count();
        long assetStates = assetStateRepository.count();

        // =============================================================================================================
        // Check that the asset and the proofs does not exist.
        assertFalse(assetService.getAssetByAssetId(TESTCOIN_ASSET_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_1_PROOF_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_2_PROOF_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_3_PROOF_ID).isPresent());

        // =============================================================================================================
        // We import the first proof.
        AddProofRequestDTO testCoinProof1 = requestService.createAddProofRequest(TESTCOIN_RAW_PROOF_1);
        addProofBatch.processRequests();
        Optional<RequestDTO> testCoinProof1Request1Treated = requestService.getRequest(testCoinProof1.getId());
        assertTrue(testCoinProof1Request1Treated.isPresent());
        assertTrue(testCoinProof1Request1Treated.get().isSuccessful());
        assertEquals(SUCCESS, testCoinProof1Request1Treated.get().getStatus());

        // Check what has been created.
        assertEquals(assetGroups, assetGroupRepository.count());
        assertEquals(assets + 1, assetRepository.count());
        assertEquals(assetStates + 1, assetStateRepository.count());
        assertTrue(assetService.getAssetByAssetId(TESTCOIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_1_PROOF_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_2_PROOF_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_3_PROOF_ID).isPresent());

        // Check the proof1 data.
        // assetId + "_" + outpointTxId + ":" + outpointVout + "_" + scriptKey;
        final Optional<AssetStateDTO> testCoinProof1AssetState = assetStateService.getAssetStateByAssetStateId(TESTCOIN_ASSET_STATE_ID_1);
        assertTrue(testCoinProof1AssetState.isPresent());
        assertNotNull(testCoinProof1AssetState.get().getId());
        assertEquals(TESTCOIN_ASSET_STATE_ID_1, testCoinProof1AssetState.get().getAssetStateId());
        // User.
        assertNotNull(testCoinProof1AssetState.get().getCreator());
        assertEquals(ANONYMOUS_ID, testCoinProof1AssetState.get().getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, testCoinProof1AssetState.get().getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, testCoinProof1AssetState.get().getCreator().getUsername());
        // Asset.
        assertNotNull(testCoinProof1AssetState.get().getAsset());
        assertNotNull(testCoinProof1AssetState.get().getCreator());
        assertEquals(ANONYMOUS_ID, testCoinProof1AssetState.get().getAsset().getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, testCoinProof1AssetState.get().getAsset().getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, testCoinProof1AssetState.get().getAsset().getCreator().getUsername());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getAssetGenesis().getAssetId(),
                testCoinProof1AssetState.get().getAsset().getAssetId());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getAssetGenesis().getGenesisPoint(),
                testCoinProof1AssetState.get().getAsset().getGenesisPoint().getTxId() + ":" + testCoinProof1AssetState.get().getAsset().getGenesisPoint().getVout());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getAssetGenesis().getMetaDataHash(),
                testCoinProof1AssetState.get().getAsset().getMetaDataHash());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getAssetGenesis().getName(),
                testCoinProof1AssetState.get().getAsset().getName());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getAssetGenesis().getOutputIndex(),
                testCoinProof1AssetState.get().getAsset().getOutputIndex().longValue());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getAssetGenesis().getVersion(),
                testCoinProof1AssetState.get().getAsset().getVersion().longValue());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getAssetType(),
                testCoinProof1AssetState.get().getAsset().getType().toString());
        assertEquals(0, testCoinDecodedProof1.getDecodedProof().getAsset().getAmount().compareTo(testCoinProof1AssetState.get().getAsset().getAmount()));
        // Assert group.
        assertNull(testCoinProof1AssetState.get().getAsset().getAssetGroup());
        // Asset state.
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getChainAnchor().getAnchorBlockHash(),
                testCoinProof1AssetState.get().getAnchorBlockHash());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getChainAnchor().getAnchorOutpoint(),
                testCoinProof1AssetState.get().getAnchorOutpoint().getTxId() + ":" + testCoinProof1AssetState.get().getAnchorOutpoint().getVout());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getChainAnchor().getAnchorTx(),
                testCoinProof1AssetState.get().getAnchorTx());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getChainAnchor().getAnchorTxId(),
                testCoinProof1AssetState.get().getAnchorTxId());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getChainAnchor().getInternalKey(),
                testCoinProof1AssetState.get().getInternalKey());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getChainAnchor().getMerkleRoot(),
                testCoinProof1AssetState.get().getMerkleRoot());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getChainAnchor().getTapscriptSibling(),
                testCoinProof1AssetState.get().getTapscriptSibling());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getScriptVersion(),
                testCoinProof1AssetState.get().getScriptVersion().longValue());
        assertEquals(testCoinDecodedProof1.getDecodedProof().getAsset().getScriptKey(),
                testCoinProof1AssetState.get().getScriptKey());

        // =============================================================================================================
        // We import a second proof.
        AddProofRequestDTO testCoinProof2 = requestService.createAddProofRequest(TESTCOIN_RAW_PROOF_2);
        addProofBatch.processRequests();
        Optional<RequestDTO> testCoinProof2Request2created = requestService.getRequest(testCoinProof2.getId());
        assertTrue(testCoinProof2Request2created.isPresent());
        assertTrue(testCoinProof2Request2created.get().isSuccessful());
        assertEquals(SUCCESS, testCoinProof2Request2created.get().getStatus());

        // Check what has been created.
        assertEquals(assetGroups, assetGroupRepository.count());
        assertEquals(assets + 1, assetRepository.count());
        assertEquals(assetStates + 2, assetStateRepository.count());
        assertTrue(assetService.getAssetByAssetId(TESTCOIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_1_PROOF_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_2_PROOF_ID).isPresent());
        assertFalse(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_3_PROOF_ID).isPresent());

        // Check the proof1 data.
        final Optional<AssetStateDTO> testCoinProof2AssetState = assetStateService.getAssetStateByAssetStateId(TESTCOIN_ASSET_STATE_ID_2);
        assertTrue(testCoinProof2AssetState.isPresent());
        assertNotNull(testCoinProof2AssetState.get().getId());
        assertEquals(TESTCOIN_ASSET_STATE_ID_2, testCoinProof2AssetState.get().getAssetStateId());
        // User.
        assertNotNull(testCoinProof2AssetState.get().getCreator());
        assertEquals(ANONYMOUS_ID, testCoinProof2AssetState.get().getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, testCoinProof2AssetState.get().getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, testCoinProof2AssetState.get().getCreator().getUsername());
        // Asset.
        assertNotNull(testCoinProof2AssetState.get().getAsset());
        assertNotNull(testCoinProof2AssetState.get().getCreator());
        assertEquals(ANONYMOUS_ID, testCoinProof2AssetState.get().getAsset().getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, testCoinProof2AssetState.get().getAsset().getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, testCoinProof2AssetState.get().getAsset().getCreator().getUsername());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getAssetGenesis().getAssetId(),
                testCoinProof2AssetState.get().getAsset().getAssetId());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getAssetGenesis().getGenesisPoint(),
                testCoinProof2AssetState.get().getAsset().getGenesisPoint().getTxId() + ":" + testCoinProof2AssetState.get().getAsset().getGenesisPoint().getVout());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getAssetGenesis().getMetaDataHash(),
                testCoinProof2AssetState.get().getAsset().getMetaDataHash());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getAssetGenesis().getName(),
                testCoinProof2AssetState.get().getAsset().getName());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getAssetGenesis().getOutputIndex(),
                testCoinProof2AssetState.get().getAsset().getOutputIndex().longValue());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getAssetGenesis().getVersion(),
                testCoinProof2AssetState.get().getAsset().getVersion().longValue());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getAssetType(),
                testCoinProof2AssetState.get().getAsset().getType().toString());
        // TODO The problem here is that different proofs has different amount value. The amount of asset should be set with the first proof.
        // System.out.println("=> " + testCoinDecodedProof2.getDecodedProof().getAsset().getAmount());
        // System.out.println("=> " + testCoinProof2AssetState.get().getAsset().getAmount());
        // assertEquals(0, testCoinDecodedProof2.getDecodedProof().getAsset().getAmount().compareTo(testCoinProof2AssetState.get().getAsset().getAmount()));
        // Assert group.
        assertNull(testCoinProof2AssetState.get().getAsset().getAssetGroup());
        // Asset state.
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getChainAnchor().getAnchorBlockHash(),
                testCoinProof2AssetState.get().getAnchorBlockHash());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getChainAnchor().getAnchorOutpoint(),
                testCoinProof2AssetState.get().getAnchorOutpoint().getTxId() + ":" + testCoinProof2AssetState.get().getAnchorOutpoint().getVout());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getChainAnchor().getAnchorTx(),
                testCoinProof2AssetState.get().getAnchorTx());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getChainAnchor().getAnchorTxId(),
                testCoinProof2AssetState.get().getAnchorTxId());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getChainAnchor().getInternalKey(),
                testCoinProof2AssetState.get().getInternalKey());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getChainAnchor().getMerkleRoot(),
                testCoinProof2AssetState.get().getMerkleRoot());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getChainAnchor().getTapscriptSibling(),
                testCoinProof2AssetState.get().getTapscriptSibling());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getScriptVersion(),
                testCoinProof2AssetState.get().getScriptVersion().longValue());
        assertEquals(testCoinDecodedProof2.getDecodedProof().getAsset().getScriptKey(),
                testCoinProof2AssetState.get().getScriptKey());

        // =============================================================================================================
        // We import the last proof.
        AddProofRequestDTO testCoinProof3 = requestService.createAddProofRequest(TESTCOIN_RAW_PROOF_3);
        addProofBatch.processRequests();
        Optional<RequestDTO> testCoinProof3Request3created = requestService.getRequest(testCoinProof3.getId());
        assertTrue(testCoinProof3Request3created.isPresent());
        assertTrue(testCoinProof3Request3created.get().isSuccessful());
        assertEquals(SUCCESS, testCoinProof3Request3created.get().getStatus());

        // Check what has been created.
        assertEquals(assetGroups, assetGroupRepository.count());
        assertEquals(assets + 1, assetRepository.count());
        assertEquals(assetStates + 3, assetStateRepository.count());
        assertTrue(assetService.getAssetByAssetId(TESTCOIN_ASSET_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_1_PROOF_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_2_PROOF_ID).isPresent());
        assertTrue(proofService.getProofFileByProofFileId(TESTCOIN_RAW_PROOF_3_PROOF_ID).isPresent());

        // Check the proof1 data.
        final Optional<AssetStateDTO> testCoinProof3AssetState = assetStateService.getAssetStateByAssetStateId(TESTCOIN_ASSET_STATE_ID_3);
        assertTrue(testCoinProof3AssetState.isPresent());
        assertNotNull(testCoinProof3AssetState.get().getId());
        assertEquals(TESTCOIN_ASSET_STATE_ID_3, testCoinProof3AssetState.get().getAssetStateId());
        // User.
        assertNotNull(testCoinProof3AssetState.get().getCreator());
        assertEquals(ANONYMOUS_ID, testCoinProof3AssetState.get().getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, testCoinProof3AssetState.get().getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, testCoinProof3AssetState.get().getCreator().getUsername());
        // Asset.
        assertNotNull(testCoinProof3AssetState.get().getAsset());
        assertNotNull(testCoinProof3AssetState.get().getCreator());
        assertEquals(ANONYMOUS_ID, testCoinProof3AssetState.get().getAsset().getCreator().getId());
        assertEquals(ANONYMOUS_USER_ID, testCoinProof3AssetState.get().getAsset().getCreator().getUserId());
        assertEquals(ANONYMOUS_USER_USERNAME, testCoinProof3AssetState.get().getAsset().getCreator().getUsername());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getAssetGenesis().getAssetId(),
                testCoinProof3AssetState.get().getAsset().getAssetId());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getAssetGenesis().getGenesisPoint(),
                testCoinProof3AssetState.get().getAsset().getGenesisPoint().getTxId() + ":" + testCoinProof3AssetState.get().getAsset().getGenesisPoint().getVout());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getAssetGenesis().getMetaDataHash(),
                testCoinProof3AssetState.get().getAsset().getMetaDataHash());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getAssetGenesis().getName(),
                testCoinProof3AssetState.get().getAsset().getName());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getAssetGenesis().getOutputIndex(),
                testCoinProof3AssetState.get().getAsset().getOutputIndex().longValue());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getAssetGenesis().getVersion(),
                testCoinProof3AssetState.get().getAsset().getVersion().longValue());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getAssetType(),
                testCoinProof3AssetState.get().getAsset().getType().toString());
        // TODO The problem here is that different proofs has different amount value. The amount of asset should be set with the first proof.
        // System.out.println("=> " + testCoinDecodedProof3.getDecodedProof().getAsset().getAmount());
        // System.out.println("=> " + testCoinProof3AssetState.get().getAsset().getAmount());
        // assertEquals(0, testCoinDecodedProof3.getDecodedProof().getAsset().getAmount().compareTo(testCoinProof3AssetState.get().getAsset().getAmount()));
        // Assert group.
        assertNull(testCoinProof3AssetState.get().getAsset().getAssetGroup());
        // Asset state.
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getChainAnchor().getAnchorBlockHash(),
                testCoinProof3AssetState.get().getAnchorBlockHash());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getChainAnchor().getAnchorOutpoint(),
                testCoinProof3AssetState.get().getAnchorOutpoint().getTxId() + ":" + testCoinProof3AssetState.get().getAnchorOutpoint().getVout());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getChainAnchor().getAnchorTx(),
                testCoinProof3AssetState.get().getAnchorTx());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getChainAnchor().getAnchorTxId(),
                testCoinProof3AssetState.get().getAnchorTxId());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getChainAnchor().getInternalKey(),
                testCoinProof3AssetState.get().getInternalKey());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getChainAnchor().getMerkleRoot(),
                testCoinProof3AssetState.get().getMerkleRoot());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getChainAnchor().getTapscriptSibling(),
                testCoinProof3AssetState.get().getTapscriptSibling());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getScriptVersion(),
                testCoinProof3AssetState.get().getScriptVersion().longValue());
        assertEquals(testCoinDecodedProof3.getDecodedProof().getAsset().getScriptKey(),
                testCoinProof3AssetState.get().getScriptKey());

    }

}
