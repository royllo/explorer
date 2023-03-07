package org.royllo.explorer.web.test.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.request.AddProofRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
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
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.COMMAND_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_ADD_PROOF_FORM_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_ADD_PROOF_SUCCESS_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Proof request controller tests")
@AutoConfigureMockMvc
@PropertySource("classpath:messages.properties")
public class ProofRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Test
    @DisplayName("Proof request form test")
    void proofRequestFormTest() throws Exception {
        mockMvc.perform(get("/request/proof/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_ADD_PROOF_FORM_PAGE))
                .andExpect(model().attributeExists(COMMAND_ATTRIBUTE));
    }

    @Test
    @DisplayName("Proof request form post test")
    void proofRequestFormPostTest() throws Exception {
        // Empty form - raw proof not set.
        mockMvc.perform(post("/request/proof/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_ADD_PROOF_FORM_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeErrorCount(COMMAND_ATTRIBUTE, 1))
                .andExpect(content().string(containsString(environment.getProperty("NotBlank.command.rawProof"))));

        // =============================================================================================================
        // Test if everything is ok if we pass correct information to create a proof.
        AtomicReference<AddProofRequestDTO> proof = new AtomicReference<>();
        mockMvc.perform(post("/request/proof/add")
                        .param("rawProof", "simple raw proof")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_ADD_PROOF_SUCCESS_PAGE))
                .andExpect(flash().attribute(COMMAND_ATTRIBUTE, hasProperty("rawProof", equalTo("simple raw proof").toString())))
                .andExpect(model().attributeExists(RESULT_ATTRIBUTE))
                .andExpect(model().hasNoErrors())
                .andDo(result -> proof.set((AddProofRequestDTO) Objects.requireNonNull(result.getModelAndView()).getModelMap().get(RESULT_ATTRIBUTE)))
                // Check page content.
                .andExpect(content().string(containsString(proof.get().getRequestId())))
                .andExpect(content().string(containsString(environment.getProperty("request.creationMessage"))))
                .andExpect(content().string(containsString(environment.getProperty("request.viewStatus"))))
                .andExpect(content().string(containsString("/request/" + proof.get().getRequestId())));

        // We test the request created.
        assertNotNull(proof.get());
        assertNotNull(proof.get().getId());
        assertNotNull(proof.get().getRequestId());
        assertEquals(0, proof.get().getCreator().getId());
        assertEquals("anonymous", proof.get().getCreator().getUsername());
        assertEquals(OPENED, proof.get().getStatus());
        assertNull(proof.get().getAsset());
        assertNull(proof.get().getErrorMessage());
        assertEquals("simple raw proof", proof.get().getRawProof());
    }

    @Test
    @DisplayName("Proof request form post test")
    void viewProofTest() throws Exception {
        // Request 1 - Add proof OPENED - Anonymous.
        mockMvc.perform(get("/request/f5623bdf-9fa6-46cf-85df-request_p_01"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking each field.
                // Request id.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.requestId"))))
                .andExpect(content().string(containsString(">f5623bdf-9fa6-46cf-85df-request_p_01<")))
                // Creator.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.creator.username"))))
                .andExpect(content().string(containsString(">anonymous<")))
                // Request status.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.status"))))
                .andExpect(content().string(containsString(">Opened<")))
                // NOT error message
                .andExpect(content().string(not(containsString(environment.getProperty("field.asset.errorMessage")))));

        // Request 2 - Add proof FAILED with error message - Anonymous.
        mockMvc.perform(get("/request/91425ba6-8b16-46a8-baa6-request_p_02"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking each field.
                // Request id.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.requestId"))))
                .andExpect(content().string(containsString(">91425ba6-8b16-46a8-baa6-request_p_02<")))
                // Creator.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.creator.username"))))
                .andExpect(content().string(containsString(">anonymous<")))
                // Request status.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.status"))))
                .andExpect(content().string(containsString(">Failure<")))
                // Error message
                .andExpect(content().string(containsString(environment.getProperty("field.asset.errorMessage"))))
                .andExpect(content().string(containsString(">There is an error !<")));

        // Request 3 - Add metadata OPENED - straumat.
        mockMvc.perform(get("/request/91425ba6-8b16-46a8-baa6-request_m_01"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking each field.
                // Request id.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.requestId"))))
                .andExpect(content().string(containsString(">91425ba6-8b16-46a8-baa6-request_m_01<")))
                // Creator.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.creator.username"))))
                .andExpect(content().string(containsString(">straumat<")))
                // Request status.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.status"))))
                .andExpect(content().string(containsString(">Opened<")))
                // NOT error message
                .andExpect(content().string(not(containsString(environment.getProperty("field.asset.errorMessage")))));

        // Request 4 - Add proof SUCCESS.
        mockMvc.perform(get("/request/91425ba6-8b16-46a8-baa6-request_p_03"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking each field.
                // Request id.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.requestId"))))
                .andExpect(content().string(containsString(">91425ba6-8b16-46a8-baa6-request_p_03<")))
                // Creator.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.creator.username"))))
                .andExpect(content().string(containsString(">anonymous<")))
                // Request status.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.status"))))
                .andExpect(content().string(containsString(">Success<")))
                // NOT error message
                .andExpect(content().string(not(containsString(environment.getProperty("field.asset.errorMessage")))));
    }

}
