package org.royllo.explorer.core.dto.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.util.enums.AssetType;

import java.math.BigInteger;

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

    /** The asset creator. */
    UserDTO creator;

    /** The version of the Taro asset. */
    int version;

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    BitcoinTransactionOutputDTO genesisPoint;

    /** The name of the asset. */
    String name;

    /** The hashed metadata of the asset. */
    String metaData;

    /** The asset ID that uniquely identifies the asset. */
    String assetId;

    /** The index of the output that carries the unique Taro commitment in the genesis transaction. */
    int outputIndex;

    /** The full genesis information encoded in a portable manner, so it can be easily copy/pasted for address creation. */
    String genesisBootstrapInformation;

    /** The version of the Taro commitment that created this asset. */
    int genesisVersion;

    /** The type of the asset: normal or a collectible. */
    AssetType type;

    /** The total amount of the asset stored in this Taro UTXO. */
    BigInteger amount;

    /** An optional lock time, as with Bitcoin transactions. */
    int lockTime;

    /** An optional relative lock time, as with Bitcoin transactions. */
    int relativeLockTime;

    /** The version of the script, only version 0 is defined at present. */
    int scriptVersion;

    /** The script key of the asset, which can be spent under Taproot semantics. */
    String scriptKey;

    /** The transaction that anchors the Taro commitment where the asset resides. */
    String anchorTx;

    /** The txid of the anchor transaction. */
    String anchorTxId;

    /** The block hash the contains the anchor transaction above. */
    String anchorBlockHash;

    /** Outpoint (txid:vout) that stores the Taro commitment. */
    String anchorOutpoint;

    /** The raw internal key that was used to create the anchor Taproot output key. */
    String anchorInternalKey;

}
