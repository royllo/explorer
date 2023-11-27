package org.royllo.explorer.web.test.controllers.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.request.AddUniverseServerRequestDTO;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
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
import static org.royllo.explorer.web.util.constants.RequestPageConstants.ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE;
import static org.royllo.explorer.web.util.constants.RequestPageConstants.ADD_UNIVERSE_SERVER_REQUEST_SUCCESS_PAGE;
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
public class AddUniverseServerRequestControllerTest extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messages;

    @Test
    @DisplayName("Add universe server request form test")
    void addUniverseServerRequestFormTest() throws Exception {

        mockMvc.perform(get("/request/universe_server/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.universeServer.error.invalidServerAddress")))));

        mockMvc.perform(get("/request/universe_server/add")
                        .param("serverAddress", "1.1.1.1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE))
                .andExpect(model().attributeExists(FORM_ATTRIBUTE))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.universeServer.error.invalidServerAddress")))));

    }

    @Test
    @DisplayName("Add universe server request form post test with error")
    void addUniverseServerRequestFormPostTestWithError() throws Exception {

        // No server address parameter.
        mockMvc.perform(post("/request/universe_server/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(content().string(containsString(getMessage(messages, "request.view.universeServer.error.invalidServerAddress"))));

        // Empty server address.
        mockMvc.perform(post("/request/universe_server/add")
                        .param("serverAddress", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(content().string(containsString(getMessage(messages, "request.view.universeServer.error.invalidServerAddress"))));

        // Invalid server address.
        mockMvc.perform(post("/request/universe_server/add")
                        .param("serverAddress", "INVALID_SERVER_ADDRESS")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_UNIVERSE_SERVER_REQUEST_FORM_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(content().string(containsString(getMessage(messages, "request.view.universeServer.error.invalidServerAddress"))));

    }

    @Test
    @DisplayName("Add universe server request form post test")
    void addUniverseServerRequestFormPostTest() throws Exception {

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
                .andExpect(content().string(containsString(getMessage(messages, "request.message.creationExplanation"))))
                .andExpect(content().string(containsString(getMessage(messages, "request.view.status"))))
                .andExpect(content().string(containsString("/request/" + request.get().getRequestId())))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.universeServer.error.invalidServerAddress")))));

        // We test the request created (we get it from the model).
        assertNotNull(request.get());
        assertNotNull(request.get().getId());
        assertNotNull(request.get().getRequestId());
        assertEquals(ANONYMOUS_ID, request.get().getCreator().getId());
        assertEquals(ANONYMOUS_USER_USERNAME, request.get().getCreator().getUsername());
        assertEquals(OPENED, request.get().getStatus());
        assertNull(request.get().getErrorMessage());
        assertEquals("1.1.1.1:8080", request.get().getServerAddress());

        // Issue with 52.23.192.176:8089 or https://universe.tiramisuwallet.com:8089/ not working.
        mockMvc.perform(post("/request/universe_server/add")
                        .param("serverAddress", "52.23.192.176:8089")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_UNIVERSE_SERVER_REQUEST_SUCCESS_PAGE))
                // TODO This should fail.
                .andExpect(flash().attribute(FORM_ATTRIBUTE, hasProperty("serverAddress", equalTo("1.1.1.1:8080").toString())))
                .andExpect(model().attributeExists(RESULT_ATTRIBUTE))
                .andExpect(model().hasNoErrors());

        mockMvc.perform(post("/request/universe_server/add")
                        .param("serverAddress", "https://universe.tiramisuwallet.com:8089")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_UNIVERSE_SERVER_REQUEST_SUCCESS_PAGE))
                // TODO This should fail.
                .andExpect(flash().attribute(FORM_ATTRIBUTE, hasProperty("serverAddress", equalTo("1.1.1.1:8080").toString())))
                .andExpect(model().attributeExists(RESULT_ATTRIBUTE))
                .andExpect(model().hasNoErrors());
    }

}
