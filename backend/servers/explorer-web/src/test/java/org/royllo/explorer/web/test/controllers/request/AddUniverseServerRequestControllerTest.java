package org.royllo.explorer.web.test.controllers.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.request.AddUniverseServerRequestDTO;
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
import static org.royllo.explorer.web.util.constants.PagesConstants.ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ADD_UNIVERSE_SERVER_REQUEST_SUCCESS_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.CHOOSE_REQUEST_TYPE_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Add universe server request controller tests")
@AutoConfigureMockMvc
@PropertySource("classpath:i18n/messages.properties")
public class AddUniverseServerRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Test
    @DisplayName("Add universe server request choice")
    void addUniverseServerRequestChoice() throws Exception {
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
    @DisplayName("Add universe server request form test")
    void addUniverseServerRequestFormTest() throws Exception {
        mockMvc.perform(get("/request/universe_server/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("request.universeServer.serverAddress.error.invalidServerAddress")))));

        mockMvc.perform(get("/request/universe_server/add")
                        .param("serverAddress", "1.1.1.1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("request.universeServer.serverAddress.error.invalidServerAddress")))));
    }

    @Test
    @DisplayName("Add universe server request form post test")
    void addUniverseServerRequestFormPostTest() throws Exception {
        // Test if everything is ok if we pass correct information to create a request.
        AtomicReference<AddUniverseServerRequestDTO> request = new AtomicReference<>();
        mockMvc.perform(post("/request/universe_server/add")
                        .param("serverAddress", "1.1.1.1:8080")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_UNIVERSE_SERVER_REQUEST_SUCCESS_PAGE))
                .andExpect(flash().attribute(FORM_ATTRIBUTE, hasProperty("serverAddress", equalTo("1.1.1.1:8080").toString())))
                .andExpect(model().attributeExists(RESULT_ATTRIBUTE))
                .andExpect(model().hasNoErrors())
                .andDo(result -> request.set((AddUniverseServerRequestDTO) Objects.requireNonNull(result.getModelAndView()).getModelMap().get(RESULT_ATTRIBUTE)))
                // Check page content.
                .andExpect(content().string(containsString(request.get().getRequestId())))
                .andExpect(content().string(containsString(environment.getProperty("request.creationMessage"))))
                .andExpect(content().string(containsString(environment.getProperty("request.viewStatus"))))
                .andExpect(content().string(containsString("/request/" + request.get().getRequestId())))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("request.universeServer.add")))));

        // We test the request created (we get it from the model).
        assertNotNull(request.get());
        assertNotNull(request.get().getId());
        assertNotNull(request.get().getRequestId());
        assertEquals(ANONYMOUS_ID, request.get().getCreator().getId());
        assertEquals(ANONYMOUS_USER_USERNAME, request.get().getCreator().getUsername());
        assertEquals(OPENED, request.get().getStatus());
        assertNull(request.get().getErrorMessage());
        assertEquals("1.1.1.1:8080", request.get().getServerAddress());
    }

}
