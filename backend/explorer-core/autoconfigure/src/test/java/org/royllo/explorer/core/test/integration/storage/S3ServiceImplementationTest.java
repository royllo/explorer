package org.royllo.explorer.core.test.integration.storage;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.storage.ContentService;
import org.royllo.explorer.core.provider.storage.S3ServiceImplementation;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.royllo.explorer.core.util.parameters.S3Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MinIOContainer;

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
@ActiveProfiles({"s3-storage"})
public class S3ServiceImplementationTest extends TestWithMockServers {

    /** S3 minio image. */
    public static final String MINIO_RELEASE = "minio/minio:RELEASE.2023-09-04T19-57-37Z";

    /** S3 bucket name. */
    public static final String S3_BUCKET_NAME = "royllo-explorer";

    /** S3 user name. */
    public static final String S3_USER_NAME = "royllo-access-key";

    /** S3 password. */
    public static final String S3_PASSWORD = "royllo-secret-key";

    /** S3 mock server. */
    public static MinIOContainer s3MockServer;

    @Autowired
    private ContentService contentService;

    @Test
    @DisplayName("S3 implementation test")
    public void s3ImplementationTest() {
        // Getting a connexion to do some verification.
        MinioClient minioClient = MinioClient.builder()
                .endpoint(s3MockServer.getS3URL())
                .credentials(S3_USER_NAME, S3_PASSWORD)
                .build();

        // Checking that the bucket exists.
        try {
            assertTrue(minioClient.bucketExists(BucketExistsArgs.builder().bucket(S3_BUCKET_NAME).build()));
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            fail("S3 bucket not found: " + e.getMessage());
        }

        // Giving to the S3 service the new parameters.
        S3Parameters parameters = new S3Parameters();
        parameters.setAccessKey(S3_USER_NAME);
        parameters.setSecretKey(S3_PASSWORD);
        parameters.setBucketName(S3_BUCKET_NAME);
        parameters.setEndpointURL(s3MockServer.getS3URL());
        final S3ServiceImplementation s3ServiceImplementation = (S3ServiceImplementation) contentService;
        s3ServiceImplementation.updateS3Parameters(parameters);

        // Checking that a file doesn't exist in minio.
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(S3_BUCKET_NAME).object("test.txt").build());
            fail("The file should not exist");
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            // The file doesn't exist, so we should have an exception.
        }

        // Adding the file.
        contentService.storeFile("test".getBytes(), "test.txt");

        // Checking that the file now exists.
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(S3_BUCKET_NAME).object("test.txt").build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            fail("The file should now exist");
        }

        // Adding the file (again) - Checking there is no exception.
        contentService.storeFile("test".getBytes(), "test.txt");
    }

    @BeforeAll
    public static void startS3MockServer() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // Starting S3 mock server;
        s3MockServer = new MinIOContainer(MINIO_RELEASE).withUserName(S3_USER_NAME).withPassword(S3_PASSWORD);
        s3MockServer.start();

        // Creating the client.
        MinioClient s3Client = MinioClient.builder()
                .endpoint(s3MockServer.getS3URL())
                .credentials(S3_USER_NAME, S3_PASSWORD)
                .build();

        // Creating the bucket if it doesn't exist.
        if (!s3Client.bucketExists(BucketExistsArgs.builder().bucket(S3_BUCKET_NAME).build())) {
            s3Client.makeBucket(MakeBucketArgs.builder().bucket(S3_BUCKET_NAME).build());
        }
    }

    @AfterAll
    public static void stopS3MockServer() {
        // Stopping S3 mock server.
        s3MockServer.stop();
    }

}
