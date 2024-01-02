package org.royllo.explorer.core.util.parameters;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * S3 parameters.
 */
@Validated
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "s3")
public class S3Parameters {

    /** S3 access key. */
    @NotEmpty(message = "Access key is required")
    private String accessKey;

    /** S3 secret key. */
    @NotEmpty(message = "Secret key is required")
    private String secretKey;

    /** S3 bucket name. */
    @NotEmpty(message = "Bucket name is required")
    private String bucketName;

    /** S3 endpoint. */
    @NotEmpty(message = "S3 endpoint is required")
    private String endpointURL;

}
