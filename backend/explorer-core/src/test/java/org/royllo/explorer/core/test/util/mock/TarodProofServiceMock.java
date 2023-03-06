package org.royllo.explorer.core.test.util.mock;

import org.mockito.Mockito;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;
import org.royllo.explorer.core.provider.tarod.TarodProofService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link TarodProofService} mock.
 */
@Profile("tarodProofServiceMock")
@Configuration
public class TarodProofServiceMock {

    @Bean
    @Primary
    public TarodProofService tarodProofService() {
        final TarodProofService mockedService = Mockito.mock(TarodProofService.class);

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
        asset.setAmount(BigDecimal.valueOf(10));
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
        chainAnchor.setAnchorTxId("A+riUffgTwRYogPwsYZf9qSPZu7VB09YbeMsTCO0eWIk");
        asset.setChainAnchor(chainAnchor);

        asset.setPrevWitnesses(Collections.emptyList());

        decodedProof.setTxMerkleProof("B4WGOIm06NBFx7jkGoxP2hfVp/npBoT99oeOOr2DShfGjGvX13J3mw9bMkLPbe4lHetJ/pwnnY1t+ZTD9Z3Fp8LS8WQioOgFhuZMDFFVpkAUJ3sWLX/PNArkawLbYitBfXNJBaHOgK9Ydt4lPyWl+ggcdZv6HPoPcWZEfo7MkGLbaepIee3Lt90lJLxKLD50EwXkjvtE5AcLHTc+v4RNw3aBE4FYQhF1lPIpqaKkJ2C2i+l6UJTqXIFZByqnfgVWtKm7KIaL5WUd0hxpJoA2dyJkJP04S8N3wKBxMRuWgTr7dA==");
        decodedProof.setInclusionProof("AAQAAAABASED6uJR9+BPBFiiA/Cxhl/2pI9m7tUHT1ht4yxMI7R5YiQCdABJAAEAASA+BUUypqhp514b3ool55RBd9ZIoDT+XBSKt/d0NTjMngIiAAD//////////////////////////////////////////wEnAAEAASIAAP//////////////////////////////////////////");
        decodedProof.setExclusionProofs(Stream.of("AAQAAAAAASECeho1fNYL7NXQ+IKJuwa/2zd9tIPb31XeHDgKz/vCmG4CnABxAAEAASA+BUUypqhp514b3ool55RBd9ZIoDT+XBSKt/d0NTjMngJKAAEIpOtKphgrhdq47EEDOFzwLpVMQv9n8DzLxyw3rtmOiAAAAAABQG82/////////////////////////////////////////98BJwABAAEiAAD//////////////////////////////////////////w==",
                        "AAQAAAACASECGwuFayXG2CoadHsNYjzsXwL/jtbbi+CAcLqeqcGvY2wDAwIBAQ==")
                .collect(Collectors.toList()));
        return mockedService;
    }

}