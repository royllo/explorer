package org.royllo.test.dto.tapd;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * Asset value (contains decoded proof values).
 */
@Getter
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AssetValue {

    /** Decoded proof value. */
    List<DecodedProofValue> decodedProofValue = new LinkedList<>();

    /** Asset id. */
    private String assetId;

    /**
     * Add a decoded proof value.
     *
     * @param newDecodedProofValue decoded proof value
     */
    public void addDecodedProofValue(final DecodedProofValue newDecodedProofValue) {
        if (assetId == null) {
            assetId = newDecodedProofValue.getResponse().getDecodedProof().getAsset().getAssetGenesis().getAssetId();
        }
        this.decodedProofValue.add(newDecodedProofValue);
    }

}
