package org.royllo.explorer.batch.test.core.proof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
import org.royllo.explorer.core.repository.bitcoin.BitcoinTransactionOutputRepository;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.royllo.test.MempoolData.UNKNOWN_ROYLLO_COIN_ANCHOR_1_TXID;
import static org.royllo.test.MempoolData.UNKNOWN_ROYLLO_COIN_ANCHOR_1_VOUT;
import static org.royllo.test.MempoolData.UNKNOWN_ROYLLO_COIN_GENESIS_TXID;
import static org.royllo.test.MempoolData.UNKNOWN_ROYLLO_COIN_GENESIS_VOUT;
import static org.royllo.test.TapdData.UNKNOWN_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.UNKNOWN_ROYLLO_COIN_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("unknownRoylloCoin test")
@ActiveProfiles({"scheduler-disabled"})
public class UnknownRoylloCoinIntegrationTest extends TestWithMockServers {

    @Autowired
    BitcoinTransactionOutputRepository bitcoinTransactionOutputRepository;

    @Autowired
    AssetGroupRepository assetGroupRepository;

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    AssetStateRepository assetStateRepository;

    @Autowired
    AssetGroupService assetGroupService;

    @Autowired
    AssetService assetService;

    @Autowired
    BitcoinService bitcoinService;

    @Autowired
    AssetStateService assetStateService;

    @Autowired
    ProofService proofService;

    @Autowired
    RequestService requestService;

    @Autowired
    AddProofBatch addProofBatch;

    @Test
    @DisplayName("Process proof")
    public void processProof() {
        // unknownRoylloCoin is not in our database - We add it (No asset group).
        final String UNKNOWN_ROYLLO_COIN_RAW_PROOF = UNKNOWN_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String UNKNOWN_ROYLLO_COIN_PROOF_ID = sha256(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        final String UNKNOWN_ROYLLO_COIN_STATE_ID = UNKNOWN_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // =============================================================================================================
        // We check that the asset doesn't already exist.
        assertFalse(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(UNKNOWN_ROYLLO_COIN_STATE_ID).isPresent());
        assertFalse(proofService.getProofByProofId(UNKNOWN_ROYLLO_COIN_PROOF_ID).isPresent());

        // =============================================================================================================
        // We count how many items we have before inserting.
        final long assetGroupCountBefore = assetGroupRepository.count();
        final long assetCountBefore = assetRepository.count();
        final long assetStateCountBefore = assetStateRepository.count();

        // =============================================================================================================
        // We add a request to add roylloNFT proof.
        AddProofRequestDTO addUnknownRoylloCoinRequest = requestService.createAddProofRequest(UNKNOWN_ROYLLO_COIN_RAW_PROOF);
        assertNotNull(addUnknownRoylloCoinRequest);
        assertEquals(OPENED, addUnknownRoylloCoinRequest.getStatus());

        // =============================================================================================================
        // We process the request and test its results
        addProofBatch.processRequests();
        final Optional<RequestDTO> unknownRoylloCoinRequestTreated = requestService.getRequest(addUnknownRoylloCoinRequest.getId());
        assertTrue(unknownRoylloCoinRequestTreated.isPresent());
        assertTrue(unknownRoylloCoinRequestTreated.get().isSuccessful());
        assertEquals(SUCCESS, unknownRoylloCoinRequestTreated.get().getStatus());
        assertNotNull(((AddProofRequestDTO) unknownRoylloCoinRequestTreated.get()).getAsset());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID, ((AddProofRequestDTO) unknownRoylloCoinRequestTreated.get()).getAsset().getAssetId());

        // =============================================================================================================
        // We check that the asset now exists.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(UNKNOWN_ROYLLO_COIN_GENESIS_TXID, UNKNOWN_ROYLLO_COIN_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(UNKNOWN_ROYLLO_COIN_ANCHOR_1_TXID, UNKNOWN_ROYLLO_COIN_ANCHOR_1_VOUT).isPresent());
        assertTrue(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(UNKNOWN_ROYLLO_COIN_STATE_ID).isPresent());
        assertTrue(proofService.getProofByProofId(UNKNOWN_ROYLLO_COIN_PROOF_ID).isPresent());

        // =============================================================================================================
        // We check that nothing more has been created.
        assertEquals(assetGroupCountBefore, assetGroupRepository.count());
        assertEquals(assetCountBefore + 1, assetRepository.count());
        assertEquals(assetStateCountBefore + 1, assetStateRepository.count());

        // =============================================================================================================
        // We check the value of what has been created.
        final Optional<AssetDTO> asset = assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID);
        assertTrue(asset.isPresent());
        assertNotNull(asset.get().getAssetIdAlias());
        verifyTransaction(bitcoinService.getBitcoinTransactionOutput(UNKNOWN_ROYLLO_COIN_GENESIS_TXID, UNKNOWN_ROYLLO_COIN_GENESIS_VOUT).get(),
                UNKNOWN_ROYLLO_COIN_GENESIS_TXID);
        verifyTransaction(bitcoinService.getBitcoinTransactionOutput(UNKNOWN_ROYLLO_COIN_ANCHOR_1_TXID, UNKNOWN_ROYLLO_COIN_ANCHOR_1_VOUT).get(),
                UNKNOWN_ROYLLO_COIN_ANCHOR_1_TXID);
        verifyAsset(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).get(), UNKNOWN_ROYLLO_COIN_ASSET_ID);
        verifyAssetState(assetStateService.getAssetStateByAssetStateId(UNKNOWN_ROYLLO_COIN_STATE_ID).get(),
                UNKNOWN_ROYLLO_COIN_ASSET_ID,
                UNKNOWN_ROYLLO_COIN_ANCHOR_1_TXID,
                UNKNOWN_ROYLLO_COIN_ANCHOR_1_VOUT,
                UNKNOWN_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(0).getAsset().getScriptKey());
    }

}
