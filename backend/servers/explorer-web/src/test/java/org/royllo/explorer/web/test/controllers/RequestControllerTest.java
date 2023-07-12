package org.royllo.explorer.web.test.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Request controller tests")
@AutoConfigureMockMvc
@PropertySource("classpath:messages.properties")
public class RequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Environment environment;

    @Test
    @DisplayName("View request test")
    void viewRequestTest() throws Exception {
        // TODO Review this test
        // Request 1 - "Add proof" - OPENED - Anonymous.
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
                // Error messages.
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("request.view.error.requestNotFound"))
                        .replace("\"{0}\"", "&quot;&quot;")))));

        // Request 2 - "Add proof" - FAILED with error message - Anonymous.
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
                // Error messages.
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("request.view.error.requestNotFound"))
                        .replace("\"{0}\"", "&quot;&quot;")))));

        // Request 3 - "Add metadata" - OPENED - straumat.
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
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("request.view.error.requestNotFound"))
                        .replace("\"{0}\"", "&quot;&quot;")))));

        // Request 4 - "Add proof" - SUCCESS.
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
                // View asset.
                .andExpect(content().string(containsString(environment.getProperty("request.button.asset.view"))))
                .andExpect(content().string(containsString("\"/asset/f9dd292bb211dae8493645150b36efa990841b11038d026577440d2616d1ec32\"")))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("request.view.error.requestNotFound"))
                        .replace("\"{0}\"", "&quot;&quot;")))));

        // Trim test.
        // Request 4 - "Add proof" - SUCCESS.
        mockMvc.perform(get("/request/ 91425ba6-8b16-46a8-baa6-request_p_03 "))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking each field.
                // Request id.
                .andExpect(content().string(containsString(environment.getProperty("field.asset.requestId"))))
                .andExpect(content().string(containsString(">91425ba6-8b16-46a8-baa6-request_p_03<")))
                // Error messages.
                .andExpect(content().string(not(containsString(environment.getProperty("request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("request.view.error.requestNotFound"))
                        .replace("\"{0}\"", "&quot;&quot;")))));

        // When a request concerns a universe server, asset id is null ! so it should not be displayed!
        // REQUEST_ID_7
        mockMvc.perform(get("/request/REQUEST_ID_7"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE));
    }

    @Test
    @DisplayName("View request with invalid request id")
    void viewRequestWithInvalidRequestId() throws Exception {
        // TODO Review this test
        mockMvc.perform(get("/request/INVALID_ID"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Error messages.
                .andExpect(content().string(containsString(Objects.requireNonNull(
                                environment.getProperty("request.view.error.requestNotFound"))
                        .replace("\"{0}\"", "&quot;INVALID_ID&quot;"))))
                .andExpect(content().string(not(containsString(environment.getProperty("request.view.error.noRequestId")))));
    }

    @Test
    @DisplayName("View request with invalid request id")
    void viewRequestWithoutRequestId() throws Exception {
        // TODO Review this test
        // No request id.
        mockMvc.perform(get("/request/"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Error messages.
                .andExpect(content().string(containsString(environment.getProperty("request.view.error.noRequestId"))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("request.view.error.requestNotFound"))
                        .replace("\"{0}\"", "&quot;&quot;")))));

        // No request id and no "/".
        mockMvc.perform(get("/request"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Error messages.
                .andExpect(content().string(containsString(environment.getProperty("request.view.error.noRequestId"))))
                .andExpect(content().string(not(containsString(Objects.requireNonNull(
                                environment.getProperty("request.view.error.requestNotFound"))
                        .replace("\"{0}\"", "&quot;&quot;")))));
    }

}
