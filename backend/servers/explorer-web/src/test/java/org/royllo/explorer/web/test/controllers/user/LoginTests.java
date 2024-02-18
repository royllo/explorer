package org.royllo.explorer.web.test.controllers.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.royllo.explorer.web.util.constants.HomePagesConstants.HOME_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@DirtiesContext
@DisplayName("Login tests")
@AutoConfigureMockMvc
public class LoginTests extends BaseTest {

    @Autowired
    MessageSource messages;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("Login with straumat")
    @WithMockUser(username = "straumat")
    void loginWithStraumat() throws Exception {

        mockMvc.perform(get("/login/success"))
                // TODO - Fix this test
                //.andExpect(request().sessionAttribute("username", "straumat"))
                .andExpect(status().isOk())
                .andExpect(view().name(HOME_PAGE))
                .andExpect(content().string(containsString(getMessage(messages, "home.message"))))
                .andReturn();

    }

    @Test
    @DisplayName("Login with newUserWithAVeryVeryLongUserName")
    @WithMockUser(username = "newUserWithAVeryVeryLongUserName")
    void loginWithNewUser() throws Exception {

        // This user has not set its settings yet, so it should be redirected to the settings page.
        mockMvc.perform(get("/login/success"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/account/settings"))
                .andReturn();

    }

}
