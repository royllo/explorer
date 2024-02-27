package org.royllo.explorer.core.provider.tapd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Ownership verify request.
 */
@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"checkstyle:VisibilityModifier", "unused"})
public class OwnershipVerifyRequest {

    /**
     * Proof with witness.
     */
    @JsonProperty("proof_with_witness")
    private String proofWithWitness;

    /**
     * Returns the request in JSON.
     *
     * @return json request
     */
    @JsonIgnore
    public String getJSONRequest() {
        try {
            return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Impossible to transform to JSON" + e);
        }
    }

}
