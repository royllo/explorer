package org.royllo.explorer.web.test.controllers.asset;

import lombok.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.core.domain.proof.Proof;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.repository.proof.ProofRepository;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static java.math.BigInteger.ONE;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_DTO;
import static org.royllo.explorer.core.util.enums.AssetType.NORMAL;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_GROUP_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_PROOFS_PAGE;
import static org.royllo.explorer.web.util.constants.AssetPageConstants.ASSET_STATES_PAGE;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_COIN_FROM_TEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Asset controller tests")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AssetControllerTest extends BaseTest {

    /** Asset state id field separator. */
    private static final String FIELD_SEPARATOR = "_";

    @Autowired
    ProofRepository proofRepository;

    @Autowired
    BitcoinService bitcoinService;

    @Autowired
    AssetGroupService assetGroupService;

    @Autowired
    AssetService assetService;

    @Autowired
    AssetStateService assetStateService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messages;

    @Test
    @DisplayName("Asset page without parameter")
    void assetPageWithoutParameter() throws Exception {

        mockMvc.perform(get("/asset/"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(getMessage(messages, "asset.view.error.noAssetId"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @Test
    @DisplayName("Asset page without parameter and slash")
    void assetPageWithoutParameterAndSlash() throws Exception {

        mockMvc.perform(get("/asset"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(getMessage(messages, "asset.view.error.noAssetId"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @Test
    @DisplayName("Invalid asset id")
    void invalidAssetId() throws Exception {

        mockMvc.perform(get("/asset/NON_EXISTING_ASSET_ID"))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(containsString(getMessage(messages, "asset.view.error.assetNotFound"))));

    }

    @Test
    @DisplayName("Download proof")
    void downloadProof() throws Exception {

        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID + "/proof/" + ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getProofId()))
                .andExpect(status().isOk());

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset group page pagination")
    void assetGroupPageWithPagination(final HttpHeaders headers) throws Exception {
        createFakeAssets();

        // Page 1.
        mockMvc.perform(get("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000001/group").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_GROUP_PAGE)))
                // Checking group tab data.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup")))))
                .andExpect(content().string(containsString(getMessage(messages, "asset.data.tweakedGroupKey") + ": FAKE_TWEAKED_GROUP_KEY")))
                // Checking pagination.
                .andExpect(content().string(not(containsString("previousPage"))))
                .andExpect(content().string(containsString(">1/10</")))
                .andExpect(content().string(containsString("nextPage")))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

        // Page 10.
        mockMvc.perform(get("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000001/group?page=10").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_GROUP_PAGE)))
                // Checking group tab data.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup")))))
                .andExpect(content().string(containsString(getMessage(messages, "asset.data.tweakedGroupKey") + ": FAKE_TWEAKED_GROUP_KEY")))
                // Checking pagination.
                .andExpect(content().string(containsString("previousPage")))
                .andExpect(content().string(containsString(">10/10</")))
                .andExpect(content().string(not(containsString("nextPage"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset states page pagination")
    void assetStatesPageWithPagination(final HttpHeaders headers) throws Exception {
        createFakeAssets();

        // Page 1.
        mockMvc.perform(get("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000001/states").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_STATES_PAGE)))
                // Checking pagination.
                .andExpect(content().string(not(containsString("previousPage"))))
                .andExpect(content().string(containsString(">1/5</")))
                .andExpect(content().string(containsString("nextPage")))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

        // Page 5.
        mockMvc.perform(get("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000001/states?page=5").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_STATES_PAGE)))
                // Checking pagination.
                .andExpect(content().string(containsString("previousPage")))
                .andExpect(content().string(containsString(">5/5</")))
                .andExpect(content().string(not(containsString("nextPage"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset proofs page pagination")
    void assetProofsPageWithPagination(final HttpHeaders headers) throws Exception {
        createFakeAssets();

        // Page 1.
        mockMvc.perform(get("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000001/proofs").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PROOFS_PAGE)))
                // Checking pagination.
                .andExpect(content().string(not(containsString("previousPage"))))
                .andExpect(content().string(containsString(">1/3</")))
                .andExpect(content().string(containsString("nextPage")))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

        // Page 5.
        mockMvc.perform(get("/asset/FAKE_ASSET_ID_00000000000000000000000000000000000000000000000001/proofs?page=3").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PROOFS_PAGE)))
                // Checking pagination.
                .andExpect(content().string(containsString("previousPage")))
                .andExpect(content().string(containsString(">3/3</")))
                .andExpect(content().string(not(containsString("nextPage"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.tabs.group.noAssetGroup")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "asset.view.error.assetNotFound")))));

    }

    /**
     * Create fake assets.
     */
    private void createFakeAssets() {
        // Transaction used.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 4);
        assertTrue(bto.isPresent());

        // Create a fake asset group.
        final AssetGroupDTO assetGroupDTO = assetGroupService.getAssetGroupByAssetGroupId("FAKE_ASSET_GROUP_ID")
                .orElseGet(() -> assetGroupService.addAssetGroup(
                        AssetGroupDTO.builder()
                                .assetGroupId("FAKE_ASSET_GROUP_ID")
                                .tweakedGroupKey("FAKE_TWEAKED_GROUP_KEY")
                                .creator(ANONYMOUS_USER_DTO)
                                .build()
                ));

        // Create fake assets.
        for (int i = 1; i <= 99; i++) {
            final Optional<AssetDTO> assetFound = assetService.getAssetByAssetId("FAKE_ASSET_ID_000000000000000000000000000000000000000000000000" + String.format("%02d", i));
            if (assetFound.isEmpty()) {
                assetService.addAsset(
                        AssetDTO.builder()
                                .creator(ANONYMOUS_USER_DTO)
                                .assetGroup(assetGroupDTO)
                                .assetId("FAKE_ASSET_ID_000000000000000000000000000000000000000000000000" + String.format("%02d", i))
                                .genesisPoint(bto.get())
                                .metaDataHash("metadata")
                                .name("FAKE_ASSET_NAME_000000000000000000000000000000000000000000000000" + String.format("%02d", i))
                                .outputIndex(0)
                                .version(0)
                                .type(NORMAL)
                                .amount(ONE)
                                .build()
                );
            }
        }

        // Create fake asset states.
        final Optional<AssetDTO> assetForAssetTest = assetService.getAssetByAssetId("FAKE_ASSET_ID_00000000000000000000000000000000000000000000000001");
        if (assetForAssetTest.isPresent()) {
            for (int i = 1; i <= 50; i++) {
                final AssetStateDTO assetToCreate = AssetStateDTO.builder()
                        .creator(ANONYMOUS_USER_DTO)
                        .asset(assetForAssetTest.get())
                        .anchorBlockHash("TEST_ANCHOR_BLOCK_HASH_" + String.format("%02d", i))
                        .anchorOutpoint(bto.get())
                        .anchorTx("TEST_ANCHOR_TX")
                        .internalKey("TEST_INTERNAL_KEY")
                        .merkleRoot("TEST_MERKLE_ROOT")
                        .tapscriptSibling("TEST_TAPSCRIPT_SIBLING")
                        .scriptVersion(0)
                        .scriptKey("FAKE_ASSET_STATE_ID_" + String.format("%02d", i))
                        .build();
                if (assetStateService.getAssetStateByAssetStateId(calculateAssetStateId(assetToCreate)).isEmpty()) {
                    assetStateService.addAssetState(assetToCreate);
                }
            }
        } else {
            fail("Asset FAKE_ASSET_ID_01 not found");
        }

        // Create fake proofs.
        for (int i = 1; i <= 30; i++) {
            final String proof = "TEST_ANCHOR_BLOCK_HASH_" + String.format("%02d", i);
            final Optional<Proof> proofFound = proofRepository.findByProofId(sha256(proof));
            if (proofFound.isEmpty()) {
                // Proof doesn't exist, we create the proof.
                proofRepository.save(Proof.builder()
                        .proofId(sha256(proof))
                        .creator(ANONYMOUS_USER)
                        .asset(ASSET_MAPPER.mapToAsset(assetForAssetTest.get()))
                        .proof(proof)
                        .build());
            }
        }

    }

    private String calculateAssetStateId(@NonNull final AssetStateDTO assetState) {
        // If we are in an asset state creation, asset state id is null, so we calculate it.
        // We calculate the asset state id here.
        String uniqueValue = assetState.getAsset().getAssetId()
                + FIELD_SEPARATOR + assetState.getAnchorOutpoint().getTxId() + ":" + assetState.getAnchorOutpoint().getVout()
                + FIELD_SEPARATOR + assetState.getScriptKey();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(uniqueValue.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available: " + e.getMessage());
        }
    }

}
