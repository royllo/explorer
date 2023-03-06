package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.provider.mempool.MempoolTransactionService;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;
import org.royllo.explorer.core.provider.tarod.TarodProofService;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.royllo.explorer.core.util.exceptions.proof.ProofCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"tarodProofServiceMock", "mempoolTransactionServiceMock"})
@DisplayName("ProofService tests")
public class ProofServiceTest extends BaseTest {

    @Autowired
    private MempoolTransactionService mempoolTransactionService;

    @Autowired
    private TarodProofService tarodProofService;

    @Autowired
    private ProofService proofService;

    @Autowired
    private AssetService assetService;

    @Test
    @DisplayName("addProof()")
    public void addProof() {
        // =============================================================================================================
        // Unknown Royllo coin.
        DecodedProofResponse unknownRoylloCoinDecodedProof = tarodProofService.decode(UNKNOWN_ROYLLO_COIN_RAW_PROOF, 0).block();
        assertNotNull(unknownRoylloCoinDecodedProof);

        // We add our proof but our an asset doesn't exist yet --> an error must occur.
        try {
            proofService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, 0, unknownRoylloCoinDecodedProof);
            fail("An exception should have occurred");
        } catch (ProofCreationException e) {
            assertEquals(e.getMessage(), "Asset " + UNKNOWN_ROYLLO_COIN_ASSET_ID + " is not registered in our database");
        }

        // We add the asset of our proof, and then, our proof --> No error, proof should be added.
        final AssetDTO unknownRoylloCoin = assetService.addAsset(ASSET_MAPPER.mapToAssetDTO(unknownRoylloCoinDecodedProof.getDecodedProof()));
        assertNotNull(unknownRoylloCoin);
        assertNotNull(unknownRoylloCoin.getId());
        assertNotNull(unknownRoylloCoin.getCreator());
        assertEquals(0, unknownRoylloCoin.getCreator().getId());
        assertEquals(0, unknownRoylloCoin.getVersion());


        // We add again our proof as it's already in our database --> an error must occur.


    }

}
