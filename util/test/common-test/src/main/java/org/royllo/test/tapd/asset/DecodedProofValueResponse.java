package org.royllo.test.tapd.asset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

/**
 * Decoded proof response (for tests).
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class DecodedProofValueResponse {

    /** Decoded proof. */
    @JsonProperty("decoded_proof")
    DecodedProof decodedProof;

    /** Error code. */
    @JsonProperty("code")
    Long errorCode;

    /** Error code. */
    @JsonProperty("message")
    String errorMessage;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DecodedProof {

        /** Proof at depth. */
        @JsonProperty("proof_at_depth")
        long proofAtDepth;

        /** Number of proofs. */
        @JsonProperty("number_of_proofs")
        long numberOfProofs;

        /** Asset. */
        @JsonProperty("asset")
        Asset asset;

        /** Transaction merkle proof. */
        @JsonProperty("tx_merkle_proof")
        String txMerkleProof;

        /** Inclusion proof. */
        @JsonProperty("inclusion_proof")
        String inclusionProof;

        /** Exclusion proofs. */
        @JsonProperty("exclusion_proofs")
        List<String> exclusionProofs;

        @Getter
        @Setter
        @NoArgsConstructor
        @ToString
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Asset {

            /** Version. */
            @JsonProperty("version")
            String version;

            /** Asset genesis. */
            @JsonProperty("asset_genesis")
            AssetGenesis assetGenesis;

            /** Asset type. */
            @JsonProperty("asset_type")
            String assetType;

            /** Amount. */
            @JsonProperty("amount")
            BigInteger amount;

            /** Lock time. */
            @JsonProperty("lock_time")
            long lockTime;

            /** Relative lock time. */
            @JsonProperty("relative_lock_time")
            long relativeLockTime;

            /** Script version. */
            @JsonProperty("script_version")
            int scriptVersion;

            /** Script key. */
            @JsonProperty("script_key")
            String scriptKey;

            /** Asset group. */
            @JsonProperty("asset_group")
            AssetGroup assetGroup;

            /** Chain anchor. */
            @JsonProperty("chain_anchor")
            ChainAnchor chainAnchor;

            /** Previous witnesses. */
            @JsonProperty("prev_witnesses")
            List<String> prevWitnesses;

            @Getter
            @Setter
            @NoArgsConstructor
            @ToString
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class AssetGenesis {

                /** Genesis point. */
                @JsonProperty("genesis_point")
                String genesisPoint;

                /** Name. */
                @JsonProperty("name")
                String name;

                /** Metadata hash. */
                @JsonProperty("meta_hash")
                String metaDataHash;

                /** Asset id. */
                @JsonProperty("asset_id")
                String assetId;

                /** Output index. */
                @JsonProperty("output_index")
                long outputIndex;

                /** Version. */
                @JsonProperty("version")
                int version;

            }

            @Getter
            @Setter
            @NoArgsConstructor
            @ToString
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class AssetGroup {

                /** The raw group key which is a normal public key. */
                @JsonProperty("raw_group_key")
                String rawGroupKey;

                /** The tweaked group key, which is derived based on the genesis point and also asset type. */
                @JsonProperty("tweaked_group_key")
                String tweakedGroupKey;

                /** A signature over the genesis point using the above key. */
                @JsonProperty("asset_id_sig")
                String assetIdSig;

            }

            @Getter
            @Setter
            @NoArgsConstructor
            @ToString
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ChainAnchor {

                /** Anchor tx. */
                @JsonProperty("anchor_tx")
                String anchorTx;

                /** Anchor txid. */
                @JsonProperty("anchor_txid")
                String anchorTxId;

                /** Anchor block hash. */
                @JsonProperty("anchor_block_hash")
                String anchorBlockHash;

                /** Anchor outpoint. */
                @JsonProperty("anchor_outpoint")
                String anchorOutpoint;

                /** Internal key. */
                @JsonProperty("internal_key")
                String internalKey;

                /** The Taproot merkle root hash of the anchor output the asset was committed to. */
                @JsonProperty("merkle_root")
                String merkleRoot;

                /** The serialized preimage of a Tapscript sibling, if there was one. */
                @JsonProperty("tapscript_sibling")
                String tapscriptSibling;

            }

        }

    }

}
