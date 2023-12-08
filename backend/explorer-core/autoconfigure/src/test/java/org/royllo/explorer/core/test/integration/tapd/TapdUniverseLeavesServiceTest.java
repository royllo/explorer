package org.royllo.explorer.core.test.integration.tapd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.provider.tapd.UniverseLeavesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_ISSUANCE;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_TRANSFER;

@SpringBootTest
@DisplayName("Lightning TAPD universe leaves service test")
public class TapdUniverseLeavesServiceTest {

    @Autowired
    private TapdService tapdService;

    @Test
    @DisplayName("getUniverseLeaves() on lightning TAPD")
    public void getUniverseLeavesTest() {
        // Asset ID on the mainnet with https://universe.lightning.finance
        // 37b884f6aeab1a820a37aba8a8099b2118aff6dcf5e5ed6836d3d0251c4ccdde
        //
        // curl https://universe.lightning.finance/v1/taproot-assets/universe/leaves/asset-id/37b884f6aeab1a820a37aba8a8099b2118aff6dcf5e5ed6836d3d0251c4ccdde?proof_type=PROOF_TYPE_ISSUANCE | jq
        // curl https://universe.lightning.finance/v1/taproot-assets/universe/leaves/asset-id/37b884f6aeab1a820a37aba8a8099b2118aff6dcf5e5ed6836d3d0251c4ccdde?proof_type=PROOF_TYPE_TRANSFER | jq

        final String assetId = "37b884f6aeab1a820a37aba8a8099b2118aff6dcf5e5ed6836d3d0251c4ccdde";
        final String server = "https://universe.lightning.finance";

        // Issuance proof.
        UniverseLeavesResponse insuranceResponse = tapdService.getUniverseLeaves(server, assetId, PROOF_TYPE_ISSUANCE).block();
        assertNotNull(insuranceResponse);
        assertEquals(1, insuranceResponse.getLeaves().size());
        assertNotNull(insuranceResponse.getLeaves().get(0));
        assertNotNull(insuranceResponse.getLeaves().get(0).getProof());

        // transfer proof.
        UniverseLeavesResponse transferResponse = tapdService.getUniverseLeaves(server, assetId, PROOF_TYPE_TRANSFER).block();
        assertNotNull(transferResponse);
        assertEquals(1, transferResponse.getLeaves().size());
        assertNotNull(transferResponse.getLeaves().get(0));
        assertNotNull(transferResponse.getLeaves().get(0).getProof());

        // Checking it's not the same data.
        assertNotEquals(insuranceResponse.getLeaves().get(0).getProof(), transferResponse.getLeaves().get(0).getProof());
    }

    @Test
    @DisplayName("getUniverseLeaves() with wrong value on lightning TAPD")
    public void getUniverseLeavesWithWrongValueTest() {
        UniverseLeavesResponse response = tapdService.getUniverseLeaves("https://testnet.universe.lightning.finance",
                "NON_EXISTING_ASSET_ID", PROOF_TYPE_ISSUANCE).block();

        // Testing the response (error).
        assertNotNull(response);
        assertEquals(2, response.getErrorCode());
        assertEquals("encoding/hex: invalid byte: U+004E 'N'", response.getErrorMessage());
    }

}
