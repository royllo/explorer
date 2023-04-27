package org.royllo.explorer.web.test.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Test
    @DisplayName("Calling '/asset'")
    void assetPageWithoutParameterAndSlash() throws Exception {
        mockMvc.perform(get("/asset"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("asset.view.error.noAssetId"))));
    }

    @Test
    @DisplayName("Calling '/asset/'")
    void assetPageWithoutParameter() throws Exception {
        mockMvc.perform(get("/asset/"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("asset.view.error.noAssetId"))));
    }

    @Test
    @DisplayName("Invalid asset id")
    void invalidAssetId() throws Exception {
        final String expectedMessage = Objects.requireNonNull(environment.getProperty("asset.view.error.assetNotFound")).replace("\"{0}\"", "&quot;NON_EXISTING&quot;");
        mockMvc.perform(get("/asset/NON_EXISTING"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(expectedMessage)));
    }

    @Test
    @DisplayName("Asset display")
    void assetDisplay() throws Exception {
        mockMvc.perform(get("/asset/" + MY_ROYLLO_COIN_ASSET_ID))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PAGE))
                // Checking view proof link.
                .andExpect(content().string(containsString("/asset/" + MY_ROYLLO_COIN_ASSET_ID + "/proofs")))
                // Checking each field.
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_NAME + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ASSET_ID + "<")))
                // Asset genesis.
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_GENESIS_POINT_TXID + ":" + MY_ROYLLO_COIN_GENESIS_POINT_VOUT + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_NAME + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_META + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_OUTPUT_INDEX + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_GENESIS_BOOTSTRAP_INFORMATION + "<")))
                // Other data.
                .andExpect(content().string(containsString(">Normal<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_AMOUNT + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_SCRIPT_KEY + "<")))
                // Anchor.
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_TX + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_TX_ID + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_BLOCK_HASH + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_OUTPOINT + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHRO_INTERNAL_KEY + "<")));

        // Trim test with spaces
        mockMvc.perform(get("/asset/ " + MY_ROYLLO_COIN_ASSET_ID + " "))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PAGE))
                // Checking view proof link.
                .andExpect(content().string(containsString("/asset/" + MY_ROYLLO_COIN_ASSET_ID + "/proofs")))
                // Checking each field.
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_NAME + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ASSET_ID + "<")))
                // Asset genesis.
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_GENESIS_POINT_TXID + ":" + MY_ROYLLO_COIN_GENESIS_POINT_VOUT + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_NAME + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_META + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ASSET_ID + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_OUTPUT_INDEX + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_GENESIS_BOOTSTRAP_INFORMATION + "<")))
                // Other data.
                .andExpect(content().string(containsString(">Normal<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_AMOUNT + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_SCRIPT_KEY + "<")))
                // Anchor.
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_TX + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_TX_ID + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_BLOCK_HASH + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHOR_OUTPOINT + "<")))
                .andExpect(content().string(containsString(">" + MY_ROYLLO_COIN_ANCHRO_INTERNAL_KEY + "<")));
    }

    @Test
    @DisplayName("Calling '/asset/{assetId}/proofs'")
    void assetProofsPage() throws Exception {
        // My royllo coin has only one proof.
        mockMvc.perform(get("/asset/" + MY_ROYLLO_COIN_ASSET_ID + "/proofs"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PROOFS_PAGE))
                // Checking proofs.
                .andExpect(content().string(containsString("value=\"" + MY_ROYLLO_COIN_RAW_PROOF + "\"")))
                .andExpect(content().string(not(containsString("value=\"" + ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF + "\""))))
                .andExpect(content().string(not(containsString("value=\"" + ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF + "\""))))
                .andExpect(content().string(not(containsString("value=\"" + ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF + "\""))));

        // Active royllo coin has several proofs.
        mockMvc.perform(get("/asset/" + ACTIVE_ROYLLO_COIN_ASSET_ID + "/proofs/"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PROOFS_PAGE))
                // Checking proofs.
                .andExpect(content().string(not(containsString("value=\"" + MY_ROYLLO_COIN_RAW_PROOF + "\""))))
                .andExpect(content().string(containsString("value=\"" + ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF + "\"")))
                .andExpect(content().string(containsString("value=\"" + ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF + "\"")))
                .andExpect(content().string(containsString("value=\"" + ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF + "\"")));
    }

    @Test
    @DisplayName("Calling '/asset/{assetId}/proofs?page'")
    void assetProofsPagePagination() throws Exception {
        // Creating enough proofs to test pagination.
        final Optional<Asset> byAssetId = assetRepository.findByAssetId(MY_ROYLLO_COIN_ASSET_ID);
        assertEquals(1, proofRepository.findByAssetAssetIdOrderByCreatedOn(MY_ROYLLO_COIN_ASSET_ID, Pageable.ofSize(1)).getTotalElements());
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
        assertEquals(101, proofRepository.findByAssetAssetIdOrderByCreatedOn(MY_ROYLLO_COIN_ASSET_ID, Pageable.ofSize(1)).getTotalElements());

        // Testing page 1 (without parameter).
        mockMvc.perform(get("/asset/" + MY_ROYLLO_COIN_ASSET_ID + "/proofs"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PROOFS_PAGE))
                // Checking proofs.
                .andExpect(content().string(containsString("value=\"raw-proof-0\"")))
                .andExpect(content().string(not(containsString("value=\"raw-proof-99\""))));

        // Testing page 1 (with parameter).
        mockMvc.perform(get("/asset/" + MY_ROYLLO_COIN_ASSET_ID + "/proofs?page=1"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PROOFS_PAGE))
                // Checking proofs.
                .andExpect(content().string(containsString("value=\"raw-proof-1\"")))
                .andExpect(content().string(not(containsString("value=\"raw-proof-99\""))));

        // Testing page 2 (with parameter).
        mockMvc.perform(get("/asset/" + MY_ROYLLO_COIN_ASSET_ID + "/proofs?page=2"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PROOFS_PAGE))
                // Checking proofs.
                .andExpect(content().string(not(containsString("value=\"raw-proof-1\""))))
                .andExpect(content().string(containsString("value=\"raw-proof-99\"")));
    }

    @Test
    @DisplayName("Asset proofs page with invalid page number")
    void assetProofsPageWithInvalidPageNumber() throws Exception {
        mockMvc.perform(get("/asset/" + MY_ROYLLO_COIN_ASSET_ID + "/proofs?page=5"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSET_PROOFS_PAGE))
                // Checking error message.
                .andExpect(content().string(containsString(environment.getProperty("proof.view.error.invalidPage"))));
    }

}
