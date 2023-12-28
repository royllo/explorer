package org.royllo.explorer.core.test.core.util;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * S3Test.
 */
@SpringBootTest
@DisplayName("S3 tests")
public class S3Test extends TestWithMockServers {

    @Test
    @DisplayName("S3 connection test")
    public void connectionTest() {
        // Getting a connexion.
        MinioClient minioClient = MinioClient.builder()
                .endpoint(s3MockServer.getS3URL())
                .credentials("user", "password")
                .build();

        // Testing operations.
        try {
            minioClient.bucketExists(BucketExistsArgs.builder().bucket("test").build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            fail("S3 connection test failed: " + e.getMessage());
        }
    }

}
