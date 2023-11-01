package org.royllo.test.tapd.asset;

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
    List<DecodedProofValue> decodedProofValues = new LinkedList<>();

    /** Asset id. */
    private String assetId;

    /**
     * Add a decoded proof value.
     *
     * @param newDecodedProofValue decoded proof value
     */
    public void addDecodedProofValue(final DecodedProofValue newDecodedProofValue) {
        if (assetId == null && newDecodedProofValue.getResponse().getDecodedProof() != null) {
            assetId = newDecodedProofValue.getResponse().getDecodedProof().getAsset().getAssetGenesis().getAssetId();
        }
        this.decodedProofValues.add(newDecodedProofValue);
    }

    /**
     * Returns the decoded proof requested.
     *
     * @param i index
     * @return decoded proof
     */
    public DecodedProofValueRequest getDecodedProofRequest(final int i) {
        return decodedProofValues.get(i).getRequest();
    }

    /**
     * Returns the decoded proof requested.
     *
     * @param i index
     * @return decoded proof
     */
    public DecodedProofValueResponse.DecodedProof getDecodedProof(final int i) {
        return decodedProofValues.get(i).getResponse().getDecodedProof();
    }

}
