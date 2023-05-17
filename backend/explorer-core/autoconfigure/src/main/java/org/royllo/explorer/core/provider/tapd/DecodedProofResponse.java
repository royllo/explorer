package org.royllo.explorer.core.provider.tapd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

/**
 * Decoded proof response.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class DecodedProofResponse {

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
            int version;

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
            String assetGroup;
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

                /** Meta. */
                @JsonProperty("meta")
                String meta;

                /** Asset id. */
                @JsonProperty("asset_id")
                String assetId;

                /** Output index. */
                @JsonProperty("output_index")
                long outputIndex;

                /** Genesis bootstrap info. */
                @JsonProperty("genesis_bootstrap_info")
                String genesisBootstrapInfo;

                /** Version. */
                @JsonProperty("version")
                int version;

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

            }

        }

    }

}
