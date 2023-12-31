package org.royllo.explorer.core.provider.storage;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Local file service - Used for local development.
 * It also includes a web server to serve content and simulate a s3 CDN.
 */
@Service
public class LocalFileService implements FileService {

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
    public LocalFileService() {
        // Starting an in-memory file system.
        fileSystem = Jimfs.newFileSystem(Configuration.unix());

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
        Path file = fileSystem.getPath(".").resolve(fileName);
        try {
            Files.write(file, fileContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
