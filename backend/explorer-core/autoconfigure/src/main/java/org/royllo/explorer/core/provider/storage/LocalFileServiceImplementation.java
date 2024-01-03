package org.royllo.explorer.core.provider.storage;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import jakarta.annotation.PreDestroy;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystem;
import java.nio.file.Files;

/**
 * Content service - Local file implementation - Used for local development.
 * It also includes a web server to serve content and simulate a s3 CDN.
 */
@Service
@Profile("!s3-storage")
public class LocalFileServiceImplementation implements ContentService {

    /** Web server port. */
    public static final int WEB_SERVER_PORT = 9093;

    /** Web server host. */
    public static final String WEB_SERVER_HOST = "localhost";

    /** In-memory File system. */
    private final FileSystem fileSystem;

    /** Web server. */
    private final Undertow webServer;

    /**
     * Constructor.
     */
    public LocalFileServiceImplementation() throws DecoderException {
        // Starting an in-memory file system.
        fileSystem = Jimfs.newFileSystem(Configuration.unix());

        // For our tests, we create the asset data for RoylloCoin.
        storeFile("roylloCoin on mainnet by Royllo".getBytes(), "24a27ab522c9c33e64f4462f2acee01571e014ccbbac075786d1deae033a128d.txt");

        // For roylloNFT, we will store an image instead of the text we set.
        final String image = "89504e470d0a1a0a0000000d4948445200000018000000180806000000e0773df80000006e4944415478da63601805a360d880ff38304d0da78a25ff89c4941b5e682f8b81a9660136c3d12ca1cc029b0c75304636182646ae0528ae07d11ef9da182e078951c5025a04d1c059802e36b47c704451008e29cd07ff89f105552dc011f6d4f50135cb229c41448b229ba25403006cf5f5c3b61b973a0000000049454e44ae426082";
        storeFile(Hex.decodeHex(image), "89c9d3ff7cb9dbc4615f854f7127e94db10edd430f8bcf82d7309d0c8b750051.png");

        // Starting a web server to serve content.
        PathResourceManager resourceManager = new PathResourceManager(fileSystem.getPath("."));
        webServer = Undertow.builder()
                .addHttpListener(WEB_SERVER_PORT, WEB_SERVER_HOST) // Set port and host
                .setHandler(new ResourceHandler(resourceManager)) // Set the handler to serve files
                .build();
        webServer.start();
    }

    @PreDestroy
    @SuppressWarnings("checkstyle:DesignForExtension")
    public void onDestroy() throws Exception {
        webServer.stop();
        fileSystem.close();
    }

    @Override
    @SuppressWarnings("checkstyle:DesignForExtension")
    public void storeFile(final byte[] fileContent,
                          final String fileName) {
        try {
            Files.write(fileSystem.getPath(".").resolve(fileName), fileContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
