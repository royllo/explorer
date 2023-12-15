package org.royllo.explorer.core.domain.asset;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import lombok.experimental.NonFinal;
import org.royllo.explorer.core.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.base.BaseDomain;
import org.royllo.explorer.core.util.converter.StringListConverter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

    /** The asset state ID that uniquely identifies the asset state (calculated by Royllo). */
    @NonFinal
    @Column(name = "ASSET_STATE_ID", updatable = false)
    private String assetStateId;

    /** Asset. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_ASSET", nullable = false)
    private Asset asset;

    /** The transaction that anchors the Taproot asset commitment where the asset resides. */
    @Column(name = "ANCHOR_TX")
    private String anchorTx;

    /** The block hash the contains the anchor transaction above. */
    @Column(name = "ANCHOR_BLOCK_HASH")
    private String anchorBlockHash;

    /** Outpoint (txid:vout) that stores the Taproot asset commitment. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_BITCOIN_TRANSACTION_OUTPUT_ANCHOR_OUTPOINT", updatable = false)
    private BitcoinTransactionOutput anchorOutpoint;

    /** The raw internal key that was used to create the anchor Taproot output key. */
    @Column(name = "INTERNAL_KEY")
    private String internalKey;

    /** The Taproot merkle root hash of the anchor output the asset was committed to. */
    @Column(name = "MERKLE_ROOT")
    private String merkleRoot;

    /** The serialized preimage of a Tapscript sibling, if there was one. */
    @Column(name = "TAPSCRIPT_SIBLING")
    private String tapscriptSibling;

    /** The version of the Taproot Asset. */
    @Column(name = "VERSION")
    private String version;

    /** The total amount of the asset stored in this Taproot asset UTXO. */
    @Column(name = "AMOUNT")
    private BigInteger amount;

    /** An optional locktime, as with Bitcoin transactions. */
    @Column(name = "LOCK_TIME")
    private int lockTime;

    /** An optional relative lock time, same as Bitcoin transactions. */
    @Column(name = "RELATIVE_LOCK_TIME")
    private int relativeLockTime;

    /** The version of the script, only version 0 is defined at present. */
    @Column(name = "SCRIPT_VERSION")
    private int scriptVersion;

    /** The script key of the asset, which can be spent under Taproot semantics. */
    @Column(name = "SCRIPT_KEY")
    private String scriptKey;

    /** Indicates whether the asset has been spent. */
    @Column(name = "SPENT")
    private boolean spent;

    /** If the asset has been leased, this is the owner (application ID) of the lease. */
    @Column(name = "LEASE_OWNER")
    private String leaseOwner;

    /** If the asset has been leased, this is the expiry of the lease as a Unix timestamp in seconds. */
    @Column(name = "LEASE_EXPIRY")
    private long leaseExpiry;

    /** Indicates whether this transfer was an asset burn. If true, the number of assets in this output are destroyed and can no longer be spent. */
    @Column(name = "BURN")
    private boolean burn;

    /** The merkle proof for AnchorTx used to prove its inclusion within BlockHeader. */
    @Column(name = "TX_MERKLE_PROOF")
    private String txMerkleProof;

    /** The TaprootProof proving the new inclusion of the resulting asset within AnchorTx. */
    @Column(name = "INCLUSION_PROOF")
    private String inclusionProof;

    /** The set of TaprootProofs proving the exclusion of the resulting asset from all other Taproot outputs within AnchorTx. */
    @Convert(converter = StringListConverter.class)
    @Column(name = "EXCLUSION_PROOFS")
    private List<String> exclusionProofs = new ArrayList<>();

    /** An optional TaprootProof needed if this asset is the result of a split. SplitRootProof proves inclusion of the root asset of the split. */
    @Column(name = "SPLIT_ROOT_PROOF")
    private String splitRootProof;

    /** ChallengeWitness is an optional virtual transaction witness that serves as an ownership proof for the asset. */
    @Convert(converter = StringListConverter.class)
    @Column(name = "CHALLENGE_WITNESS")
    private List<String> challengeWitness = new ArrayList<>();

}
