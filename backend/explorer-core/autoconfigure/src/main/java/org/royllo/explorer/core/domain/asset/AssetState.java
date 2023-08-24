package org.royllo.explorer.core.domain.asset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.base.BaseDomain;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Taproot asset state.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ASSET_STATE")
public class AssetState extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** Asset state creator. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_USER_CREATOR", nullable = false)
    private User creator;

    /** Asset. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_ASSET", nullable = false)
    private Asset asset;

    /** The block hash the contains the anchor transaction above. */
    @Column(name = "ANCHOR_BLOCK_HASH")
    private String anchorBlockHash;

    /** Outpoint (txid:vout) that stores the Taproot asset commitment. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_BITCOIN_TRANSACTION_OUTPUT_ANCHOR_OUTPOINT", updatable = false)
    private BitcoinTransactionOutput anchorOutpoint;

    /** The transaction that anchors the Taproot asset commitment where the asset resides. */
    @Column(name = "ANCHOR_TX")
    private String anchorTx;

    /** The txid of the anchor transaction. */
    @Column(name = "ANCHOR_TXID")
    private String anchorTxId;

    /** The raw internal key that was used to create the anchor Taproot output key. */
    @Column(name = "INTERNAL_KEY")
    private String internalKey;

    /** The Taproot merkle root hash of the anchor output the asset was committed to. */
    @Column(name = "MERKLE_ROOT")
    private String merkleRoot;

    /** The serialized preimage of a Tapscript sibling, if there was one. */
    @Column(name = "TAPSCRIPT_SIBLING")
    private String tapscriptSibling;

    /** The version of the Taproot asset. */
    @Column(name = "VERSION", updatable = false)
    private int version;

    /** The version of the script, only version 0 is defined at present. */
    @Column(name = "SCRIPT_VERSION")
    private int scriptVersion;

    /** The script key of the asset, which can be spent under Taproot semantics. */
    @Column(name = "SCRIPT_KEY")
    private String scriptKey;

}
