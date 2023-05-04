package org.royllo.explorer.core.test.integration.tarod;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;
import org.royllo.explorer.core.provider.tarod.TarodProofService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("Tarod proof service test")
public class TarodProofServiceTest extends BaseTest {

    @Autowired
    private TarodProofService tarodProofService;

    @Test
    @DisplayName("Calling decode() on Tarod")
    @SuppressWarnings("SpellCheckingInspection")
    public void decodeTest() {
        final DecodedProofResponse response = tarodProofService.decode("AAAAAAL9BDwAJBDC6+N2taWDvnmTVLgwr4mGBCuZpHd1LFUnq5CqK1crAAAAAAFQAAAAIGegXtiUZvfVqFzIXM+Ul6Ea0ocKGbVXeRYAAAAAAAAA88V9mnDK6ygsK+wQ7KfCeg/nwff6vxI6hejjG3e3ppSzbuFj//8AHfcJY/QC9wIAAAAAAQEQwuvjdrWlg755k1S4MK+JhgQrmaR3dSxVJ6uQqitXKwAAAAAA/////wLoAwAAAAAAACJRIEl5QgFinncSR5W9teZ0N9pg58KwtxVaxsqFGhe/u9T62wIUAAAAAAAiUSBCgsBJGj0pHAIH5pnVg2aT5RRCO77LppWEcSI74X/5+wJIMEUCIQD6tMoanMEyK7rpP8b2rIx087ZAwe0mVoYpuCxkAamPmwIgENrBagbUcOWF075vtCPOvlI9X57J/fvVE8P4t4czbdkBIQK+k2PkmF3Nwiw95Io50bOKY24IHopAQLzt0o3QBXxMBQAAAAAD/QECCNgtEMDuLNmpqUJZOmpfRiLqFw2aYQ4U5oWH8TweQpeRlrZDEsFy8XAbIOrDi2ZrkZxm7znVvSkgK7S/B2Ta49wxA67ssl5wpx9vdsc61OZNPi3AI0y6gS/q4+rok/ol1ad+s90SwnbVUgkJ47SxCRkj5grGRt1PrFv0Zjf+rJVpmZTmL8MZvk9dChCbc510zh+wdAan3BdudN0AOSrMQctsMb97k0nHtICgrjIRPivXUXdEUNO2SHLoqC9SsypXuDoleqRtyL1uga66yfapcEgOHBYhAKQShxShweEXXQsVb+Ok1B+O7rVz6Nqn2gbg3doES/fH/i/4zxHLLAbHZVTnBPAAAQABTxDC6+N2taWDvnmTVLgwr4mGBCuZpHd1LFUnq5CqK1crAAAAABBoYWJpYnRhcm9fcmVtb3RlFGhhYmlidGFyby5jb21fcmVtb3RlAAAAAAACAQADBf4BQG9ABmkBZwBlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAgAACSECA+AboGxeZjf10S0GaTD2D9iVIjqlz4EM++c0+e6OeHoFnwAEAAAAAAEhA30ykpyTT6FlRxaNVkG5o8GTpDZs/evCPmYMwsU4TEbsAnQASQABAAEgPgVFMqaoaedeG96KJeeUQXfWSKA0/lwUirf3dDU4zJ4CIgAA//////////////////////////////////////////8BJwABAAEiAAD//////////////////////////////////////////wYwAS4ABAAAAAEBIQLcSHLzaT/lCeYzvSz09KG7cDGpf10FayQj1Nabl6rTHwMDAgEBvBHvQ38qh6p5zALFW2Tb5hlkomhRe2JrsW+JXDHjKTv9B6MAJItUvaRjib4LIAdu+JHZhfKL09chEbykNqv8Wtf4ZjuFAAAAAAFQAABgIKp/g5866TLs7shSVp67lnMLTGj5SrWe6RQAAAAAAAAAfYgH/VE/hEuIsQEzMW3sPtBF6wySS/DXEfxSoQJXzTGljOFjQV0iGT8vm9wC/QFjAgAAAAABAotUvaRjib4LIAdu+JHZhfKL09chEbykNqv8Wtf4ZjuFAQAAAAD/////i1S9pGOJvgsgB274kdmF8ovT1yERvKQ2q/xa1/hmO4UAAAAAAAAAAAAD6AMAAAAAAAAiUSA9IQPuVFvpjbeV1z4R/YY0s8C9ddkTNAJSC2HoHyHIregDAAAAAAAAIlEgUai+/8Zd3X7xelRG7HWkqk+aWMERe8R0VTxvNBXKbCY+zRMAAAAAACJRINqgGvK1f1IpAc/1nKQ2DiPaSkHWu0qknqmPtn2NIRvHAUBlGhmFAycyVnnN7vFCI4h26fRS3xbQDzqwVtbUudst1Zxiv/5WsajTzq/0QGcbyJCoG/eL7/obfCqbl+idWInBAUDzUTcJn5wvzDEoP7vkiVTKu4LKMPRyi800488/ygMl6nsKKgmnizlM+0cky4CPYcdWt8aXt4qE/Wl9O+8jENqqAAAAAAPiB4WGOIm06NBFx7jkGoxP2hfVp/npBoT99oeOOr2DShfGjGvX13J3mw9bMkLPbe4lHetJ/pwnnY1t+ZTD9Z3Fp8LS8WQioOgFhuZMDFFVpkAUJ3sWLX/PNArkawLbYitBfXNJBaHOgK9Ydt4lPyWl+ggcdZv6HPoPcWZEfo7MkGLbaepIee3Lt90lJLxKLD50EwXkjvtE5AcLHTc+v4RNw3aBE4FYQhF1lPIpqaKkJ2C2i+l6UJTqXIFZByqnfgVWtKm7KIaL5WUd0hxpJoA2dyJkJP04S8N3wKBxMRuWgTr7dAT9AqAAAQABTxDC6+N2taWDvnmTVLgwr4mGBCuZpHd1LFUnq5CqK1crAAAAABBoYWJpYnRhcm9fcmVtb3RlFGhhYmlidGFyby5jb21fcmVtb3RlAAAAAAACAQADAQoG/QIbAf0CFwBlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAC/QGsSgAB5NRF1CXSOcmsCA7ruti701k17esd4DkHknGTvKlyybkAAAAAAUBvNv////////////////////////////////////////9//QFeAAEAAU8QwuvjdrWlg755k1S4MK+JhgQrmaR3dSxVJ6uQqitXKwAAAAAQaGFiaWJ0YXJvX3JlbW90ZRRoYWJpYnRhcm8uY29tX3JlbW90ZQAAAAAAAgEAAwX+AUBvNgatAasAZYtUvaRjib4LIAdu+JHZhfKL09chEbykNqv8Wtf4ZjuFAAAAAD4FRTKmqGnnXhveiiXnlEF31kigNP5cFIq393Q1OMyeAgPgG6BsXmY39dEtBmkw9g/YlSI6pc+BDPvnNPnujnh6AUIBQOcBOWi0inzLOOcRrYChn+G4JyWe9eJlO6s4pYa9PKXiSs9jhJn507nvN3D3am3rGN8L59gXo5Whx5noCeHzct8HKMbUVMC2KVHdr5Jiz2Vteqtd2BzaNSwvi8Lns2tfwA/RAAAAAAFAb0AIAgAACSECV+BOR3G04Fq5J3zkatO6g72pRZit4NNLYohjWD1wyyMIAgAACSECj1cBE6L59z9epRdOVwJVSLvamNAyPwX1tWvm++NAxO4FnwAEAAAAAQEhA+riUffgTwRYogPwsYZf9qSPZu7VB09YbeMsTCO0eWIkAnQASQABAAEgPgVFMqaoaedeG96KJeeUQXfWSKA0/lwUirf3dDU4zJ4CIgAA//////////////////////////////////////////8BJwABAAEiAAD//////////////////////////////////////////wb4AscABAAAAAABIQJ6GjV81gvs1dD4gom7Br/bN320g9vfVd4cOArP+8KYbgKcAHEAAQABID4FRTKmqGnnXhveiiXnlEF31kigNP5cFIq393Q1OMyeAkoAAQik60qmGCuF2rjsQQM4XPAulUxC/2fwPMvHLDeu2Y6IAAAAAAFAbzb/////////////////////////////////////////3wEnAAEAASIAAP//////////////////////////////////////////LgAEAAAAAgEhAhsLhWslxtgqGnR7DWI87F8C/47W24vggHC6nqnBr2NsAwMCAQEHnwAEAAAAAAEhAnoaNXzWC+zV0PiCibsGv9s3fbSD299V3hw4Cs/7wphuAnQASQABAAEgPgVFMqaoaedeG96KJeeUQXfWSKA0/lwUirf3dDU4zJ4CIgAA//////////////////////////////////////////8BJwABAAEiAAD//////////////////////////////////////////6NumG4nl1ZBFr46o4uvgs0ocbYbB1NTHzX7mTrGcqqx",
                0).block();

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
        assertNull(asset.getAssetGroup());
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

    @Disabled
    @Test
    @DisplayName("Calling decode() on Tarod with an invalid proof")
    public void decodeErrorTest() {
        final DecodedProofResponse response = tarodProofService.decode("0000000001fd03f1002488d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac00000000015000000020dbc0a8738a77d0b1f29df61820e015784d87b13f508d07af1200000000000000e9a0e62808024419f29e12075ee7192283d45787ba4e510dcc4325c4f13e2da617252c64ffff001dee9af8c502ea0200000000010188d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac0000000000ffffffff02e80300000000000022512068b8ab655c6f3edfc0b241a3c560355146ed6bbf92514c09b10a0c86ac140e23910a000000000000160014c35088956d26c44af4c00dfc8bf10d9a4c79320e0247304402201829c56dba3c2ab1cd85de1fe3d7bfec8dffc2b2d4aac8c362cae25052a420290220506b92ca45a514a8b2daabb1eb8adf5283ab29b382d0331c9c884f8c89dd830f012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c0000000003fd0102083c7c4198be914f1cab7319592f0249d727fa87890ec4185b8b7881d777158d0af976bfe9e708afddbef3616fe086f90e0879295f231ac2c6d5be387ba682903bc9f67bd23cff72af821bc6feb5995b83b603aba9074ce1c74a4e13a45f108713102ce2cdd7590df19cc6c9b0f631097a2dedec3e30c1633300ea02e179eea39ef366c1a3711657d6302e516e3218b8de4a0089ca0f92621a8a49a8790dc9a1deba788b11327b109130ca4dede1a29d6ac6c03a86454199c5bc6e8a74d32df7f93d7b24f5dec13ec57f232b95d712d5947b04785cac15d00e4ea9ae6e0ffd9b3361cab39effb1b072f0918fd3ea9e9e0ae35ee61ed51e9d09548c619f06d4caa8c104e4000100014588d6b6d1f7fe8dd4134e699037b52fa7273674e3a181deacc52bdddbf62274ac000000000c6d79526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000000000201000303fd03e7066901670065000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008020000092102102a7c269bf61b587c6278206d05433fa09b9fbe30d618a2cc61868622f5518f059f000400000000012102bf9d0eac1be77456ced581721d9f8837b42346173556c418794e0347e08f817102740049000100012018c4eaca8905cad3ae027494b8c70a085e9452226ddea8eacfdaf6cb88431c1202220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0ea5f7971d2fe0f27d8975f656332a5f6bf0176b998a52dedaedf2ffbaef922f", 0).block();

        // Testing all the value from the response.
        assertNotNull(response);
        assertNotNull(response.getErrorCode());
        assertNotNull(response.getErrorMessage());
    }

}
