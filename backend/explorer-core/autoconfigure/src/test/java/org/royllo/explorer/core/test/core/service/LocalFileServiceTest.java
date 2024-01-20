package org.royllo.explorer.core.test.core.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_HOST;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_PORT;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_NFT_ASSET_ID;

@SpringBootTest
@DirtiesContext
@DisplayName("LocalFileService tests")
public class LocalFileServiceTest extends TestWithMockServers {

    @Autowired
    private LocalFileServiceImplementation localFileServiceImplementation;

    @Test
    @DisplayName("Store and get file")
    public void storeAndGetFile() {
        // We store a file.
        localFileServiceImplementation.storeFile("Hello World!".getBytes(), "hello.txt");

        // We try retrieve hello.txt from the webserver.
        var client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/hello.txt")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("Hello World!"));
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        // We try to retrieve another file that doesn't exist, so we should get a 404 error.
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/non_existing_file.txt")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(404, response.code());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        // We try to store again the same file.
        localFileServiceImplementation.storeFile("Hello World!".getBytes(), "hello.txt");

        // We test that the 24a27ab522c9c33e64f4462f2acee01571e014ccbbac075786d1deae033a128d.txt file for Royllo coin
        // exists and the content is correct.
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + ROYLLO_COIN_ASSET_ID + ".txt")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("roylloCoin on mainnet by Royllo"));
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        // For Royllo NFT, we will store an image instead of the text we set.
        // We check if we retrieve the image correctly.
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + ROYLLO_NFT_ASSET_ID + ".png")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertTrue(response.body().string().contains("PNG"));
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

    }

    @Test
    @DisplayName("File exists")
    public void fileExists() {
        // The file should not exist.
        assertFalse(localFileServiceImplementation.fileExists("fileExistsTest.txt"));

        // We create the file.
        localFileServiceImplementation.storeFile("Hello World!".getBytes(), "fileExistsTest.txt");

        // The file should now exist.
        assertTrue(localFileServiceImplementation.fileExists("fileExistsTest.txt"));
    }

    @Test
    @DisplayName("Delete file")
    public void deleteFile() {
        // The file should not exist.
        assertFalse(localFileServiceImplementation.fileExists("fileDeleteTest.txt"));

        // We create the file.
        localFileServiceImplementation.storeFile("Hello World!".getBytes(), "fileDeleteTest.txt");

        // The file should now exist.
        assertTrue(localFileServiceImplementation.fileExists("fileDeleteTest.txt"));

        // We delete the file.
        localFileServiceImplementation.deleteFile("fileDeleteTest.txt");

        // The file should not exist anymore.
        assertFalse(localFileServiceImplementation.fileExists("fileDeleteTest.txt"));
    }

}
