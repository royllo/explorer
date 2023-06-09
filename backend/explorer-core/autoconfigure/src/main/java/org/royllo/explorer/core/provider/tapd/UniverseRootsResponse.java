package org.royllo.explorer.core.provider.tapd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * "taproot-assets/universe/roots" response.
 * <a href="https://lightning.engineering/api-docs/api/taproot-assets/universe/asset-roots/index.html">Documentation</a>
 * <a href="https://testnet.universe.lightning.finance/v1/taproot-assets/universe/roots">Test call</a>
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class UniverseRootsResponse {

    /** Universe roots. */
    @JsonProperty("universe_roots")
    private Map<String, UniverseRoot> universeRoots;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UniverseRoot {

        /** ID. */
        @JsonProperty("id")
        ID id;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ID {

        /** The 32-byte asset ID. */
        @JsonProperty("asset_id")
        String assetId;

    }

}
