package org.royllo.explorer.web.test.controllers.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.royllo.explorer.web.test.util.BaseWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.hasProperty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.FORM_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.RequestPageConstants.ADD_PROOF_REQUEST_FORM_PAGE;
import static org.royllo.explorer.web.util.constants.RequestPageConstants.ADD_PROOF_REQUEST_SUCCESS_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DirtiesContext
@DisplayName("Add proof request controller tests")
@AutoConfigureMockMvc
public class AddProofRequestRequestControllerTest extends BaseWebTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Autowired
    MessageSource messages;

    @Test
    @DisplayName("Add proof request form test")
    void addProofRequestFormTest() throws Exception {

        mockMvc.perform(get("/request/proof/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_PROOF_REQUEST_FORM_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "NotBlank.command.proof")))));

    }

    @Test
    @DisplayName("Add proof request form post test with error")
    void addProofRequestFormPostTestWithError() throws Exception {

        // Empty form - proof not set.
        mockMvc.perform(post("/request/proof/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_PROOF_REQUEST_FORM_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeErrorCount(FORM_ATTRIBUTE, 1))
                .andExpect(content().string(containsString(getMessage(messages, "NotBlank.command.proof"))));

    }

    @Test
    @DisplayName("Add proof request form post test")
    void addProofRequestFormPostTest() throws Exception {

        // Test if everything is ok if we pass correct information to create a request.
        AtomicReference<AddProofRequestDTO> proof = new AtomicReference<>();
        mockMvc.perform(post("/request/proof/add")
                        .param("proof", "simple proof")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_PROOF_REQUEST_SUCCESS_PAGE))
                .andExpect(flash().attribute(FORM_ATTRIBUTE, hasProperty("proof", equalTo("simple proof").toString())))
                .andExpect(model().attributeExists(RESULT_ATTRIBUTE))
                .andExpect(model().hasNoErrors())
                .andDo(result -> proof.set((AddProofRequestDTO) Objects.requireNonNull(result.getModelAndView()).getModelMap().get(RESULT_ATTRIBUTE)))
                // Check page content.
                .andExpect(content().string(containsString(proof.get().getRequestId())))
                .andExpect(content().string(containsString(getMessage(messages, "request.message.creationExplanation"))))
                .andExpect(content().string(containsString(getMessage(messages, "request.view.status"))))
                .andExpect(content().string(containsString("/request/" + proof.get().getRequestId())))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "NotBlank.command.proof")))));

        // We test the request created (we get it from the model).
        assertNotNull(proof.get());
        assertNotNull(proof.get().getId());
        assertNotNull(proof.get().getRequestId());
        assertEquals(ANONYMOUS_ID, proof.get().getCreator().getId());
        assertEquals(ANONYMOUS_USER_USERNAME, proof.get().getCreator().getUsername());
        assertEquals(OPENED, proof.get().getStatus());
        assertNull(proof.get().getAsset());
        assertNull(proof.get().getErrorMessage());
        assertEquals("simple proof", proof.get().getProof());

    }

}
