package org.royllo.explorer.web.test.controllers.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.service.user.UserService;
import org.royllo.explorer.web.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.royllo.explorer.web.util.constants.UserPageConstants.USER_PUBLIC_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DirtiesContext
@DisplayName("User public page controller tests")
@AutoConfigureMockMvc
public class UserPageControllerTest extends BaseTest {

    @Autowired
    MessageSource messages;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("User public page")
    void userPublicPage() throws Exception {

        // User not found.
        mockMvc.perform(get("/user/UNKNOWN"))
                .andExpect(status().isNotFound());

        // Normal page.
        mockMvc.perform(get("/user/" + STRAUMAT_USER_USERNAME))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_PUBLIC_PAGE))
                // Checking user profile.
                .andExpect(content().string(containsString("/" + STRAUMAT_USER_ID + ".")))
                .andExpect(content().string(containsString(STRAUMAT_USER_FULL_NAME)))
                .andExpect(content().string(containsString(STRAUMAT_USER_BIOGRAPHY)))
                .andExpect(content().string(containsString(STRAUMAT_USER_WEBSITE)))
                // Assets are displayed.
                .andExpect(content().string(containsString("setOfRoylloNFT2")))
                .andExpect(content().string(containsString("trickyRoylloCoin")));

    }

}
