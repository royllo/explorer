package org.royllo.explorer.core.test.integration.tapd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("TAPD proof service test")
public class TapdServiceTest extends BaseTest {

    @Autowired
    private TapdService tapdService;

    @Test
    @DisplayName("Calling decode() on TAPD")
    @SuppressWarnings("SpellCheckingInspection")
    public void decodeTest() {
        // TODO Review this test
        final DecodedProofResponse response = tapdService.decode("0000000001fd051400241101b19e0dd323f86840976a161bf807fd1dbc30e793bb74aff307e1e9edfa570000000101500000d6224f34b13ded7327694942bf00a9060938f759a544e98e7b0139120000000000002d2c483ab11c46d36e302cae0e225c790ce201449138186b3dc7e83e72652a594816a764ffff001d274d807202fd0189020000000001021101b19e0dd323f86840976a161bf807fd1dbc30e793bb74aff307e1e9edfa570100000000ffffffffdc62e81401abfec546a45213c65c9910466f06ea1b831b017c13edfa71d775b10200000000ffffffff02e80300000000000022512040b3ddefc0df09b89fd1379a3bd246bf2827ed461506a5899dc230324d0aa158bc05000000000000225120d7ab0deb6185088278a66f5599be41510db531fd7f2df39e3b8558e2d2b65b0602473044022023f92ca5b7289d5cfb2ae415b91efa8bb9758d97b78b8ad17c81ce43fa9d262a02205c790874cbd4dae139f1d3e2b029bb97c4bf13bff0bc80127dacf9e917a37ed5012102cf948a447b6b49ccf83754b2455b7c6f77a9daeb1bb192f17ae34d3f52182a1a024630430220698a8f0543c97775ece1ad26a63f99d837ff9fd3b136e5c43cc984c37a75b3dd021f4d183f9811ac7a7cde904c795f68f96bda845efb5493a95582b60a93ca55c201210305c9ac7185c67621b7534e700d9592863f07ae5b9add38da9576e4a06a66c1000000000003c206e47b7ffac0a46c638ebd66bd72c07c93059cb9dcacd5474b4d29bb0f1cc661ef12dc3d91a992c4d4800fadc4593e665783966e9ea18257e7d1d187ee3f70a3a53f660f84796da9dccf2521f2bc0caddb627789062cca22e8de926f1e2327370f38e8899c9da3fcc32ccbccb65ccac88e253398c0d447323a456813bf08f7b22bd1af29b3bec54a12010c5bcd4a545e0bc461507236adc7fef624b70042f53a0ebf264c2dfde363f7e68820f57767962e412238ce2ee06ace92795f496ee1c5eb2e04fd015600010001541101b19e0dd323f86840976a161bf807fd1dbc30e793bb74aff307e1e9edfa57000000010a726f796c6c6f436f696e0c482467dfb29000804e044c4c6044b487c0280082002d4611a8ba7f22703d6300000000000201000303fd03e70669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d2110a6102260e9ffbe7fdabe746b4ba4c3b86c8f237d7946f116949e93db77d5e0357c13dd0d86f5c646fcca990b8ceaf7a480762c92900351ae84e8135740fa92a66c8c0a4e509c8d10df8913924a583d74e9cf25ccd56eb2552c3b92a8c3c9ebe8cca97059f0004000000000121026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae60274004900010001209e4f59a9e6c363a266472043f21f2f11b0b7f206f2abd8760d555832f16cf63f02220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0630012e00040000000101210264e26d9bcccf4e442e08a8bab1fc2a0b64fcb83b34cbba1fe4b0404c3fd110980303020101081c0001000117526f796c6c6f20636f696e20666f7220746573746e657401bc6c08537fd418062422c4c2803bca5cc7edba3fd5693f5d3f6370a45d0d7d").block();

        // Testing all the value from the response.
        assertNotNull(response);
        assertNotNull(response.getDecodedProof());
        assertEquals(1, response.getDecodedProof().getProofAtDepth());
        assertEquals(2, response.getDecodedProof().getNumberOfProofs());
        // Asset.
        DecodedProofResponse.DecodedProof.Asset asset = response.getDecodedProof().getAsset();
        assertEquals(0, asset.getVersion());
        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = asset.getAssetGenesis();
        assertEquals(0, response.getDecodedProof().getAsset().getVersion());
        assertEquals("2b572baa90ab27552c7577a4992b048689af30b8549379be83a5b576e3ebc210:0", assetGenesis.getGenesisPoint());
        assertEquals("habibtaro_remote", assetGenesis.getName());
        assertEquals("aGFiaWJ0YXJvLmNvbV9yZW1vdGU=", assetGenesis.getMeta());
        assertEquals("PgVFMqaoaedeG96KJeeUQXfWSKA0/lwUirf3dDU4zJ4=", assetGenesis.getAssetId());
        assertEquals(0, assetGenesis.getOutputIndex());
        assertEquals("EMLr43a1pYO+eZNUuDCviYYEK5mkd3UsVSerkKorVysAAAAAEGhhYmlidGFyb19yZW1vdGUUaGFiaWJ0YXJvLmNvbV9yZW1vdGUAAAAAAA==", assetGenesis.getGenesisBootstrapInfo());
        assertEquals(0, assetGenesis.getVersion());
        // End asset genesis.
        assertEquals("NORMAL", asset.getAssetType());
        assertEquals(0, asset.getAmount().compareTo(BigInteger.valueOf(10)));
        assertEquals(0, asset.getLockTime());
        assertEquals(0, asset.getRelativeLockTime());
        assertEquals(0, asset.getScriptVersion());
        assertEquals("Ao9XAROi+fc/XqUXTlcCVUi72pjQMj8F9bVr5vvjQMTu", asset.getScriptKey());
        // Chain anchor.
        DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchor = asset.getChainAnchor();
        assertEquals("AgAAAAABAotUvaRjib4LIAdu+JHZhfKL09chEbykNqv8Wtf4ZjuFAQAAAAD/////i1S9pGOJvgsgB274kdmF8ovT1yERvKQ2q/xa1/hmO4UAAAAAAAAAAAAD6AMAAAAAAAAiUSA9IQPuVFvpjbeV1z4R/YY0s8C9ddkTNAJSC2HoHyHIregDAAAAAAAAIlEgUai+/8Zd3X7xelRG7HWkqk+aWMERe8R0VTxvNBXKbCY+zRMAAAAAACJRINqgGvK1f1IpAc/1nKQ2DiPaSkHWu0qknqmPtn2NIRvHAUBlGhmFAycyVnnN7vFCI4h26fRS3xbQDzqwVtbUudst1Zxiv/5WsajTzq/0QGcbyJCoG/eL7/obfCqbl+idWInBAUDzUTcJn5wvzDEoP7vkiVTKu4LKMPRyi800488/ygMl6nsKKgmnizlM+0cky4CPYcdWt8aXt4qE/Wl9O+8jENqqAAAAAA==", chainAnchor.getAnchorTx());
        assertEquals("6ac3a0f55ab3370e689b4dffee511936a9550782be7a2d1eaf9566f39442a0e9", chainAnchor.getAnchorTxId());
        assertEquals("00000000000000033049394c9a659c276dd09ebd197803ebac5ca2691df81ea9", chainAnchor.getAnchorBlockHash());
        assertEquals("6ac3a0f55ab3370e689b4dffee511936a9550782be7a2d1eaf9566f39442a0e9:1", chainAnchor.getAnchorOutpoint());
        assertEquals("A+riUffgTwRYogPwsYZf9qSPZu7VB09YbeMsTCO0eWIk", chainAnchor.getInternalKey());
        assertEquals(0, asset.getPrevWitnesses().size());
        assertEquals("B4WGOIm06NBFx7jkGoxP2hfVp/npBoT99oeOOr2DShfGjGvX13J3mw9bMkLPbe4lHetJ/pwnnY1t+ZTD9Z3Fp8LS8WQioOgFhuZMDFFVpkAUJ3sWLX/PNArkawLbYitBfXNJBaHOgK9Ydt4lPyWl+ggcdZv6HPoPcWZEfo7MkGLbaepIee3Lt90lJLxKLD50EwXkjvtE5AcLHTc+v4RNw3aBE4FYQhF1lPIpqaKkJ2C2i+l6UJTqXIFZByqnfgVWtKm7KIaL5WUd0hxpJoA2dyJkJP04S8N3wKBxMRuWgTr7dA==", response.getDecodedProof().getTxMerkleProof());
        assertEquals("AAQAAAABASED6uJR9+BPBFiiA/Cxhl/2pI9m7tUHT1ht4yxMI7R5YiQCdABJAAEAASA+BUUypqhp514b3ool55RBd9ZIoDT+XBSKt/d0NTjMngIiAAD//////////////////////////////////////////wEnAAEAASIAAP//////////////////////////////////////////", response.getDecodedProof().getInclusionProof());
        assertEquals(2, response.getDecodedProof().getExclusionProofs().size());
        assertEquals("AAQAAAAAASECeho1fNYL7NXQ+IKJuwa/2zd9tIPb31XeHDgKz/vCmG4CnABxAAEAASA+BUUypqhp514b3ool55RBd9ZIoDT+XBSKt/d0NTjMngJKAAEIpOtKphgrhdq47EEDOFzwLpVMQv9n8DzLxyw3rtmOiAAAAAABQG82/////////////////////////////////////////98BJwABAAEiAAD//////////////////////////////////////////w==", response.getDecodedProof().getExclusionProofs().get(0));
        assertEquals("AAQAAAACASECGwuFayXG2CoadHsNYjzsXwL/jtbbi+CAcLqeqcGvY2wDAwIBAQ==", response.getDecodedProof().getExclusionProofs().get(1));

        // Errors.
        assertNull(response.getErrorCode());
        assertNull(response.getErrorMessage());
    }

    @Test
    @DisplayName("Calling decode() on tapd with an invalid proof")
    public void decodeErrorTest() {
        // TODO Review this test
        final DecodedProofResponse response = tapdService.decode("0000000001fd03f1002488d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac00000000015000000020dbc0a8738a77d0b1f29df61820e015784d87b13f508d07af1200000000000000e9a0e62808024419f29e12075ee7192283d45787ba4e510dcc4325c4f13e2da617252c64ffff001dee9af8c502ea0200000000010188d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac0000000000ffffffff02e80300000000000022512068b8ab655c6f3edfc0b241a3c560355146ed6bbf92514c09b10a0c86ac140e23910a000000000000160014c35088956d26c44af4c00dfc8bf10d9a4c79320e0247304402201829c56dba3c2ab1cd85de1fe3d7bfec8dffc2b2d4aac8c362cae25052a420290220506b92ca45a514a8b2daabb1eb8adf5283ab29b382d0331c9c884f8c89dd830f012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c0000000003fd0102083c7c4198be914f1cab7319592f0249d727fa87890ec4185b8b7881d777158d0af976bfe9e708afddbef3616fe086f90e0879295f231ac2c6d5be387ba682903bc9f67bd23cff72af821bc6feb5995b83b603aba9074ce1c74a4e13a45f108713102ce2cdd7590df19cc6c9b0f631097a2dedec3e30c1633300ea02e179eea39ef366c1a3711657d6302e516e3218b8de4a0089ca0f92621a8a49a8790dc9a1deba788b11327b109130ca4dede1a29d6ac6c03a86454199c5bc6e8a74d32df7f93d7b24f5dec13ec57f232b95d712d5947b04785cac15d00e4ea9ae6e0ffd9b3361cab39effb1b072f0918fd3ea9e9e0ae35ee61ed51e9d09548c619f06d4caa8c104e4000100014588d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac000000000c6d79526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000000000201000303fd03e7066901670065000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008020000092102102a7c269bf61b587c6278206d05433fa09b9fbe30d618a2cc61868622f5518f059f000400000000012102bf9d0eac1be77456ced581721d9f8837b42346173556c418794e0347e08f817102740049000100012018c4eaca8905cad3ae027494b8c70a085e9452226ddea8eacfdaf6cb88431c1202220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0ea5f7971d2fe0f27d8975f656332a5f6bf0176b998a52dedaedf2ffbaef922f").block();

        // Testing all the value from the response.
        assertNotNull(response);
        assertNotNull(response.getErrorCode());
        assertNotNull(response.getErrorMessage());
    }

}
