package org.royllo.explorer.api.graphql.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * addAssetRequest inputs.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AddAssetRequestInputs {

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    private String genesisPoint;

    /** The name of the asset. */
    private String name;

    /** The hashed metadata of the asset. */
    private String metaData;

    /** The asset ID that uniquely identifies the asset. */
    private String assetId;

    /** The index of the output that carries the unique Taro commitment in the genesis transaction. */
    private int outputIndex;

    /** Proof that validates the asset information. */
    private String proof;

}
