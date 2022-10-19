package org.royllo.explorer.core.dto.request;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A request to add asset to royllo explorer.
 */
@Getter
@SuperBuilder
@ToString
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AddAssetRequestDTO extends RequestDTO {

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    String genesisPoint;

    /** The name of the asset. */
    String name;

    /** The hashed metadata of the asset. */
    String metaData;

    /** The asset ID that uniquely identifies the asset. */
    String assetId;

    /** The index of the output that carries the unique Taro commitment in the genesis transaction. */
    int outputIndex;

    /** Proof that validates the asset information. */
    String proof;

}
