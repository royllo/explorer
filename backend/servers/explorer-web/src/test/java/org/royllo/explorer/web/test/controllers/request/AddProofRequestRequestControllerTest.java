package org.royllo.explorer.web.test.controllers.request;

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
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.FORM_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ADD_PROOF_REQUEST_FORM_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ADD_PROOF_REQUEST_SUCCESS_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.CHOOSE_REQUEST_TYPE_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Add proof request controller tests")
@AutoConfigureMockMvc
@PropertySource("classpath:i18n/messages.properties")
public class AddProofRequestRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Test
    @DisplayName("Add proof request choice")
    void addProofRequestChoice() throws Exception {
        mockMvc.perform(get("/request/choose_request_type"))
                .andExpect(status().isOk())
                .andExpect(view().name(CHOOSE_REQUEST_TYPE_PAGE))
                // Two choices must be available.
                .andExpect(content().string(containsString(environment.getProperty("request.proof.add"))))
                .andExpect(content().string(containsString("/request/proof/add")))
                .andExpect(content().string(containsString(environment.getProperty("request.universeServer.add"))))
                .andExpect(content().string(containsString("/request/universe_server/add")));
    }

    @Test
    @DisplayName("Add proof request form test")
    void addProofRequestFormTest() throws Exception {
        mockMvc.perform(get("/request/proof/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_PROOF_REQUEST_FORM_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("NotBlank.command.rawProof")))));
    }

    @Test
    @DisplayName("Add proof request form post test")
    void addProofRequestFormPostTest() throws Exception {
        // Empty form - raw proof not set.
        mockMvc.perform(post("/request/proof/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_PROOF_REQUEST_FORM_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeErrorCount(FORM_ATTRIBUTE, 1))
                .andExpect(content().string(containsString(environment.getProperty("NotBlank.command.rawProof"))));

        // Test if everything is ok if we pass correct information to create a request.
        AtomicReference<AddProofRequestDTO> proof = new AtomicReference<>();
        mockMvc.perform(post("/request/proof/add")
                        .param("rawProof", "simple raw proof")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_PROOF_REQUEST_SUCCESS_PAGE))
                .andExpect(flash().attribute(FORM_ATTRIBUTE, hasProperty("rawProof", equalTo("simple raw proof").toString())))
                .andExpect(model().attributeExists(RESULT_ATTRIBUTE))
                .andExpect(model().hasNoErrors())
                .andDo(result -> proof.set((AddProofRequestDTO) Objects.requireNonNull(result.getModelAndView()).getModelMap().get(RESULT_ATTRIBUTE)))
                // Check page content.
                .andExpect(content().string(containsString(proof.get().getRequestId())))
                .andExpect(content().string(containsString(environment.getProperty("request.creationMessage"))))
                .andExpect(content().string(containsString(environment.getProperty("request.viewStatus"))))
                .andExpect(content().string(containsString("/request/" + proof.get().getRequestId())))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("NotBlank.command.rawProof")))));

        // We test the request created (we get it from the model).
        assertNotNull(proof.get());
        assertNotNull(proof.get().getId());
        assertNotNull(proof.get().getRequestId());
        assertEquals(ANONYMOUS_ID, proof.get().getCreator().getId());
        assertEquals(ANONYMOUS_USER_USERNAME, proof.get().getCreator().getUsername());
        assertEquals(OPENED, proof.get().getStatus());
        assertNull(proof.get().getAsset());
        assertNull(proof.get().getErrorMessage());
        assertEquals("simple raw proof", proof.get().getRawProof());
    }

}
