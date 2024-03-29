package org.royllo.explorer.batch.test.core.proof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.asset.AssetStateRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.dto.proof.ProofDTO.PROOF_FILE_NAME_EXTENSION;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_GENESIS_VOUT;
import static org.royllo.test.MempoolData.TRICKY_ROYLLO_COIN_ANCHOR_1_TXID;
import static org.royllo.test.MempoolData.TRICKY_ROYLLO_COIN_ANCHOR_2_TXID;
import static org.royllo.test.MempoolData.TRICKY_ROYLLO_COIN_ANCHOR_3_TXID;
import static org.royllo.test.MempoolData.TRICKY_ROYLLO_COIN_GENESIS_TXID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_FROM_TEST;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DisplayName("trickyRoylloCoin test")
@ActiveProfiles({"scheduler-disabled"})
public class TrickyRoylloCoinIntegrationTest extends TestWithMockServers {

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
    @DisplayName("Process proofs")
    public void processProofs() {
        /*
            11 000 trickyCoins were created.
                50 trickyCoins were sent to another address.
               125 trickyCoins were sent to another address.

        Proof file 1/0, proof file 2/1 and proof file 3/2 are the same (Same ID) => 11 0000
        Proof file 2/1 is different.
        Proof file 3/1 is different.
        Proof file 3/2 is different.

        Proof file 1
            0 >	Asset id	    : e16029acc4d2cf0505857265442344efb5547f2047a3fc1c4822683f7c57820e
                Anchor outpoint : 632d0c3935fff230aa3718e268dce5517786d7976f7aa33efb615b408737b0ad:0
                Script key 	    : 023a8d9bc352eb3f5f69b798a941f06244b3633a8fd2cf82406132504ff08e23ff
                ID              : 4fa07a9188bab61f6582613679bb1f3c07a0bdded3f8e8ff15618d0b5c1d04fc
                Amount		    : 11000

        Proof file 2
            0 >	Asset id	    : e16029acc4d2cf0505857265442344efb5547f2047a3fc1c4822683f7c57820e
                Anchor outpoint : f2acf1235882a7683bad5baeb1b84c2f1dbf33f0fc4a7c85f2191aa8d49ce0d3:1
                Script key 	    : 026e6f1ce5a4cf58f63f1c6d2421aaf3d011e3811bab92122cffd67dada97ab610
                ID              : 86c1102c45b8e80872f7781963d5f96f3ec32436bc44595ed0687521adc7bbd1
                Amount		    : 50

            1 >	Asset id	    : e16029acc4d2cf0505857265442344efb5547f2047a3fc1c4822683f7c57820e
                Anchor outpoint : 632d0c3935fff230aa3718e268dce5517786d7976f7aa33efb615b408737b0ad:0
                Script key 	    : 023a8d9bc352eb3f5f69b798a941f06244b3633a8fd2cf82406132504ff08e23ff
                ID              : 4fa07a9188bab61f6582613679bb1f3c07a0bdded3f8e8ff15618d0b5c1d04fc
                Amount		    : 11000

        Proof file 3
            0 >	Asset id	    : e16029acc4d2cf0505857265442344efb5547f2047a3fc1c4822683f7c57820e
                Anchor outpoint : 2727229cc771efa552f9232a04b0cd8d16df6d83bd217523888feee6a8553ec8:1
                Script key 	    : 02629f225f4ef29972e703b1086796dbe47e881ae913904a1c2eec15df35361fb6
                ID              : a070016ca7f1380e4279402aa475e66a24267594ae4a8d4aff9fdb2703faac7a
                Amount		    : 125

            1 >	Asset id	    : e16029acc4d2cf0505857265442344efb5547f2047a3fc1c4822683f7c57820e
                Anchor outpoint : f2acf1235882a7683bad5baeb1b84c2f1dbf33f0fc4a7c85f2191aa8d49ce0d3:0
                Script key 	    : 022dcc33d4e29c5d322277b9404ced5c67790fe1e31e234fecb3d06239caf3eddb
                ID              : 96863fefa716fc81a50d34702370d342707449e1a613482f888fe11a136a72eb
                Amount		    : 10950

            2 >	Asset id	    : e16029acc4d2cf0505857265442344efb5547f2047a3fc1c4822683f7c57820e
                Anchor outpoint : 632d0c3935fff230aa3718e268dce5517786d7976f7aa33efb615b408737b0ad:0
                Script key      : 023a8d9bc352eb3f5f69b798a941f06244b3633a8fd2cf82406132504ff08e23ff
                ID       	    : 4fa07a9188bab61f6582613679bb1f3c07a0bdded3f8e8ff15618d0b5c1d04fc
                Amount		    : 11000
         */

        // Proof 1.
        final String TRICKY_ROYLLO_COIN_1_RAW_PROOF = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String TRICKY_ROYLLO_COIN_1_PROOF_ID = sha256(TRICKY_ROYLLO_COIN_1_RAW_PROOF);

        // Proof 2.
        final String TRICKY_ROYLLO_COIN_2_RAW_PROOF = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(1).getRawProof();
        final String TRICKY_ROYLLO_COIN_2_PROOF_ID = sha256(TRICKY_ROYLLO_COIN_2_RAW_PROOF);

        // Proof 3.
        final String TRICKY_ROYLLO_COIN_3_RAW_PROOF = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(3).getRawProof();
        final String TRICKY_ROYLLO_COIN_3_PROOF_ID = sha256(TRICKY_ROYLLO_COIN_3_RAW_PROOF);

        // Asset states.
        final String TRICKY_ROYLLO_COIN_1_ASSET_STATE_ID = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();
        final String TRICKY_ROYLLO_COIN_2_ASSET_STATE_ID = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(1).getAsset().getAssetStateId();
        final String TRICKY_ROYLLO_COIN_3_ASSET_STATE_ID = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(3).getAsset().getAssetStateId();
        final String TRICKY_ROYLLO_COIN_4_ASSET_STATE_ID = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(4).getAsset().getAssetStateId();

        // =============================================================================================================
        // We check that the asset doesn't already exist.
        assertFalse(assetService.getAssetByAssetIdOrAlias(TRICKY_ROYLLO_COIN_ASSET_ID).isPresent());

        assertFalse(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_1_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_2_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_3_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_4_ASSET_STATE_ID).isPresent());

        assertFalse(proofService.getProofByProofId(TRICKY_ROYLLO_COIN_1_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(TRICKY_ROYLLO_COIN_2_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(TRICKY_ROYLLO_COIN_3_PROOF_ID).isPresent());

        // =============================================================================================================
        // We count how many items we have before inserting.
        final long assetGroupCountBefore = assetGroupRepository.count();
        final long assetCountBefore = assetRepository.count();
        final long assetStateCountBefore = assetStateRepository.count();

        // =============================================================================================================
        // We add the three proofs.
        AddProofRequestDTO addTrickyCoinProof1Request = requestService.createAddProofRequest(TRICKY_ROYLLO_COIN_1_RAW_PROOF);
        assertNotNull(addTrickyCoinProof1Request);
        assertEquals(OPENED, addTrickyCoinProof1Request.getStatus());

        AddProofRequestDTO addTrickyCoinProof2Request = requestService.createAddProofRequest(TRICKY_ROYLLO_COIN_2_RAW_PROOF);
        assertNotNull(addTrickyCoinProof2Request);
        assertEquals(OPENED, addTrickyCoinProof2Request.getStatus());

        AddProofRequestDTO addTrickyCoinProof3Request = requestService.createAddProofRequest(TRICKY_ROYLLO_COIN_3_RAW_PROOF);
        assertNotNull(addTrickyCoinProof3Request);
        assertEquals(OPENED, addTrickyCoinProof3Request.getStatus());

        // =============================================================================================================
        // We process the request and test its results
        addProofBatch.processRequests();

        assertThat(requestService.getRequest(addTrickyCoinProof1Request.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    assertThat(((AddProofRequestDTO) request).getAsset())
                            .isNotNull()
                            .satisfies(asset -> {
                                assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, asset.getAssetId());
                            });
                });

        assertThat(requestService.getRequest(addTrickyCoinProof2Request.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    assertThat(((AddProofRequestDTO) request).getAsset())
                            .isNotNull()
                            .satisfies(asset -> {
                                assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, asset.getAssetId());
                            });
                });

        assertThat(requestService.getRequest(addTrickyCoinProof3Request.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    assertThat(((AddProofRequestDTO) request).getAsset())
                            .isNotNull()
                            .satisfies(asset -> {
                                assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, asset.getAssetId());
                            });
                });

        // =============================================================================================================
        // We check that nothing more has been created.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(TRICKY_ROYLLO_COIN_GENESIS_TXID, SET_OF_ROYLLO_NFT_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(TRICKY_ROYLLO_COIN_ANCHOR_1_TXID, SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(TRICKY_ROYLLO_COIN_ANCHOR_2_TXID, SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(TRICKY_ROYLLO_COIN_ANCHOR_3_TXID, SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT).isPresent());

        assertTrue(assetService.getAssetByAssetIdOrAlias(TRICKY_ROYLLO_COIN_ASSET_ID).isPresent());

        assertTrue(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_1_ASSET_STATE_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_2_ASSET_STATE_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_3_ASSET_STATE_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_4_ASSET_STATE_ID).isPresent());

        assertTrue(proofService.getProofByProofId(TRICKY_ROYLLO_COIN_1_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(TRICKY_ROYLLO_COIN_2_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(TRICKY_ROYLLO_COIN_3_PROOF_ID).isPresent());

        // =============================================================================================================
        // We check what has been created.
        assertThat(assetService.getAssetByAssetIdOrAlias(TRICKY_ROYLLO_COIN_ASSET_ID))
                .isPresent()
                .hasValueSatisfying(asset -> {
                    verifyAsset(asset, TRICKY_ROYLLO_COIN_ASSET_ID);
                    assertNotNull(asset.getAssetIdAlias());
                    assertNotNull(asset.getAmount());
                    assertNotNull(asset.getIssuanceDate());
                    // We check the states.
                    AssetStateDTO assetState1 = assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_1_ASSET_STATE_ID)
                            .orElseThrow(() -> new AssertionError("Asset state 1 not found"));
                    verifyAssetState(assetState1, TRICKY_ROYLLO_COIN_1_ASSET_STATE_ID);
                    AssetStateDTO assetState2 = assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_2_ASSET_STATE_ID)
                            .orElseThrow(() -> new AssertionError("Asset state 2 not found"));
                    verifyAssetState(assetState2, TRICKY_ROYLLO_COIN_2_ASSET_STATE_ID);
                    AssetStateDTO assetState3 = assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_3_ASSET_STATE_ID)
                            .orElseThrow(() -> new AssertionError("Asset state 3 not found"));
                    verifyAssetState(assetState3, TRICKY_ROYLLO_COIN_3_ASSET_STATE_ID);
                    // We now check the metadata file.
                    assertThat(getFileFromContentServer(asset.getMetaDataFileName()))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals("trickyRoylloCoin by Royllo", response.body().string());
                            });
                    // And now every proof.
                    assertThat(getFileFromContentServer(TRICKY_ROYLLO_COIN_1_PROOF_ID + PROOF_FILE_NAME_EXTENSION))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals(TRICKY_ROYLLO_COIN_1_RAW_PROOF, response.body().string());
                            });
                    assertThat(getFileFromContentServer(TRICKY_ROYLLO_COIN_2_PROOF_ID + PROOF_FILE_NAME_EXTENSION))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals(TRICKY_ROYLLO_COIN_2_RAW_PROOF, response.body().string());
                            });
                    assertThat(getFileFromContentServer(TRICKY_ROYLLO_COIN_3_PROOF_ID + PROOF_FILE_NAME_EXTENSION))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals(TRICKY_ROYLLO_COIN_3_RAW_PROOF, response.body().string());
                            });
                });

        // We check what has been created with counts.
        assertEquals(assetGroupCountBefore, assetGroupRepository.count());
        assertEquals(assetCountBefore + 1, assetRepository.count());
        assertEquals(assetStateCountBefore + 4, assetStateRepository.count());
    }

    @Test
    @DisplayName("Process proofs starting from the last one")
    public void processProofsStartingFromTheLastOne() {
        // Proof 3.
        final String TRICKY_ROYLLO_COIN_3_RAW_PROOF = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(3).getRawProof();
        final String TRICKY_ROYLLO_COIN_3_PROOF_ID = sha256(TRICKY_ROYLLO_COIN_3_RAW_PROOF);

        // Asset states.
        final String TRICKY_ROYLLO_COIN_1_ASSET_STATE_ID = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();
        final String TRICKY_ROYLLO_COIN_2_ASSET_STATE_ID = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(1).getAsset().getAssetStateId();
        final String TRICKY_ROYLLO_COIN_3_ASSET_STATE_ID = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(3).getAsset().getAssetStateId();
        final String TRICKY_ROYLLO_COIN_4_ASSET_STATE_ID = TRICKY_ROYLLO_COIN_FROM_TEST.getDecodedProofResponse(4).getAsset().getAssetStateId();

        // =============================================================================================================
        // We check that the asset doesn't already exist.
        assertFalse(assetService.getAssetByAssetIdOrAlias(TRICKY_ROYLLO_COIN_ASSET_ID).isPresent());

        assertFalse(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_1_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_2_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_3_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_4_ASSET_STATE_ID).isPresent());

        assertFalse(proofService.getProofByProofId(TRICKY_ROYLLO_COIN_3_PROOF_ID).isPresent());

        // =============================================================================================================
        // We count how many items we have before inserting.
        final long assetGroupCountBefore = assetGroupRepository.count();
        final long assetCountBefore = assetRepository.count();
        final long assetStateCountBefore = assetStateRepository.count();

        // =============================================================================================================
        // We add the latest proof.
        AddProofRequestDTO addTrickyCoinProof3Request = requestService.createAddProofRequest(TRICKY_ROYLLO_COIN_3_RAW_PROOF);
        assertNotNull(addTrickyCoinProof3Request);
        assertEquals(OPENED, addTrickyCoinProof3Request.getStatus());

        // =============================================================================================================
        // We process the request and test its results
        addProofBatch.processRequests();

        assertThat(requestService.getRequest(addTrickyCoinProof3Request.getId()))
                .isPresent()
                .get()
                .satisfies(request -> {
                    assertTrue(request.isSuccessful());
                    assertEquals(SUCCESS, request.getStatus());
                    assertThat(((AddProofRequestDTO) request).getAsset())
                            .isNotNull()
                            .satisfies(asset -> {
                                assertEquals(TRICKY_ROYLLO_COIN_ASSET_ID, asset.getAssetId());
                            });
                });

        // =============================================================================================================
        // We check that nothing more has been created.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(TRICKY_ROYLLO_COIN_GENESIS_TXID, SET_OF_ROYLLO_NFT_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(TRICKY_ROYLLO_COIN_ANCHOR_1_TXID, SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(TRICKY_ROYLLO_COIN_ANCHOR_2_TXID, SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(TRICKY_ROYLLO_COIN_ANCHOR_3_TXID, SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT).isPresent());

        assertTrue(assetService.getAssetByAssetIdOrAlias(TRICKY_ROYLLO_COIN_ASSET_ID).isPresent());

        assertTrue(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_1_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_2_ASSET_STATE_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_3_ASSET_STATE_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_4_ASSET_STATE_ID).isPresent());

        assertTrue(proofService.getProofByProofId(TRICKY_ROYLLO_COIN_3_PROOF_ID).isPresent());

        // =============================================================================================================
        // We check what has been created.
        assertEquals(assetGroupCountBefore, assetGroupRepository.count());
        assertEquals(assetCountBefore + 1, assetRepository.count());
        assertEquals(assetStateCountBefore + 3, assetStateRepository.count());

        // Verify asset.
        assertThat(assetService.getAssetByAssetIdOrAlias(TRICKY_ROYLLO_COIN_ASSET_ID))
                .isPresent()
                .hasValueSatisfying(asset -> {
                    verifyAsset(asset, TRICKY_ROYLLO_COIN_ASSET_ID);
                    assertNotNull(asset.getAssetIdAlias());
                    assertNotNull(asset.getAmount());
                    assertNotNull(asset.getIssuanceDate());
                    // We check the states.
                    AssetStateDTO assetState1 = assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_1_ASSET_STATE_ID)
                            .orElseThrow(() -> new AssertionError("Asset state 1 not found"));
                    verifyAssetState(assetState1, TRICKY_ROYLLO_COIN_1_ASSET_STATE_ID);
                    AssetStateDTO assetState3 = assetStateService.getAssetStateByAssetStateId(TRICKY_ROYLLO_COIN_3_ASSET_STATE_ID)
                            .orElseThrow(() -> new AssertionError("Asset state 3 not found"));
                    verifyAssetState(assetState3, TRICKY_ROYLLO_COIN_3_ASSET_STATE_ID);
                    // We now check the metadata file.
                    assertThat(getFileFromContentServer(asset.getMetaDataFileName()))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals("trickyRoylloCoin by Royllo", response.body().string());
                            });
                    // And now every proof.
                    assertThat(getFileFromContentServer(TRICKY_ROYLLO_COIN_3_PROOF_ID + PROOF_FILE_NAME_EXTENSION))
                            .isNotNull()
                            .satisfies(response -> {
                                assertEquals(200, response.code());
                                assertEquals(TRICKY_ROYLLO_COIN_3_RAW_PROOF, response.body().string());
                            });


                });
    }

}
