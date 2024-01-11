package org.royllo.explorer.batch.test.core.proof;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.core.dto.request.RequestDTO;
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

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.royllo.explorer.core.dto.proof.ProofDTO.PROOF_FILE_NAME_EXTENSION;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_HOST;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_PORT;
import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.ASSET_ALIAS_LENGTH;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_1_TXID;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_2_TXID;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_3_TXID;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_GENESIS_TXID;
import static org.royllo.test.MempoolData.SET_OF_ROYLLO_NFT_GENESIS_VOUT;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_FROM_TEST;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_FROM_TEST;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("setOfRoylloNFT test")
@ActiveProfiles({"scheduler-disabled"})
public class SetOfRoylloNFTIntegrationTest extends TestWithMockServers {

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
        // The setOfRoylloNFT (3 of them) are not in our database, we add them.

        // =============================================================================================================
        // We retrieve the data.
        final String SET_OF_ROYLLO_NFT_TWEAKED_GROUP_KEY = SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey();

        // NFT1.
        final String SET_OF_ROYLLO_NFT_1_RAW_PROOF = SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String SET_OF_ROYLLO_NFT_1_PROOF_ID = sha256(SET_OF_ROYLLO_NFT_1_RAW_PROOF);
        final String SET_OF_ROYLLO_NFT_1_ASSET_STATE_ID = SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // NFT2.
        final String SET_OF_ROYLLO_NFT_2_RAW_PROOF = SET_OF_ROYLLO_NFT_2_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String SET_OF_ROYLLO_NFT_2_PROOF_ID = sha256(SET_OF_ROYLLO_NFT_2_RAW_PROOF);
        final String SET_OF_ROYLLO_NFT_2_ASSET_STATE_ID = SET_OF_ROYLLO_NFT_2_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // NFT3.
        final String SET_OF_ROYLLO_NFT_3_RAW_PROOF = SET_OF_ROYLLO_NFT_3_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String SET_OF_ROYLLO_NFT_3_PROOF_ID = sha256(SET_OF_ROYLLO_NFT_3_RAW_PROOF);
        final String SET_OF_ROYLLO_NFT_3_ASSET_STATE_ID = SET_OF_ROYLLO_NFT_3_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // =============================================================================================================
        // We check that the asset doesn't already exist.
        assertFalse(assetGroupService.getAssetGroupByAssetGroupId(SET_OF_ROYLLO_NFT_TWEAKED_GROUP_KEY).isPresent());

        assertFalse(assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_1_ASSET_ID).isPresent());
        assertFalse(assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_2_ASSET_ID).isPresent());
        assertFalse(assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_3_ASSET_ID).isPresent());

        assertFalse(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_1_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_2_ASSET_STATE_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_3_ASSET_STATE_ID).isPresent());

        assertFalse(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_1_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_2_PROOF_ID).isPresent());
        assertFalse(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_3_PROOF_ID).isPresent());

        // =============================================================================================================
        // We count how many items we have before inserting.
        final long assetGroupCountBefore = assetGroupRepository.count();
        final long assetCountBefore = assetRepository.count();
        final long assetStateCountBefore = assetStateRepository.count();

        // =============================================================================================================
        // We add the three proofs.
        AddProofRequestDTO addRoylloNFT1Request = requestService.createAddProofRequest(SET_OF_ROYLLO_NFT_1_RAW_PROOF);
        assertNotNull(addRoylloNFT1Request);
        assertEquals(OPENED, addRoylloNFT1Request.getStatus());

        AddProofRequestDTO addRoylloNFT2Request = requestService.createAddProofRequest(SET_OF_ROYLLO_NFT_2_RAW_PROOF);
        assertNotNull(addRoylloNFT2Request);
        assertEquals(OPENED, addRoylloNFT1Request.getStatus());

        AddProofRequestDTO addRoylloNFT3Request = requestService.createAddProofRequest(SET_OF_ROYLLO_NFT_3_RAW_PROOF);
        assertNotNull(addRoylloNFT3Request);
        assertEquals(OPENED, addRoylloNFT3Request.getStatus());

        // =============================================================================================================
        // We process the request and test its results
        addProofBatch.processRequests();

        final Optional<RequestDTO> addRoylloNFT1RequestTreated = requestService.getRequest(addRoylloNFT1Request.getId());
        assertTrue(addRoylloNFT1RequestTreated.isPresent());
        assertTrue(addRoylloNFT1RequestTreated.get().isSuccessful());
        assertEquals(SUCCESS, addRoylloNFT1RequestTreated.get().getStatus());
        assertNotNull(((AddProofRequestDTO) addRoylloNFT1RequestTreated.get()).getAsset());
        assertEquals(SET_OF_ROYLLO_NFT_1_ASSET_ID, ((AddProofRequestDTO) addRoylloNFT1RequestTreated.get()).getAsset().getAssetId());

        final Optional<RequestDTO> addRoylloNFT2RequestTreated = requestService.getRequest(addRoylloNFT2Request.getId());
        assertTrue(addRoylloNFT2RequestTreated.isPresent());
        assertTrue(addRoylloNFT2RequestTreated.get().isSuccessful());
        assertEquals(SUCCESS, addRoylloNFT2RequestTreated.get().getStatus());
        assertNotNull(((AddProofRequestDTO) addRoylloNFT2RequestTreated.get()).getAsset());
        assertEquals(SET_OF_ROYLLO_NFT_2_ASSET_ID, ((AddProofRequestDTO) addRoylloNFT2RequestTreated.get()).getAsset().getAssetId());

        final Optional<RequestDTO> addRoylloNFT3RequestTreated = requestService.getRequest(addRoylloNFT3Request.getId());
        assertTrue(addRoylloNFT3RequestTreated.isPresent());
        assertTrue(addRoylloNFT3RequestTreated.get().isSuccessful());
        assertEquals(SUCCESS, addRoylloNFT3RequestTreated.get().getStatus());
        assertNotNull(((AddProofRequestDTO) addRoylloNFT3RequestTreated.get()).getAsset());
        assertEquals(SET_OF_ROYLLO_NFT_3_ASSET_ID, ((AddProofRequestDTO) addRoylloNFT3RequestTreated.get()).getAsset().getAssetId());

        // =============================================================================================================
        // We check that nothing more has been created.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_GENESIS_TXID, SET_OF_ROYLLO_NFT_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_1_TXID, SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_2_TXID, SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_3_TXID, SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT).isPresent());

        assertTrue(assetGroupService.getAssetGroupByAssetGroupId(SET_OF_ROYLLO_NFT_TWEAKED_GROUP_KEY).isPresent());

        assertTrue(assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_1_ASSET_ID).isPresent());
        assertTrue(assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_2_ASSET_ID).isPresent());
        assertTrue(assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_3_ASSET_ID).isPresent());

        assertTrue(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_1_ASSET_STATE_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_2_ASSET_STATE_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_3_ASSET_STATE_ID).isPresent());

        assertTrue(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_1_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_2_PROOF_ID).isPresent());
        assertTrue(proofService.getProofByProofId(SET_OF_ROYLLO_NFT_3_PROOF_ID).isPresent());

        // =============================================================================================================
        // We check what have been created.
        assertEquals(assetGroupCountBefore + 1, assetGroupRepository.count());
        assertEquals(assetCountBefore + 3, assetRepository.count());
        assertEquals(assetStateCountBefore + 3, assetStateRepository.count());

        // =============================================================================================================
        // We check the value of what has been created.
        final Optional<AssetDTO> asset1 = assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_1_ASSET_ID);
        assertTrue(asset1.isPresent());
        assertNotNull(asset1.get().getAssetIdAlias());
        assertEquals(ASSET_ALIAS_LENGTH, asset1.get().getAssetIdAlias().length());
        final Optional<AssetDTO> asset2 = assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_2_ASSET_ID);
        assertTrue(asset2.isPresent());
        assertNotNull(asset2.get().getAssetIdAlias());
        assertEquals(ASSET_ALIAS_LENGTH, asset2.get().getAssetIdAlias().length());
        final Optional<AssetDTO> asset3 = assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_3_ASSET_ID);
        assertTrue(asset3.isPresent());
        assertNotNull(asset3.get().getAssetIdAlias());
        assertEquals(ASSET_ALIAS_LENGTH, asset3.get().getAssetIdAlias().length());

        verifyTransaction(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_GENESIS_TXID, SET_OF_ROYLLO_NFT_GENESIS_VOUT).get(),
                SET_OF_ROYLLO_NFT_GENESIS_TXID);

        verifyAssetGroup(assetGroupService.getAssetGroupByAssetGroupId(SET_OF_ROYLLO_NFT_TWEAKED_GROUP_KEY).get(), SET_OF_ROYLLO_NFT_1_ASSET_ID);

        // NTF1.
        verifyTransaction(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_1_TXID, SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT).get(),
                SET_OF_ROYLLO_NFT_ANCHOR_1_TXID);
        verifyAsset(assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_1_ASSET_ID).get(), SET_OF_ROYLLO_NFT_1_ASSET_ID);
        verifyAssetState(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_1_ASSET_STATE_ID).get(),
                SET_OF_ROYLLO_NFT_1_ASSET_ID,
                SET_OF_ROYLLO_NFT_ANCHOR_1_TXID,
                SET_OF_ROYLLO_NFT_ANCHOR_1_VOUT,
                SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getScriptKey());

        // NTF2.
        verifyTransaction(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_2_TXID, SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT).get(),
                SET_OF_ROYLLO_NFT_ANCHOR_2_TXID);
        verifyAsset(assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_2_ASSET_ID).get(), SET_OF_ROYLLO_NFT_2_ASSET_ID);
        verifyAssetState(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_2_ASSET_STATE_ID).get(),
                SET_OF_ROYLLO_NFT_2_ASSET_ID,
                SET_OF_ROYLLO_NFT_ANCHOR_2_TXID,
                SET_OF_ROYLLO_NFT_ANCHOR_2_VOUT,
                SET_OF_ROYLLO_NFT_2_FROM_TEST.getDecodedProofResponse(0).getAsset().getScriptKey());

        // NTF1.
        verifyTransaction(bitcoinService.getBitcoinTransactionOutput(SET_OF_ROYLLO_NFT_ANCHOR_3_TXID, SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT).get(),
                SET_OF_ROYLLO_NFT_ANCHOR_3_TXID);
        verifyAsset(assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_3_ASSET_ID).get(), SET_OF_ROYLLO_NFT_3_ASSET_ID);
        verifyAssetState(assetStateService.getAssetStateByAssetStateId(SET_OF_ROYLLO_NFT_3_ASSET_STATE_ID).get(),
                SET_OF_ROYLLO_NFT_3_ASSET_ID,
                SET_OF_ROYLLO_NFT_ANCHOR_3_TXID,
                SET_OF_ROYLLO_NFT_ANCHOR_3_VOUT,
                SET_OF_ROYLLO_NFT_3_FROM_TEST.getDecodedProofResponse(0).getAsset().getScriptKey());

        assertNotNull(asset1.get().getIssuanceDate());
        assertNotNull(asset1.get().getAmount());
        assertNotNull(asset2.get().getIssuanceDate());
        assertNotNull(asset2.get().getAmount());
        assertNotNull(asset3.get().getIssuanceDate());
        assertNotNull(asset3.get().getAmount());

        // =============================================================================================================
        // We check meta-data file for our three assets
        var client = new OkHttpClient();

        // Asset 1.
        assertNotNull(asset1.get().getMetaDataFileName());
        Request request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + asset1.get().getMetaDataFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals("setOfRoylloNFT by Royllo", response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        // Asset 2.
        assertNotNull(asset2.get().getMetaDataFileName());
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + asset2.get().getMetaDataFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals("setOfRoylloNFT2 by Royllo", response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        // Asset 3.
        assertNotNull(asset3.get().getMetaDataFileName());
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + asset3.get().getMetaDataFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals("setOfRoylloNFT3 by Royllo", response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        // =============================================================================================================
        // We should have the proof file on our content service.
        final Optional<ProofDTO> proof1Created = proofService.getProofByProofId(SET_OF_ROYLLO_NFT_1_PROOF_ID);
        assertTrue(proof1Created.isPresent());
        assertEquals(SET_OF_ROYLLO_NFT_1_PROOF_ID + PROOF_FILE_NAME_EXTENSION, proof1Created.get().getProofFileName());
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + proof1Created.get().getProofFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals(SET_OF_ROYLLO_NFT_1_RAW_PROOF, response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        final Optional<ProofDTO> proof2Created = proofService.getProofByProofId(SET_OF_ROYLLO_NFT_2_PROOF_ID);
        assertTrue(proof2Created.isPresent());
        assertEquals(SET_OF_ROYLLO_NFT_2_PROOF_ID + PROOF_FILE_NAME_EXTENSION, proof2Created.get().getProofFileName());
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + proof2Created.get().getProofFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals(SET_OF_ROYLLO_NFT_2_RAW_PROOF, response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        final Optional<ProofDTO> proof3Created = proofService.getProofByProofId(SET_OF_ROYLLO_NFT_3_PROOF_ID);
        assertTrue(proof3Created.isPresent());
        assertEquals(SET_OF_ROYLLO_NFT_3_PROOF_ID + PROOF_FILE_NAME_EXTENSION, proof3Created.get().getProofFileName());
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + proof3Created.get().getProofFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals(SET_OF_ROYLLO_NFT_3_RAW_PROOF, response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }
    }

}
