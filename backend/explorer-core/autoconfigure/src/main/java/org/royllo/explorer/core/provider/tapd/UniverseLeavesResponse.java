package org.royllo.explorer.core.provider.tapd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * "taproot-assets/universe/leaves/asset-id" response.
 * <a href="https://lightning.engineering/api-docs/api/taproot-assets/universe/asset-leaves">Documentation</a>
 * <a href="curl https://testnet.universe.lightning.finance/v1/taproot-assets/universe/leaves/asset-id/f84238ffd7838b663f1800d8147c9338f15688b430f6e9d8d53f148049ef3bcb | jq">Test call</a>
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class UniverseLeavesResponse {

    /** Error code. */
    @JsonProperty("code")
    Long errorCode;

    /** Error code. */
    @JsonProperty("message")
    String errorMessage;

    /** Leaves. */
    @JsonProperty("leaves")
    private List<Leaf> leaves;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Leaf {

        /** Proof. */
        @JsonProperty("proof")
        private String proof;

    }

}
