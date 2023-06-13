package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.service.universe.UniverseServerService;
import org.royllo.explorer.core.util.exceptions.universe.UniverseServerCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DisplayName("UniverseServerService tests")
public class UniverseServerServiceTest {

    @Autowired
    private UniverseServerService universeServerService;

    @Test
    @DisplayName("addUniverseServer()")
    public void addUniverseServer() {

        // =============================================================================================================
        // Adding a universe server with a null value.
        try {
            universeServerService.addUniverseServer(null);
            fail("An exception should have been thrown");
        } catch (UniverseServerCreationException exception) {
            assertEquals("Server address cannot be null", exception.getMessage());
        }

        // =============================================================================================================
        // Adding a universe server with an invalid value.
        try {
            universeServerService.addUniverseServer("invalid");
            fail("An exception should have been thrown");
        } catch (UniverseServerCreationException exception) {
            assertEquals("Invalid server address invalid", exception.getMessage());
        }

        // =============================================================================================================
        // Adding a universe server with a valid value (hostname).
        try {
            universeServerService.addUniverseServer("universe.royllo.org:8080");
        } catch (UniverseServerCreationException exception) {
            fail("An exception should not have been thrown");
        }

        // =============================================================================================================
        // Adding a universe server with a valid value (hostname).
        try {
            universeServerService.addUniverseServer("1.1.1.1:8080");
        } catch (UniverseServerCreationException exception) {
            fail("An exception should not have been thrown");
        }

        // =============================================================================================================
        // Trying to add a duplicated value in universe server.
        try {
            universeServerService.addUniverseServer("1.1.1.1:8080");
            fail("An exception should have been thrown");
        } catch (UniverseServerCreationException exception) {
            assertEquals("1.1.1.1:8080 is already in our database", exception.getMessage());
        }

        // =============================================================================================================
        // Trying to add a duplicated value in universe server (with space around).
        try {
            universeServerService.addUniverseServer("1.1.1.1:8080");
            fail("An exception should have been thrown");
        } catch (UniverseServerCreationException exception) {
            assertEquals("1.1.1.1:8080 is already in our database", exception.getMessage());
        }

    }

    @Test
    @DisplayName("getUniverseServerByServerAddress()")
    public void getUniverseServerByServerAddress() {

        // =============================================================================================================
        // Checking if a universe exists before we create it.
        assertTrue(universeServerService.getUniverseServerByServerAddress("test.royllo.org:8080").isEmpty());

        // =============================================================================================================
        // Adding a universe server with a valid value (hostname).
        try {
            universeServerService.addUniverseServer("test.royllo.org:8080");
        } catch (UniverseServerCreationException exception) {
            fail("An exception should not have been thrown");
        }

        // =============================================================================================================
        // Checking if a universe exists before after create it.
        assertTrue(universeServerService.getUniverseServerByServerAddress("test.royllo.org:8080").isPresent());
        assertTrue(universeServerService.getUniverseServerByServerAddress(" test.royllo.org:8080 ").isPresent());
    }

}
