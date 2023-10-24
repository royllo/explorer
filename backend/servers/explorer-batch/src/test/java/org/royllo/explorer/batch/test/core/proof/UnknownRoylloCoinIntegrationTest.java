package org.royllo.explorer.batch.test.core.proof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.batch.batch.request.AddProofBatch;
import org.royllo.explorer.core.service.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("unknownRoylloCoin test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles({"scheduler-disabled"})
public class UnknownRoylloCoinIntegrationTest {

    @Autowired
    RequestService requestService;

    @Autowired
    AddProofBatch addProofBatch;

    @Test
    @DisplayName("Process proof")
    public void processProof() {
        System.out.printf("toto");
        fail("Not yet implemented");
    }

}
