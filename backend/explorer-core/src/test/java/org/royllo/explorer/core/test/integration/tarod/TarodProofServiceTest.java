package org.royllo.explorer.core.test.integration.tarod;

import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;
import org.royllo.explorer.core.provider.tarod.TarodProofService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
@SpringBootTest
@DisplayName("Tarod proof service test")
public class TarodProofServiceTest extends BaseTest {

    @Autowired
    private TarodProofService tarodProofService;
//    @Container
//    public static DockerComposeContainer environment = new DockerComposeContainer(new File("src/test/resources/docker-compose-lnd-and-taro.yml"))
//            .withOptions("--compatibility")
//            .waitingFor("taro_1", Wait.forLogMessage(".*Taro Daemon fully active!.*\\n", 1));

    @Test
    @DisplayName("Calling decode() on Tarod")
    @Ignore("Waiting for docked-compose to work with testcontainers")
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

    @Test
    @DisplayName("Calling decode() on Tarod with an invalid proof")
    @Ignore("Waiting for docked-compose to work with testcontainers")
    @SuppressWarnings("SpellCheckingInspection")
    public void decodeErrorTest() {
        final DecodedProofResponse response = tarodProofService.decode("INVALID_RAW_PROOF", 0).block();

        // Testing all the value from the response.
        assertNotNull(response);
        assertNotNull(response.getErrorCode());
        assertNotNull(response.getErrorMessage());
    }

}
