package org.royllo.explorer.web.test.controllers.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.request.ClaimAssetOwnershipRequestDTO;
import org.royllo.explorer.core.repository.request.RequestRepository;
import org.royllo.explorer.core.service.request.RequestService;
import org.royllo.explorer.web.test.util.BaseWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.FORM_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.RequestPageConstants.CLAIM_ASSET_OWNERSHIP_REQUEST_FORM_PAGE;
import static org.royllo.explorer.web.util.constants.RequestPageConstants.CLAIM_ASSET_OWNERSHIP_REQUEST_SUCCESS_PAGE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DirtiesContext
@DisplayName("Claim asset ownership request controller tests")
@AutoConfigureMockMvc
public class ClaimAssetOwnershipRequestControllerTest extends BaseWebTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Autowired
    MessageSource messages;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    RequestService requestService;

    @Test
    @WithMockUser(username = "22222222-2222-2222-2222-222222222222")
    @DisplayName("Claim asset ownership request form test")
    void claimAssetOwnershipRequestFormTest() throws Exception {

        mockMvc.perform(get("/account/request/claim_asset_ownership/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(CLAIM_ASSET_OWNERSHIP_REQUEST_FORM_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                .andExpect(content().string(containsString(getMessage(messages, "request.view.claimAssetOwnership.title"))));

    }

    @Test
    @DisplayName("Account settings saved")
    @WithMockUser(username = "22222222-2222-2222-2222-222222222222")
    void claimAssetOwnershipRequestSaved() throws Exception {

        final long requestCount = requestRepository.count();

        mockMvc.perform(post("/account/request/claim_asset_ownership/add")
                        .param("proofWithWitness", "MyProofWithWitness")
                        .contentType(APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(CLAIM_ASSET_OWNERSHIP_REQUEST_SUCCESS_PAGE))
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(containsString(getMessage(messages, "request.message.creationExplanation"))));

        // We check that the request was created.
        assertEquals(requestCount + 1, requestRepository.count());
        var requestCrated = requestService.getOpenedRequests().stream()
                .filter(requestDTO -> requestDTO instanceof ClaimAssetOwnershipRequestDTO)
                .map(requestDTO -> (ClaimAssetOwnershipRequestDTO) requestDTO)
                .filter(requestDTO -> "MyProofWithWitness".equals(requestDTO.getProofWithWitness()))
                .findFirst();
        assertTrue(requestCrated.isPresent());

    }

}
