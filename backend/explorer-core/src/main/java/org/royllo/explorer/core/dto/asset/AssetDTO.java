package org.royllo.explorer.core.dto.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.dto.user.UserDTO;

import static lombok.AccessLevel.PRIVATE;

/**
 * Taro asset.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class AssetDTO {

    /** Unique identifier. */
    Long id;

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    BitcoinTransactionOutputDTO genesisPoint;

    /** The asset creator. */
    UserDTO creator;

    /** The name of the asset. */
    String name;

    /** The hashed metadata of the asset. */
    String metaData;

    /** The asset ID that uniquely identifies the asset. */
    String assetId;

    /** The index of the output that carries the unique Taro commitment in the genesis transaction. */
    int outputIndex;

}
