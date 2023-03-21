package org.royllo.explorer.batch.test.util.mock;

import org.mockito.Mockito;
import org.royllo.explorer.batch.test.util.BaseTest;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;
import org.royllo.explorer.core.provider.tarod.TarodProofService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link TarodProofService} mock.
 */
@Profile("tarodProofServiceMock")
@Configuration
public class TarodProofServiceMock extends BaseTest {

    @Bean
    @Primary
    public TarodProofService tarodProofService() {
        final TarodProofService mockedService = Mockito.mock(TarodProofService.class);

        // Non-existing proof.
        // Mockito.when(mockedService.decode(any(), 0)).thenReturn(Mono.empty());

        Mockito.when(mockedService.decode(MY_ROYLLO_COIN_RAW_PROOF, 0)).thenReturn(Mono.just(getMyRoylloCoin()));
        Mockito.when(mockedService.decode(UNKNOWN_ROYLLO_COIN_RAW_PROOF, 0)).thenReturn(Mono.just(getUnknownRoylloCoin()));

        return mockedService;
    }

    /**
     * Returns "My royllo coin" decoded proof.
     *
     * @return "My royllo coin" decoded proof.
     */
    private DecodedProofResponse getMyRoylloCoin() {
        // "myRoylloCoin" (documented in docs/test/myRoylloCoin.MD).
        DecodedProofResponse decodedProofResponse = new DecodedProofResponse();

        // Decoded proof.
        DecodedProofResponse.DecodedProof decodedProof = new DecodedProofResponse.DecodedProof();
        decodedProof.setProofIndex(0);
        decodedProof.setNumberOfProofs(1);

        // Asset.
        DecodedProofResponse.DecodedProof.Asset asset = new DecodedProofResponse.DecodedProof.Asset();
        asset.setVersion(MY_ROYLLO_COIN_VERSION);

        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = new DecodedProofResponse.DecodedProof.Asset.AssetGenesis();
        assetGenesis.setGenesisPoint(MY_ROYLLO_COIN_GENESIS_POINT_TXID + ":" + MY_ROYLLO_COIN_GENESIS_POINT_VOUT);
        assetGenesis.setName(MY_ROYLLO_COIN_NAME);
        assetGenesis.setMeta(MY_ROYLLO_COIN_META);
        assetGenesis.setAssetId(MY_ROYLLO_COIN_ASSET_ID);
        assetGenesis.setOutputIndex(MY_ROYLLO_COIN_OUTPUT_INDEX);
        assetGenesis.setGenesisBootstrapInfo(MY_ROYLLO_COIN_GENESIS_BOOTSTRAP_INFORMATION);
        assetGenesis.setVersion(MY_ROYLLO_COIN_GENESIS_VERSION);
        asset.setAssetGenesis(assetGenesis);

        asset.setAssetType(MY_ROYLLO_COIN_ASSET_TYPE.toString());
        asset.setAmount(MY_ROYLLO_COIN_AMOUNT);
        asset.setLockTime(MY_ROYLLO_COIN_LOCK_TIME);
        asset.setRelativeLockTime(MY_ROYLLO_COIN_RELATIVE_LOCK_TIME);
        asset.setScriptVersion(MY_ROYLLO_COIN_SCRIPT_VERSION);
        asset.setScriptKey(MY_ROYLLO_COIN_SCRIPT_KEY);
        asset.setAssetGroup(null);

        // Chain anchor.
        DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchor = new DecodedProofResponse.DecodedProof.Asset.ChainAnchor();
        chainAnchor.setAnchorTx(MY_ROYLLO_COIN_ANCHOR_TX);
        chainAnchor.setAnchorTxId(MY_ROYLLO_COIN_ANCHOR_TX_ID);
        chainAnchor.setAnchorBlockHash(MY_ROYLLO_COIN_ANCHOR_BLOCK_HASH);
        chainAnchor.setAnchorOutpoint(MY_ROYLLO_COIN_ANCHOR_OUTPOINT);
        chainAnchor.setInternalKey(MY_ROYLLO_COIN_ANCHOR_INTERNAL_KEY);
        asset.setChainAnchor(chainAnchor);

        asset.setPrevWitnesses(Collections.emptyList());

        decodedProof.setTxMerkleProof(MY_ROYLLO_COIN_TX_MERKLE_PROOF);
        decodedProof.setInclusionProof(MY_ROYLLO_COIN_INCLUSION_PROOF);
        decodedProof.setExclusionProofs(Collections.emptyList());
        decodedProofResponse.setDecodedProof(decodedProof);

        decodedProof.setAsset(asset);

        return decodedProofResponse;
    }

    /**
     * Returns "Unknown royllo coin" decoded proof.
     *
     * @return "unknown royllo coin" decoded proof.
     */
    private DecodedProofResponse getUnknownRoylloCoin() {
        // "unknownRoylloCoin" (documented in docs/test/unknownRoylloCoin.MD).
        DecodedProofResponse decodedProofResponse = new DecodedProofResponse();

        // Decoded proof.
        DecodedProofResponse.DecodedProof decodedProof = new DecodedProofResponse.DecodedProof();
        decodedProof.setProofIndex(0);
        decodedProof.setNumberOfProofs(1);

        // Asset.
        DecodedProofResponse.DecodedProof.Asset asset = new DecodedProofResponse.DecodedProof.Asset();
        asset.setVersion(UNKNOWN_ROYLLO_COIN_VERSION);

        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = new DecodedProofResponse.DecodedProof.Asset.AssetGenesis();
        assetGenesis.setGenesisPoint(UNKNOWN_ROYLLO_COIN_GENESIS_POINT_TXID + ":" + UNKNOWN_ROYLLO_COIN_GENESIS_POINT_VOUT);
        assetGenesis.setName(UNKNOWN_ROYLLO_COIN_NAME);
        assetGenesis.setMeta(UNKNOWN_ROYLLO_COIN_META);
        assetGenesis.setAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID);
        assetGenesis.setOutputIndex(UNKNOWN_ROYLLO_COIN_OUTPUT_INDEX);
        assetGenesis.setGenesisBootstrapInfo(UNKNOWN_ROYLLO_COIN_GENESIS_BOOTSTRAP_INFORMATION);
        assetGenesis.setVersion(UNKNOWN_ROYLLO_COIN_GENESIS_VERSION);
        asset.setAssetGenesis(assetGenesis);

        asset.setAssetType(UNKNOWN_ROYLLO_COIN_ASSET_TYPE.toString());
        asset.setAmount(UNKNOWN_ROYLLO_COIN_AMOUNT);
        asset.setLockTime(UNKNOWN_ROYLLO_COIN_LOCK_TIME);
        asset.setRelativeLockTime(UNKNOWN_ROYLLO_COIN_RELATIVE_LOCK_TIME);
        asset.setScriptVersion(UNKNOWN_ROYLLO_COIN_SCRIPT_VERSION);
        asset.setScriptKey(UNKNOWN_ROYLLO_COIN_SCRIPT_KEY);
        asset.setAssetGroup(null);

        // Chain anchor.
        DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchor = new DecodedProofResponse.DecodedProof.Asset.ChainAnchor();
        chainAnchor.setAnchorTx(UNKNOWN_ROYLLO_COIN_ANCHOR_TX);
        chainAnchor.setAnchorTxId(UNKNOWN_ROYLLO_COIN_ANCHOR_TX_ID);
        chainAnchor.setAnchorBlockHash(UNKNOWN_ROYLLO_COIN_ANCHOR_BLOCK_HASH);
        chainAnchor.setAnchorOutpoint(UNKNOWN_ROYLLO_COIN_ANCHOR_OUTPOINT);
        chainAnchor.setInternalKey(UNKNOWN_ROYLLO_COIN_ANCHOR_INTERNAL_KEY);
        asset.setChainAnchor(chainAnchor);

        asset.setPrevWitnesses(Collections.emptyList());

        decodedProof.setTxMerkleProof(UNKNOWN_ROYLLO_COIN_TX_MERKLE_PROOF);
        decodedProof.setInclusionProof(UNKNOWN_ROYLLO_COIN_INCLUSION_PROOF);
        decodedProof.setExclusionProofs(Collections.emptyList());
        decodedProofResponse.setDecodedProof(decodedProof);

        decodedProof.setAsset(asset);

        return decodedProofResponse;
    }


    /**
     * Returns habibtaro coin decoded proof
     *
     * @return habibtaro coin decoded proof
     */
    private DecodedProofResponse getHabitaroCoin() {
        // Decoded response.
        DecodedProofResponse decodedProofResponse = new DecodedProofResponse();

        // Decoded proof.
        DecodedProofResponse.DecodedProof decodedProof = new DecodedProofResponse.DecodedProof();
        decodedProof.setProofIndex(1);
        decodedProof.setNumberOfProofs(2);

        // Asset - "habibtaro_remote".
        DecodedProofResponse.DecodedProof.Asset asset = new DecodedProofResponse.DecodedProof.Asset();
        asset.setVersion(0);

        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = new DecodedProofResponse.DecodedProof.Asset.AssetGenesis();
        assetGenesis.setGenesisPoint("2b572baa90ab27552c7577a4992b048689af30b8549379be83a5b576e3ebc210:0");
        assetGenesis.setName("habibtaro_remote");
        assetGenesis.setMeta("aGFiaWJ0YXJvLmNvbV9yZW1vdGU=");
        assetGenesis.setAssetId("PgVFMqaoaedeG96KJeeUQXfWSKA0/lwUirf3dDU4zJ4=");
        assetGenesis.setOutputIndex(0L);
        assetGenesis.setGenesisBootstrapInfo("EMLr43a1pYO+eZNUuDCviYYEK5mkd3UsVSerkKorVysAAAAAEGhhYmlidGFyb19yZW1vdGUUaGFiaWJ0YXJvLmNvbV9yZW1vdGUAAAAAAA==");
        assetGenesis.setVersion(0);
        asset.setAssetGenesis(assetGenesis);

        asset.setAssetType("NORMAL");
        asset.setAmount(BigInteger.valueOf(10));
        asset.setLockTime(0);
        asset.setRelativeLockTime(0);
        asset.setScriptVersion(0);
        asset.setScriptKey("Ao9XAROi+fc/XqUXTlcCVUi72pjQMj8F9bVr5vvjQMTu");
        asset.setAssetGroup(null);

        // Chain anchor.
        DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchor = new DecodedProofResponse.DecodedProof.Asset.ChainAnchor();
        chainAnchor.setAnchorTx("AgAAAAABAotUvaRjib4LIAdu+JHZhfKL09chEbykNqv8Wtf4ZjuFAQAAAAD/////i1S9pGOJvgsgB274kdmF8ovT1yERvKQ2q/xa1/hmO4UAAAAAAAAAAAAD6AMAAAAAAAAiUSA9IQPuVFvpjbeV1z4R/YY0s8C9ddkTNAJSC2HoHyHIregDAAAAAAAAIlEgUai+/8Zd3X7xelRG7HWkqk+aWMERe8R0VTxvNBXKbCY+zRMAAAAAACJRINqgGvK1f1IpAc/1nKQ2DiPaSkHWu0qknqmPtn2NIRvHAUBlGhmFAycyVnnN7vFCI4h26fRS3xbQDzqwVtbUudst1Zxiv/5WsajTzq/0QGcbyJCoG/eL7/obfCqbl+idWInBAUDzUTcJn5wvzDEoP7vkiVTKu4LKMPRyi800488/ygMl6nsKKgmnizlM+0cky4CPYcdWt8aXt4qE/Wl9O+8jENqqAAAAAA==");
        chainAnchor.setAnchorTxId("6ac3a0f55ab3370e689b4dffee511936a9550782be7a2d1eaf9566f39442a0e9");
        chainAnchor.setAnchorBlockHash("00000000000000033049394c9a659c276dd09ebd197803ebac5ca2691df81ea9");
        chainAnchor.setAnchorOutpoint("6ac3a0f55ab3370e689b4dffee511936a9550782be7a2d1eaf9566f39442a0e9:1");
        chainAnchor.setInternalKey("A+riUffgTwRYogPwsYZf9qSPZu7VB09YbeMsTCO0eWIk");
        asset.setChainAnchor(chainAnchor);

        asset.setPrevWitnesses(Collections.emptyList());

        decodedProof.setTxMerkleProof("B4WGOIm06NBFx7jkGoxP2hfVp/npBoT99oeOOr2DShfGjGvX13J3mw9bMkLPbe4lHetJ/pwnnY1t+ZTD9Z3Fp8LS8WQioOgFhuZMDFFVpkAUJ3sWLX/PNArkawLbYitBfXNJBaHOgK9Ydt4lPyWl+ggcdZv6HPoPcWZEfo7MkGLbaepIee3Lt90lJLxKLD50EwXkjvtE5AcLHTc+v4RNw3aBE4FYQhF1lPIpqaKkJ2C2i+l6UJTqXIFZByqnfgVWtKm7KIaL5WUd0hxpJoA2dyJkJP04S8N3wKBxMRuWgTr7dA==");
        decodedProof.setInclusionProof("AAQAAAABASED6uJR9+BPBFiiA/Cxhl/2pI9m7tUHT1ht4yxMI7R5YiQCdABJAAEAASA+BUUypqhp514b3ool55RBd9ZIoDT+XBSKt/d0NTjMngIiAAD//////////////////////////////////////////wEnAAEAASIAAP//////////////////////////////////////////");
        decodedProof.setExclusionProofs(Stream.of("AAQAAAAAASECeho1fNYL7NXQ+IKJuwa/2zd9tIPb31XeHDgKz/vCmG4CnABxAAEAASA+BUUypqhp514b3ool55RBd9ZIoDT+XBSKt/d0NTjMngJKAAEIpOtKphgrhdq47EEDOFzwLpVMQv9n8DzLxyw3rtmOiAAAAAABQG82/////////////////////////////////////////98BJwABAAEiAAD//////////////////////////////////////////w==",
                        "AAQAAAACASECGwuFayXG2CoadHsNYjzsXwL/jtbbi+CAcLqeqcGvY2wDAwIBAQ==")
                .collect(Collectors.toList()));
        return decodedProofResponse;
    }

}
