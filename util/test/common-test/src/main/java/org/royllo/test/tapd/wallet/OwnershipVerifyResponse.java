package org.royllo.test.tapd.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Ownership verify response.
 */
@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"checkstyle:VisibilityModifier", "unused"})
public class OwnershipVerifyResponse {

    /**
     * Ownership verify result.
     */
    @JsonProperty("valid_proof")
    private Boolean validProof;

    /** Error code. */
    @JsonProperty("code")
    Long errorCode;

    /** Error code. */
    @JsonProperty("message")
    String errorMessage;

    /**
     * Returns the response in JSON.
     *
     * @return json response
     */
    @JsonIgnore
    public String getJSONResponse() {
        try {
            return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Impossible to transform to JSON" + e);
        }
    }

}
