package org.royllo.explorer.core.test.core.util;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.royllo.explorer.core.util.parameters.S3Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * S3 test.
 */
@SpringBootTest
@DirtiesContext
@DisplayName("S3 tests")
public class S3Test extends TestWithMockServers {

    @Autowired
    private S3Parameters s3Parameters;

    @Test
    @DisplayName("S3 connection test")
    public void connectionTest() {
        // Getting a connexion.
        MinioClient minioClient = MinioClient.builder()
                .endpoint(s3MockServer.getS3URL())
                .credentials(s3Parameters.getAccessKey(), s3Parameters.getSecretKey())
                .build();

        // Testing operations.
        try {
            assertTrue(minioClient.bucketExists(BucketExistsArgs.builder().bucket(s3Parameters.getBucketName()).build()));
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            fail("S3 connection test failed: " + e.getMessage());
        }
    }

}
