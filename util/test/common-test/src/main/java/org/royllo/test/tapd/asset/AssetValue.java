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

    /** Decoded proof value (with meta reveal). */
    @Getter
    List<DecodedProofValue> decodedProofValuesWithoutMetaReveal = new LinkedList<>();

    /** Decoded proof value (without meta reveal). */
    @Getter
    List<DecodedProofValue> decodedProofValuesWithMetaReveal = new LinkedList<>();

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

        // We have two different lists for decoded proof values with and without meta reveal.
        if (newDecodedProofValue.getRequest().isWithMetaReveal()) {
            this.decodedProofValuesWithMetaReveal.add(newDecodedProofValue);
        } else {
            this.decodedProofValuesWithoutMetaReveal.add(newDecodedProofValue);
        }
    }

    /**
     * Returns the decoded proof requested.
     *
     * @param i index
     * @return decoded proof
     */
    public DecodedProofValueRequest getDecodedProofRequest(final int i) {
        return decodedProofValuesWithoutMetaReveal.get(i).getRequest();
    }

    /**
     * Returns the decoded proof requested.
     *
     * @param i index
     * @return decoded proof
     */
    public DecodedProofValueResponse.DecodedProof getDecodedProofResponse(final int i) {
        return decodedProofValuesWithoutMetaReveal.get(i).getResponse().getDecodedProof();
    }

    /**
     * Returns the decoded proof requested (with meta reveal).
     *
     * @param i index
     * @return decoded proof
     */
    public DecodedProofValueRequest getDecodedProofRequestWithMetaReveal(final int i) {
        return decodedProofValuesWithMetaReveal.get(i).getRequest();
    }

    /**
     * Returns the decoded proof requested (with meta reveal).
     *
     * @param i index
     * @return decoded proof
     */
    public DecodedProofValueResponse.DecodedProof getDecodedProofResponseWithMetaReveal(final int i) {
        return decodedProofValuesWithMetaReveal.get(i).getResponse().getDecodedProof();
    }

}
