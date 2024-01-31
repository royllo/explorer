package org.royllo.explorer.core.provider.storage;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.NonNull;
import org.apache.tika.Tika;
import org.royllo.explorer.core.util.base.BaseService;
import org.royllo.explorer.core.util.parameters.S3Parameters;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;


/**
 * Content service - S3 implementation.
 */
@Service
@Profile("s3-storage")
@SuppressWarnings("checkstyle:DesignForExtension")
public class S3ServiceImplementation extends BaseService implements ContentService {

    /** S3 parameters. */
    private S3Parameters s3Parameters;

    /**
     * Constructor.
     *
     * @param newS3Parameters S3 parameters.
     */
    public S3ServiceImplementation(final S3Parameters newS3Parameters) {
        this.s3Parameters = newS3Parameters;
    }

    /**
     * Update S3 parameters.
     *
     * @param newS3Parameters new S3 parameters.
     */
    public void updateS3Parameters(final S3Parameters newS3Parameters) {
        this.s3Parameters = newS3Parameters;
    }

    /**
     * Get Minio client.
     *
     * @return Minio client.
     */
    private MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(s3Parameters.getEndpointURL())
                .credentials(s3Parameters.getAccessKey(), s3Parameters.getSecretKey())
                .build();
    }

    @Override
    public void storeFile(final byte[] fileContent, @NonNull final String fileName) {
        try {
            getMinioClient().putObject(PutObjectArgs.builder()
                    .bucket(s3Parameters.getBucketName())
                    .object(fileName).stream(new ByteArrayInputStream(fileContent), fileContent.length, -1)
                    .contentType(new Tika().detect(fileContent))
                    .build());
        } catch (Exception e) {
            logger.error("Error while storing file {} in S3: {}", fileName, e.getMessage());
        }
    }

    @Override
    public boolean fileExists(@NonNull final String fileName) {
        try {
            getMinioClient().statObject(StatObjectArgs.builder()
                    .bucket(s3Parameters.getBucketName())
                    .object(fileName)
                    .build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            logger.error("Error checking if file exists {} in S3: {}", fileName, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteFile(@NonNull final String fileName) {
        try {
            getMinioClient().removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(s3Parameters.getBucketName())
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            logger.error("Error deleting file {} in S3: {}", fileName, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
