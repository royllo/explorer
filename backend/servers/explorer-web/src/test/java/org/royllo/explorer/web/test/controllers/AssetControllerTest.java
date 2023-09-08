package org.royllo.explorer.web.test.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.royllo.explorer.core.domain.asset.Asset;
import org.royllo.explorer.core.domain.proof.Proof;
import org.royllo.explorer.core.repository.asset.AssetRepository;
import org.royllo.explorer.core.repository.proof.ProofRepository;
import org.royllo.explorer.web.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PROOFS_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Asset controller tests")
@AutoConfigureMockMvc
@PropertySource("classpath:messages.properties")
public class AssetControllerTest extends BaseTest {

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    ProofRepository proofRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset page")
    void assetPage(final HttpHeaders headers) throws Exception {
        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID).headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking view proof link.
                .andExpect(content().string(containsString("/asset/" + ROYLLO_COIN_ASSET_ID + "/proofs")))
                // Checking each field.
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_NAME + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ASSET_ID + "<")))
                // Asset genesis.
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_GENESIS_POINT_TXID + ":" + ROYLLO_COIN_GENESIS_POINT_VOUT + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_NAME + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_META_DATA_HASH + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_OUTPUT_INDEX + "<")))
                // Other data.
                .andExpect(content().string(containsString(">Normal<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_AMOUNT + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_SCRIPT_KEY + "<")))
                // Group key.
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_RAW_GROUP_KEY + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_TWEAKED_GROUP_KEY + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ASSET_ID_SIG + "<")))
                // Anchor.
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ANCHOR_TX + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ANCHOR_BLOCK_HASH + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ANCHOR_OUTPOINT + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_INTERNAL_KEY + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_MERKLE_ROOT + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_TAPSCRIPT_SIBLING + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("asset.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));

        // Trim test with spaces
        mockMvc.perform(get("/asset/ " + ROYLLO_COIN_ASSET_ID + " ").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking view proof link.
                .andExpect(content().string(containsString("/asset/" + ROYLLO_COIN_ASSET_ID + "/proofs")))
                // Checking each field.
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_NAME + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ASSET_ID + "<")))
                // Asset.
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_GENESIS_POINT_TXID + ":" + ROYLLO_COIN_GENESIS_POINT_VOUT + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_NAME + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_META_DATA_HASH + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_OUTPUT_INDEX + "<")))
                // Other data.
                .andExpect(content().string(containsString(">Normal<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_AMOUNT + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_SCRIPT_KEY + "<")))
                // Asset group.
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_RAW_GROUP_KEY + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_TWEAKED_GROUP_KEY + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ASSET_ID_SIG + "<")))
                // Asset state.
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ANCHOR_TX + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ANCHOR_BLOCK_HASH + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_ANCHOR_OUTPOINT + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_INTERNAL_KEY + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_MERKLE_ROOT + "<")))
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_TAPSCRIPT_SIBLING + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("asset.view.error.noAssetId")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("asset.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset page without parameter")
    void assetPageWithoutParameter(final HttpHeaders headers) throws Exception {
        mockMvc.perform(get("/asset/").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("asset.view.error.noAssetId"))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("asset.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset page without parameter and slash")
    void assetPageWithoutParameterAndSlash(final HttpHeaders headers) throws Exception {
        mockMvc.perform(get("/asset").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("asset.view.error.noAssetId"))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("asset.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Invalid asset id")
    void invalidAssetId(final HttpHeaders headers) throws Exception {
        final String expectedMessage = Objects.requireNonNull(environment.getProperty("asset.view.error.assetNotFound")).replace("\"{0}\"", "&quot;NON_EXISTING&quot;");
        mockMvc.perform(get("/asset/NON_EXISTING").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset proofs page")
    void assetProofsPage(final HttpHeaders headers) throws Exception {
        // My royllo coin has only one proof.
        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID + "/proofs").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PROOFS_PAGE)))
                // Checking proofs.
                .andExpect(content().string(containsString(">" + ROYLLO_COIN_RAW_PROOF + "<")))
                .andExpect(content().string(not(containsString(">" + ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF + "<"))))
                .andExpect(content().string(not(containsString(">" + ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF + "<"))))
                .andExpect(content().string(not(containsString(">" + ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF + "<"))))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("proof.view.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("proof.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));

        // Active royllo coin has several proofs.
        mockMvc.perform(get("/asset/" + ACTIVE_ROYLLO_COIN_ASSET_ID + "/proofs/").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PROOFS_PAGE)))
                // Checking proofs.
                .andExpect(content().string(not(containsString(">" + ROYLLO_COIN_RAW_PROOF + "<"))))
                .andExpect(content().string(containsString(">" + ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF + "<")))
                .andExpect(content().string(containsString(">" + ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF + "<")))
                .andExpect(content().string(containsString(">" + ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF + "<")))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("proof.view.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("proof.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset proofs page with pagination")
    void assetProofsPagePagination(final HttpHeaders headers) throws Exception {
        // Creating enough proofs to test pagination.
        // We purge data as the same test will be run with different headers.
        proofRepository.findByAssetAssetIdOrderByCreatedOn(ROYLLO_COIN_ASSET_ID, Pageable.ofSize(101))
                .stream()
                .filter(proof -> !Objects.equals(proof.getRawProof(), ROYLLO_COIN_RAW_PROOF))
                .forEach(proofRepository::delete);
        final Optional<Asset> byAssetId = assetRepository.findByAssetId(ROYLLO_COIN_ASSET_ID);
        assertEquals(1, proofRepository.findByAssetAssetIdOrderByCreatedOn(ROYLLO_COIN_ASSET_ID, Pageable.ofSize(1)).getTotalElements());
        byAssetId.ifPresent(asset -> {
            for (int i = 0; i < 100; i++) {
                proofRepository.save(Proof.builder()
                        .proofId("proof-id-" + i)
                        .asset(asset)
                        .rawProof("raw-proof-" + i)
                        .creator(ANONYMOUS_USER)
                        .build());
            }
        });
        assertEquals(101, proofRepository.findByAssetAssetIdOrderByCreatedOn(ROYLLO_COIN_ASSET_ID, Pageable.ofSize(1)).getTotalElements());

        // Testing page 1 (without parameter).
        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID + "/proofs").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PROOFS_PAGE)))
                // Checking proofs.
                .andExpect(content().string(containsString(">raw-proof-0<")))
                .andExpect(content().string(not(containsString(">raw-proof-99<"))))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("proof.view.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("proof.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));

        // Testing page 1 (with parameter).
        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID + "/proofs?page=1").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PROOFS_PAGE)))
                // Checking proofs.
                .andExpect(content().string(containsString(">raw-proof-1<")))
                .andExpect(content().string(not(containsString(">raw-proof-99<"))))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("proof.view.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("proof.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));

        // Testing page 2 (with parameter).
        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID + "/proofs?page=2").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PROOFS_PAGE)))
                // Checking proofs.
                .andExpect(content().string(not(containsString(">raw-proof-1<"))))
                .andExpect(content().string(containsString(">raw-proof-99<")))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("proof.view.error.invalidPage")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("proof.view.error.assetNotFound"))
                        .replace("\"{0}\"", "&quot;" + ROYLLO_COIN_ASSET_ID + "&quot;")))));
    }

    @ParameterizedTest
    @MethodSource("headers")
    @DisplayName("Asset proofs page with invalid page number")
    void assetProofsPageWithInvalidPageNumber(final HttpHeaders headers) throws Exception {
        mockMvc.perform(get("/asset/" + ROYLLO_COIN_ASSET_ID + "/proofs?page=5").headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(containsString(ASSET_PROOFS_PAGE)))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("proof.view.error.invalidPage"))));
    }

}
