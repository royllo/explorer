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
import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.ASSET_ID_ALIAS_LENGTH;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;
import static org.royllo.test.MempoolData.ROYLLO_NFT_ANCHOR_1_TXID;
import static org.royllo.test.MempoolData.ROYLLO_NFT_ANCHOR_1_VOUT;
import static org.royllo.test.MempoolData.ROYLLO_NFT_GENESIS_TXID;
import static org.royllo.test.MempoolData.ROYLLO_NFT_GENESIS_VOUT;
import static org.royllo.test.TapdData.ROYLLO_NFT_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_NFT_FROM_TEST;

@SpringBootTest
@DirtiesContext
@DisplayName("roylloNFT integration test")
@ActiveProfiles({"scheduler-disabled"})
public class RoylloNFTIntegrationTest extends TestWithMockServers {

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
        // roylloNFT is not in our database - We add it (No asset group).
        final String ROYLLO_NFT_RAW_PROOF = ROYLLO_NFT_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String ROYLLO_NFT_PROOF_ID = sha256(ROYLLO_NFT_RAW_PROOF);
        final String ROYLLO_NFT_ASSET_STATE_ID = ROYLLO_NFT_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetStateId();

        // =============================================================================================================
        // We check that the asset doesn't already exist.
        assertFalse(assetService.getAssetByAssetIdOrAlias(ROYLLO_NFT_ASSET_ID).isPresent());
        assertFalse(assetStateService.getAssetStateByAssetStateId(ROYLLO_NFT_ASSET_STATE_ID).isPresent());
        assertFalse(proofService.getProofByProofId(ROYLLO_NFT_PROOF_ID).isPresent());

        // =============================================================================================================
        // We count how many items we have before inserting.
        final long bitcoinTransactionOutputCountBefore = bitcoinTransactionOutputRepository.count();
        final long assetGroupCountBefore = assetGroupRepository.count();
        final long assetCountBefore = assetRepository.count();
        final long assetStateCountBefore = assetStateRepository.count();

        // =============================================================================================================
        // We add a request to add roylloNFT proof.
        AddProofRequestDTO addRoylloNFTRequest = requestService.createAddProofRequest(ROYLLO_NFT_RAW_PROOF);
        assertNotNull(addRoylloNFTRequest);
        assertEquals(OPENED, addRoylloNFTRequest.getStatus());

        // =============================================================================================================
        // We process the request and test its results
        addProofBatch.processRequests();
        final Optional<RequestDTO> roylloNFTRequestTreated = requestService.getRequest(addRoylloNFTRequest.getId());
        assertTrue(roylloNFTRequestTreated.isPresent());
        assertTrue(roylloNFTRequestTreated.get().isSuccessful());
        assertEquals(SUCCESS, roylloNFTRequestTreated.get().getStatus());
        assertNotNull(((AddProofRequestDTO) roylloNFTRequestTreated.get()).getAsset());
        assertEquals(ROYLLO_NFT_ASSET_ID, ((AddProofRequestDTO) roylloNFTRequestTreated.get()).getAsset().getAssetId());

        // =============================================================================================================
        // We check that the asset now exists.
        assertTrue(bitcoinService.getBitcoinTransactionOutput(ROYLLO_NFT_GENESIS_TXID, ROYLLO_NFT_GENESIS_VOUT).isPresent());
        assertTrue(bitcoinService.getBitcoinTransactionOutput(ROYLLO_NFT_ANCHOR_1_TXID, ROYLLO_NFT_ANCHOR_1_VOUT).isPresent());
        assertTrue(assetService.getAssetByAssetIdOrAlias(ROYLLO_NFT_ASSET_ID).isPresent());
        assertTrue(assetStateService.getAssetStateByAssetStateId(ROYLLO_NFT_ASSET_STATE_ID).isPresent());
        assertTrue(proofService.getProofByProofId(ROYLLO_NFT_PROOF_ID).isPresent());

        // =============================================================================================================
        // We check that nothing more has been created.
        assertEquals(bitcoinTransactionOutputCountBefore + 2, bitcoinTransactionOutputRepository.count());
        assertEquals(assetGroupCountBefore, assetGroupRepository.count());
        assertEquals(assetCountBefore + 1, assetRepository.count());
        assertEquals(assetStateCountBefore + 1, assetStateRepository.count());

        // =============================================================================================================
        // We check the value of what has been created.
        final Optional<AssetDTO> asset = assetService.getAssetByAssetIdOrAlias(ROYLLO_NFT_ASSET_ID);
        assertTrue(asset.isPresent());
        assertNotNull(asset.get().getAssetIdAlias());
        assertEquals(ASSET_ID_ALIAS_LENGTH, asset.get().getAssetIdAlias().length());
        assertNotNull(asset.get().getAmount());
        assertNotNull(asset.get().getIssuanceDate());
        verifyTransaction(bitcoinService.getBitcoinTransactionOutput(ROYLLO_NFT_GENESIS_TXID, ROYLLO_NFT_GENESIS_VOUT).get(),
                ROYLLO_NFT_GENESIS_TXID);
        verifyTransaction(bitcoinService.getBitcoinTransactionOutput(ROYLLO_NFT_ANCHOR_1_TXID, ROYLLO_NFT_ANCHOR_1_VOUT).get(),
                ROYLLO_NFT_ANCHOR_1_TXID);
        verifyAsset(assetService.getAssetByAssetIdOrAlias(ROYLLO_NFT_ASSET_ID).get(), ROYLLO_NFT_ASSET_ID);
        verifyAssetState(assetStateService.getAssetStateByAssetStateId(ROYLLO_NFT_ASSET_STATE_ID).get(),
                ROYLLO_NFT_ASSET_ID,
                ROYLLO_NFT_ANCHOR_1_TXID,
                ROYLLO_NFT_ANCHOR_1_VOUT,
                ROYLLO_NFT_FROM_TEST.getDecodedProofResponse(0).getAsset().getScriptKey());

        // =============================================================================================================
        // We check meta-data file for our three assets
        var client = new OkHttpClient();

        // Asset 1.
        assertNotNull(asset.get().getMetaDataFileName());
        Request request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + asset.get().getMetaDataFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals("roylloNFT by Royllo", response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        // We should have the proof file on our content service.
        final Optional<ProofDTO> proofCreated = proofService.getProofByProofId(ROYLLO_NFT_PROOF_ID);
        assertTrue(proofCreated.isPresent());
        assertEquals(ROYLLO_NFT_PROOF_ID + PROOF_FILE_NAME_EXTENSION, proofCreated.get().getProofFileName());
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + proofCreated.get().getProofFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
            assertEquals(ROYLLO_NFT_RAW_PROOF, response.body().string());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }
    }

}
