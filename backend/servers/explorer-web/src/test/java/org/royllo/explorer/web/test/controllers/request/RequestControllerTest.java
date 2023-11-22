package org.royllo.explorer.web.test.controllers.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.royllo.explorer.web.util.constants.PagesConstants.REQUEST_PAGE;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Request controller tests")
@AutoConfigureMockMvc
public class RequestControllerTest extends BaseTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messages;

    @Test
    @DisplayName("View request with invalid request id")
    void viewRequestWithInvalidRequestId() throws Exception {

        mockMvc.perform(get("/request/INVALID_ID"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.noRequestId")))))
                .andExpect(content().string(containsString(getMessage(messages, "request.view.error.requestNotFound"))));

    }

    @Test
    @DisplayName("View request without request id")
    void viewRequestWithoutRequestId() throws Exception {

        // No request id.
        mockMvc.perform(get("/request/"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Error messages.
                .andExpect(content().string(containsString(getMessage(messages, "request.view.error.noRequestId"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.requestNotFound")))));

        // No request id and no "/".
        mockMvc.perform(get("/request"))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Error messages.
                .andExpect(content().string(containsString(getMessage(messages, "request.view.error.noRequestId"))))
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.requestNotFound")))));

    }

    @Test
    @DisplayName("View request test")
    void viewRequestTest() throws Exception {

        // Request 1 - "Add proof" - OPENED - Anonymous.
        String requestId1 = "f5623bdf-9fa6-46cf-85df-request_p_01";
        mockMvc.perform(get("/request/" + requestId1))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Request id.
                .andExpect(content().string(containsString(getMessage(messages, "request") + " " + requestId1)))
                // Request status.
                .andExpect(content().string(containsString(getMessage(messages, "field.request.status") + ": Opened")))
                // No view asset.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.button.asset.view")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.requestNotFound")))));

        // Request 2 - "Add proof" - FAILED with error message - Anonymous.
        String requestId2 = "91425ba6-8b16-46a8-baa6-request_p_02";
        mockMvc.perform(get("/request/" + requestId2))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Request id.
                .andExpect(content().string(containsString(getMessage(messages, "request") + " " + requestId2)))
                // Request status.
                .andExpect(content().string(containsString(getMessage(messages, "field.request.status") + ": Failure")))
                .andExpect(content().string(containsString(getMessage(messages, "field.request.errorMessage") + ": Impossible to decode")))
                // No view asset.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.button.asset.view")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.requestNotFound")))));

        // Request 3 - "Add metadata" - OPENED - straumat.
        String requestId3 = "91425ba6-8b16-46a8-baa6-request_m_01";
        mockMvc.perform(get("/request/" + requestId3))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking each field.
                // Request id.
                .andExpect(content().string(containsString(getMessage(messages, "request") + " " + requestId3)))
                // Request status.
                .andExpect(content().string(containsString(getMessage(messages, "field.request.status") + ": Opened")))
                // No view asset.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.button.asset.view")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.requestNotFound")))));

        // Request 4 - "Add proof" - SUCCESS.
        String requestId4 = "91425ba6-8b16-46a8-baa6-request_p_03";
        mockMvc.perform(get("/request/" + requestId4))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking each field.
                // Request id.
                .andExpect(content().string(containsString(getMessage(messages, "request") + " " + requestId4)))
                // Request status.
                .andExpect(content().string(containsString(getMessage(messages, "field.request.status") + ": Success")))
                // View asset.
                .andExpect(content().string(containsString(getMessage(messages, "request.button.asset.view"))))
                .andExpect(content().string(containsString("\"/asset/" + ROYLLO_COIN_ASSET_ID + "\"")))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.requestNotFound")))));

        // Request 5 - "Add universe server" - Opened.
        String requestId5 = "request_5_universe_opened";
        mockMvc.perform(get("/request/" + requestId5))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking each field.
                // Request id.
                .andExpect(content().string(containsString(getMessage(messages, "request") + " " + requestId5)))
                // Request status.
                .andExpect(content().string(containsString(getMessage(messages, "field.request.status") + ": Opened")))
                // No view asset.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.button.asset.view")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.requestNotFound")))));

        // Request 6 - "Add universe server" - Failure.
        String requestId6 = "request_6_universe_failure";
        mockMvc.perform(get("/request/" + requestId6))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking each field.
                // Request id.
                .andExpect(content().string(containsString(getMessage(messages, "request") + " " + requestId6)))
                // Request status.
                // Request status.
                .andExpect(content().string(containsString(getMessage(messages, "field.request.status") + ": Failure")))
                .andExpect(content().string(containsString(getMessage(messages, "field.request.errorMessage") + ": Server 2.2.2.2 not connecte")))
                // No view asset.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.button.asset.view")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.requestNotFound")))));

        // Request 5 - "Add universe server" - Success.
        String requestId7 = "request_7_universe_success";
        mockMvc.perform(get("/request/" + requestId7))
                .andExpect(status().isOk())
                .andExpect(view().name(REQUEST_PAGE))
                // Checking each field.
                // Request id.
                .andExpect(content().string(containsString(getMessage(messages, "request") + " " + requestId7)))
                // Request status.
                .andExpect(content().string(containsString(getMessage(messages, "field.request.status") + ": Success")))
                // No view asset.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.button.asset.view")))))
                // Error messages.
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.noRequestId")))))
                .andExpect(content().string(not(containsString(getMessage(messages, "request.view.error.requestNotFound")))));
    }

}
