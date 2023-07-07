package org.royllo.explorer.batch.test.util.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.royllo.explorer.batch.test.util.BaseTest;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.provider.tapd.UniverseLeavesResponse;
import org.royllo.explorer.core.provider.tapd.UniverseRootsResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link TapdService} mock.
 */
@Profile("tapdProofServiceMock")
@Configuration
public class TAPDServiceMock extends BaseTest {

    @Bean
    @Primary
    public TapdService tapdProofService() throws IOException {
        final TapdService mockedService = Mockito.mock(TapdService.class);

        // =============================================================================================================
        // Mock for mockedService.decode()
        // Exception test.
        Mockito.when(mockedService.decode("TIMEOUT_ERROR")).thenThrow(new RuntimeException("Time out error"))
                .thenReturn(Mono.just(getActiveRoylloCoinProof1()));

        // Non-existing proof.
        Mockito.when(mockedService.decode("INVALID_PROOF")).thenReturn(Mono.just(getError()));

        // My Royllo coin.
        Mockito.when(mockedService.decode(MY_ROYLLO_COIN_RAW_PROOF)).thenReturn(Mono.just(getMyRoylloCoin()));

        // Unknown Royllo coin.
        Mockito.when(mockedService.decode(UNKNOWN_ROYLLO_COIN_RAW_PROOF)).thenReturn(Mono.just(getUnknownRoylloCoin()));

        // Active Royllo coin.
        Mockito.when(mockedService.decode(ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF)).thenReturn(Mono.just(getActiveRoylloCoinProof1()));
        Mockito.when(mockedService.decode(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF)).thenReturn(Mono.just(getActiveRoylloCoinProof2Index0()));
        Mockito.when(mockedService.decode(ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF)).thenReturn(Mono.just(getActiveRoylloCoinProof2Index1()));
        Mockito.when(mockedService.decode(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF)).thenReturn(Mono.just(getActiveRoylloCoinProof3Index0()));
        Mockito.when(mockedService.decode(ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF)).thenReturn(Mono.just(getActiveRoylloCoinProof3Index1()));

        // =============================================================================================================
        // Mock for universes.

        // - 1.1.1.1: Server is responding.
        UniverseRootsResponse universeRootsResponse = new UniverseRootsResponse();
        UniverseRootsResponse.UniverseRoot universeRoot = new UniverseRootsResponse.UniverseRoot();
        UniverseRootsResponse.ID id = new UniverseRootsResponse.ID();
        id.setAssetId("asset1");
        universeRoot.setId(id);
        Map<String, UniverseRootsResponse.UniverseRoot> map = new HashMap<>();
        map.put("asset1", universeRoot);
        universeRootsResponse.setUniverseRoots(map);
        Mockito.when(mockedService.getUniverseRoots("1.1.1.1:8080")).thenReturn(Mono.just(universeRootsResponse));

        // - 1.1.1.2: Error code.
        UniverseRootsResponse universeRootsResponse2 = new UniverseRootsResponse();
        universeRootsResponse2.setErrorCode(1L);
        universeRootsResponse2.setErrorMessage("Mocked error message");
        Mockito.when(mockedService.getUniverseRoots("1.1.1.2:8080")).thenReturn(Mono.just(universeRootsResponse2));

        // - 1.1.1.3: Exception.
        Mockito.when(mockedService.getUniverseRoots("1.1.1.3:8080")).thenThrow(new RuntimeException("Mocked exception"));

        // =============================================================================================================
        // testnet.universe.lightning.finance
        final ClassPathResource classPathResourceUniverseRoots1 = new ClassPathResource("tapd/universe-roots-response-for-testnet-universe-lightning-finance.json");
        UniverseRootsResponse lightningResponse = new ObjectMapper().readValue(classPathResourceUniverseRoots1.getInputStream(), UniverseRootsResponse.class);
        Mockito.when(mockedService.getUniverseRoots("testnet.universe.lightning.finance")).thenReturn(Mono.just(lightningResponse));
        // testnet.universe.lightning.finance:asset_id_1
        final ClassPathResource classPathResourceUniverseLeaveAssetId1 = new ClassPathResource("tapd/universe-leaves-asset-id-1-for-testnet-universe-lightning-finance.json");
        UniverseLeavesResponse asset1Response = new ObjectMapper().readValue(classPathResourceUniverseLeaveAssetId1.getInputStream(), UniverseLeavesResponse.class);
        Mockito.when(mockedService.getUniverseLeaves("testnet.universe.lightning.finance", "asset_id_1")).thenReturn(Mono.just(asset1Response));
        // testnet.universe.lightning.finance:asset_id_2
        final ClassPathResource classPathResourceUniverseLeaveAssetId2 = new ClassPathResource("tapd/universe-leaves-asset-id-2-for-testnet-universe-lightning-finance.json");
        UniverseLeavesResponse asset2Response = new ObjectMapper().readValue(classPathResourceUniverseLeaveAssetId2.getInputStream(), UniverseLeavesResponse.class);
        Mockito.when(mockedService.getUniverseLeaves("testnet.universe.lightning.finance", "asset_id_2")).thenReturn(Mono.just(asset2Response));
        // testnet.universe.lightning.finance:asset_id_3
        final ClassPathResource classPathResourceUniverseLeaveAssetId3 = new ClassPathResource("tapd/universe-leaves-asset-id-3-for-testnet-universe-lightning-finance.json");
        UniverseLeavesResponse asset3Response = new ObjectMapper().readValue(classPathResourceUniverseLeaveAssetId3.getInputStream(), UniverseLeavesResponse.class);
        Mockito.when(mockedService.getUniverseLeaves("testnet.universe.lightning.finance", "asset_id_3")).thenReturn(Mono.just(asset3Response));

        // =============================================================================================================
        // testnet2.universe.lightning.finance
        final ClassPathResource classPathResourceUniverseRoots2 = new ClassPathResource("tapd/universe-roots-response-for-testnet2-universe-lightning-finance.json");
        UniverseRootsResponse lightning2Response = new ObjectMapper().readValue(classPathResourceUniverseRoots2.getInputStream(), UniverseRootsResponse.class);
        Mockito.when(mockedService.getUniverseRoots("testnet2.universe.lightning.finance")).thenReturn(Mono.just(lightning2Response));
        // testnet2.universe.lightning.finance:asset_id_4
        final ClassPathResource classPathResourceUniverseLeaveAssetId4 = new ClassPathResource("tapd/universe-leaves-asset-id-4-for-testnet2-universe-lightning-finance.json");
        UniverseLeavesResponse asset4Response = new ObjectMapper().readValue(classPathResourceUniverseLeaveAssetId4.getInputStream(), UniverseLeavesResponse.class);
        Mockito.when(mockedService.getUniverseLeaves("testnet2.universe.lightning.finance", "asset_id_4")).thenReturn(Mono.just(asset4Response));
        // testnet2.universe.lightning.finance:asset_id_1
        final ClassPathResource classPathResourceUniverseLeaveAssetId1Bis = new ClassPathResource("tapd/universe-leaves-asset-id-1-for-testnet2-universe-lightning-finance.json");
        UniverseLeavesResponse asset1BisResponse = new ObjectMapper().readValue(classPathResourceUniverseLeaveAssetId1Bis.getInputStream(), UniverseLeavesResponse.class);
        Mockito.when(mockedService.getUniverseLeaves("testnet2.universe.lightning.finance", "asset_id_1")).thenReturn(Mono.just(asset1BisResponse));
        // testnet2.universe.lightning.finance:asset_id_1
        final ClassPathResource classPathResourceUniverseLeaveAssetId5 = new ClassPathResource("tapd/universe-leaves-asset-id-5-for-testnet2-universe-lightning-finance.json");
        UniverseLeavesResponse asset5Response = new ObjectMapper().readValue(classPathResourceUniverseLeaveAssetId5.getInputStream(), UniverseLeavesResponse.class);
        Mockito.when(mockedService.getUniverseLeaves("testnet2.universe.lightning.finance", "asset_id_5")).thenReturn(Mono.just(asset5Response));

        // =============================================================================================================
        // Empty response for lastSync field tests.
        final ClassPathResource classPathResourceUniverseEmpty = new ClassPathResource("tapd/universe-roots-response-empty.json");
        UniverseRootsResponse emptyLightningResponse = new ObjectMapper().readValue(classPathResourceUniverseEmpty.getInputStream(), UniverseRootsResponse.class);
        Mockito.when(mockedService.getUniverseRoots("server1")).thenReturn(Mono.just(emptyLightningResponse));
        Mockito.when(mockedService.getUniverseRoots("server2")).thenReturn(Mono.just(emptyLightningResponse));
        Mockito.when(mockedService.getUniverseRoots("server3")).thenReturn(Mono.just(emptyLightningResponse));
        Mockito.when(mockedService.getUniverseRoots("server4")).thenReturn(Mono.just(emptyLightningResponse));
        Mockito.when(mockedService.getUniverseRoots("server5")).thenReturn(Mono.just(emptyLightningResponse));
        Mockito.when(mockedService.getUniverseRoots("server6")).thenReturn(Mono.just(emptyLightningResponse));

        return mockedService;
    }

    /**
     * Returns decoded proof error.
     *
     * @return decoded proof error.
     */
    private DecodedProofResponse getError() {
        DecodedProofResponse decodedProofResponse = new DecodedProofResponse();
        decodedProofResponse.setErrorCode(1L);
        decodedProofResponse.setErrorMessage("An error occurred while decoding");
        return decodedProofResponse;
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
        decodedProof.setProofAtDepth(0);
        decodedProof.setNumberOfProofs(1);

        // Asset.
        DecodedProofResponse.DecodedProof.Asset asset = new DecodedProofResponse.DecodedProof.Asset();
        asset.setVersion(MY_ROYLLO_COIN_VERSION);

        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = new DecodedProofResponse.DecodedProof.Asset.AssetGenesis();
        assetGenesis.setGenesisPoint(MY_ROYLLO_COIN_GENESIS_POINT_TXID + ":" + MY_ROYLLO_COIN_GENESIS_POINT_VOUT);
        assetGenesis.setName(MY_ROYLLO_COIN_NAME);
        assetGenesis.setMetaDataHash(MY_ROYLLO_COIN_META);
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
        decodedProof.setProofAtDepth(0);
        decodedProof.setNumberOfProofs(1);

        // Asset.
        DecodedProofResponse.DecodedProof.Asset asset = new DecodedProofResponse.DecodedProof.Asset();
        asset.setVersion(UNKNOWN_ROYLLO_COIN_VERSION);

        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = new DecodedProofResponse.DecodedProof.Asset.AssetGenesis();
        assetGenesis.setGenesisPoint(UNKNOWN_ROYLLO_COIN_GENESIS_POINT_TXID + ":" + UNKNOWN_ROYLLO_COIN_GENESIS_POINT_VOUT);
        assetGenesis.setName(UNKNOWN_ROYLLO_COIN_NAME);
        assetGenesis.setMetaDataHash(UNKNOWN_ROYLLO_COIN_META);
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
     * Returns active royllo coin 1 decoded proof
     *
     * @return active royllo coin 1 decoded proof
     */
    private DecodedProofResponse getActiveRoylloCoinProof1() {
        // Decoded response.
        DecodedProofResponse decodedProofResponse = new DecodedProofResponse();

        // Decoded proof.
        DecodedProofResponse.DecodedProof decodedProof = new DecodedProofResponse.DecodedProof();
        decodedProof.setProofAtDepth(0);
        decodedProof.setNumberOfProofs(1);

        // Asset.
        DecodedProofResponse.DecodedProof.Asset asset = new DecodedProofResponse.DecodedProof.Asset();
        asset.setVersion(0);

        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = new DecodedProofResponse.DecodedProof.Asset.AssetGenesis();
        assetGenesis.setGenesisPoint("db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839:1");
        assetGenesis.setName("activeRoylloCoin");
        assetGenesis.setMetaDataHash("5573656420627920526f796c6c6f=");
        assetGenesis.setAssetId("1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413");
        assetGenesis.setOutputIndex(1L);
        assetGenesis.setGenesisBootstrapInfo("39481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000100");
        assetGenesis.setVersion(0);
        asset.setAssetGenesis(assetGenesis);

        asset.setAssetType("NORMAL");
        asset.setAmount(BigInteger.valueOf(1003));
        asset.setLockTime(0);
        asset.setRelativeLockTime(0);
        asset.setScriptVersion(0);
        asset.setScriptKey("024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc");

        // Chain anchor.
        DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchor = new DecodedProofResponse.DecodedProof.Asset.ChainAnchor();
        chainAnchor.setAnchorTx("0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d600000000");
        chainAnchor.setAnchorTxId("60b11c06d29ad10a66082953e37010090b39bc7541c321066a9e8bf7507abdd7");
        chainAnchor.setAnchorBlockHash("000000000000002366fc3e4cc4074a94529e09858b6585004a0056e47e08c0c9");
        chainAnchor.setAnchorOutpoint("60b11c06d29ad10a66082953e37010090b39bc7541c321066a9e8bf7507abdd7");
        chainAnchor.setInternalKey("03bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e962401429");
        asset.setChainAnchor(chainAnchor);

        asset.setPrevWitnesses(Collections.emptyList());

        decodedProof.setTxMerkleProof("05e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c761511");
        decodedProof.setInclusionProof("000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        decodedProof.setExclusionProofs(Collections.emptyList());
        decodedProofResponse.setDecodedProof(decodedProof);

        decodedProof.setAsset(asset);

        return decodedProofResponse;
    }

    /**
     * Returns active royllo coin 2 decoded proof - Index 0.
     *
     * @return active royllo coin 2 decoded proof
     */
    private DecodedProofResponse getActiveRoylloCoinProof2Index0() {
        // Decoded response.
        DecodedProofResponse decodedProofResponse = new DecodedProofResponse();

        // Decoded proof.
        DecodedProofResponse.DecodedProof decodedProof = new DecodedProofResponse.DecodedProof();
        decodedProof.setProofAtDepth(0);
        decodedProof.setNumberOfProofs(2);

        // Asset.
        DecodedProofResponse.DecodedProof.Asset asset = new DecodedProofResponse.DecodedProof.Asset();
        asset.setVersion(0);

        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = new DecodedProofResponse.DecodedProof.Asset.AssetGenesis();
        assetGenesis.setGenesisPoint("db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839:1");
        assetGenesis.setName("activeRoylloCoin");
        assetGenesis.setMetaDataHash("5573656420627920526f796c6c6f=");
        assetGenesis.setAssetId("1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413");
        assetGenesis.setOutputIndex(1L);
        assetGenesis.setGenesisBootstrapInfo("39481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000100");
        assetGenesis.setVersion(0);
        asset.setAssetGenesis(assetGenesis);

        asset.setAssetType("NORMAL");
        asset.setAmount(BigInteger.valueOf(1003));
        asset.setLockTime(0);
        asset.setRelativeLockTime(0);
        asset.setScriptVersion(0);
        asset.setScriptKey("024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc");

        // Chain anchor.
        DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchor = new DecodedProofResponse.DecodedProof.Asset.ChainAnchor();
        chainAnchor.setAnchorTx("0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d600000000");
        chainAnchor.setAnchorTxId("60b11c06d29ad10a66082953e37010090b39bc7541c321066a9e8bf7507abdd7");
        chainAnchor.setAnchorBlockHash("000000000000002366fc3e4cc4074a94529e09858b6585004a0056e47e08c0c9");
        chainAnchor.setAnchorOutpoint("60b11c06d29ad10a66082953e37010090b39bc7541c321066a9e8bf7507abdd7");
        chainAnchor.setInternalKey("03bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e962401429");
        asset.setChainAnchor(chainAnchor);

        asset.setPrevWitnesses(Collections.emptyList());

        decodedProof.setTxMerkleProof("05e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c761511");
        decodedProof.setInclusionProof("000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        decodedProof.setExclusionProofs(Stream.of("", "").collect(Collectors.toList()));
        decodedProofResponse.setDecodedProof(decodedProof);

        decodedProof.setAsset(asset);

        return decodedProofResponse;
    }

    /**
     * Returns active royllo coin 2 decoded proof - Index 1.
     *
     * @return active royllo coin 2 decoded proof
     */
    private DecodedProofResponse getActiveRoylloCoinProof2Index1() {
        // Decoded response.
        DecodedProofResponse decodedProofResponse = new DecodedProofResponse();

        // Decoded proof.
        DecodedProofResponse.DecodedProof decodedProof = new DecodedProofResponse.DecodedProof();
        decodedProof.setProofAtDepth(1);
        decodedProof.setNumberOfProofs(2);

        // Asset.
        DecodedProofResponse.DecodedProof.Asset asset = new DecodedProofResponse.DecodedProof.Asset();
        asset.setVersion(0);

        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = new DecodedProofResponse.DecodedProof.Asset.AssetGenesis();
        assetGenesis.setGenesisPoint("db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839:1");
        assetGenesis.setName("activeRoylloCoin");
        assetGenesis.setMetaDataHash("5573656420627920526f796c6c6f=");
        assetGenesis.setAssetId("1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413");
        assetGenesis.setOutputIndex(1L);
        assetGenesis.setGenesisBootstrapInfo("39481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000100");
        assetGenesis.setVersion(0);
        asset.setAssetGenesis(assetGenesis);

        asset.setAssetType("NORMAL");
        asset.setAmount(BigInteger.valueOf(1003));
        asset.setLockTime(0);
        asset.setRelativeLockTime(0);
        asset.setScriptVersion(0);
        asset.setScriptKey("024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc");

        // Chain anchor.
        DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchor = new DecodedProofResponse.DecodedProof.Asset.ChainAnchor();
        chainAnchor.setAnchorTx("020000000001024186b51fb5050ce20332a18c940934faaf7b7f80874a9d9889b456a8927a20050200000000ffffffffd7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb16001000000000000000003e8030000000000002251207d84b124b559f1a0ab396cec2677d5a27a140caaef680cfdbecd7a16a4593762e8030000000000002251209588124b01b2a1f00db66c22b3c5560d58a7f6f8c09a98878723e87ee6eb10a3c90c0000000000001600148cb54fe42666c5e008316c342aa58c0d803392ca0247304402203b09ac49a17faf2c1bed11faccba212cfcfb01eb962656b9fd28f4a60fdc81980220561c9053d536e8cdcb771c13ba5957497e421c5ac6456b0c5be76ef908fc60c8012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c01407eb8a1f6bf214a8471f72a894cc99e6fe756b8f2258a6c786cb825767fc2762d2e1320da2eecabaa92872fa03d584d52e5ff5c80056be1488f44cc0aeb50559c00000000");
        chainAnchor.setAnchorTxId("b175d771faed137c011b831bea066f4610995cc61352a446c5feab0114e862dc");
        chainAnchor.setAnchorBlockHash("00000000ff54cb2d9fba5814b6699b1e674857e6f7c2d0ab27559c69438e4a47");
        chainAnchor.setAnchorOutpoint("b175d771faed137c011b831bea066f4610995cc61352a446c5feab0114e862dc");
        chainAnchor.setInternalKey("02d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae");
        asset.setChainAnchor(chainAnchor);

        asset.setPrevWitnesses(Collections.emptyList());

        decodedProof.setTxMerkleProof("07938006468f02d2d507be06a19e95dcb745c0c80d2f909b699b1e9be7d51ab20f013260064752b181c2b7211d73f34b6a898c7728eed82e4fa837d3032a0f9f940b660b9ab0110750f38696f8afb8c9e1efe82f134bb264ccb624d2a89afde4d9eb44c9b74161a524df3f8d0cee247be9984f6694ca31766d2a12470bfc4e0b5c33b2659a8b50f188deb330af74218788c0717f66f4f56832b2ec568e571e97dd0fc080ed0b6465878f8547163ddf71a6825290b08846484fab9ab9f84895feedd0ff6506a12b5c5e718cd7ee09f8474c852587b7a6c693819fd4add29752f67963");
        decodedProof.setInclusionProof("000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae0274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        decodedProof.setExclusionProofs(Stream.of("0004000000010121030411db4d023a8d55607fedc562d519b1854af7752d9e1f00279213380d439e88029c007100010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024a000151f716888109ae4abf80fa5120d1eaca3e36ec8a2a45849e573001373de715170000000000000064ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffbf012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff").collect(Collectors.toList()));
        return decodedProofResponse;
    }

    /**
     * Returns active royllo coin 3 decoded proof - Index 0.
     *
     * @return active royllo coin 3 decoded proof
     */
    private DecodedProofResponse getActiveRoylloCoinProof3Index0() {
        // Decoded response.
        DecodedProofResponse decodedProofResponse = new DecodedProofResponse();

        // Decoded proof.
        DecodedProofResponse.DecodedProof decodedProof = new DecodedProofResponse.DecodedProof();
        decodedProof.setProofAtDepth(0);
        decodedProof.setNumberOfProofs(1);

        // Asset.
        DecodedProofResponse.DecodedProof.Asset asset = new DecodedProofResponse.DecodedProof.Asset();
        asset.setVersion(0);

        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = new DecodedProofResponse.DecodedProof.Asset.AssetGenesis();
        assetGenesis.setGenesisPoint("db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839:1");
        assetGenesis.setName("activeRoylloCoin");
        assetGenesis.setMetaDataHash("5573656420627920526f796c6c6f=");
        assetGenesis.setAssetId("1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413");
        assetGenesis.setOutputIndex(1L);
        assetGenesis.setGenesisBootstrapInfo("39481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000100");
        assetGenesis.setVersion(0);
        asset.setAssetGenesis(assetGenesis);

        asset.setAssetType("NORMAL");
        asset.setAmount(BigInteger.valueOf(1003));
        asset.setLockTime(0);
        asset.setRelativeLockTime(0);
        asset.setScriptVersion(0);
        asset.setScriptKey("024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc");

        // Chain anchor.
        DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchor = new DecodedProofResponse.DecodedProof.Asset.ChainAnchor();
        chainAnchor.setAnchorTx("0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d600000000");
        chainAnchor.setAnchorTxId("60b11c06d29ad10a66082953e37010090b39bc7541c321066a9e8bf7507abdd7");
        chainAnchor.setAnchorBlockHash("000000000000002366fc3e4cc4074a94529e09858b6585004a0056e47e08c0c9");
        chainAnchor.setAnchorOutpoint("60b11c06d29ad10a66082953e37010090b39bc7541c321066a9e8bf7507abdd7");
        chainAnchor.setInternalKey("03bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e962401429");
        asset.setChainAnchor(chainAnchor);

        asset.setPrevWitnesses(Collections.emptyList());

        decodedProof.setTxMerkleProof("05e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c761511");
        decodedProof.setInclusionProof("000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        decodedProof.setExclusionProofs(Collections.emptyList());
        decodedProofResponse.setDecodedProof(decodedProof);

        decodedProof.setAsset(asset);

        return decodedProofResponse;
    }

    /**
     * Returns active royllo coin 3 decoded proof - Index 1.
     *
     * @return active royllo coin 3 decoded proof
     */
    private DecodedProofResponse getActiveRoylloCoinProof3Index1() {
        // Decoded response.
        DecodedProofResponse decodedProofResponse = new DecodedProofResponse();

        // Decoded proof.
        DecodedProofResponse.DecodedProof decodedProof = new DecodedProofResponse.DecodedProof();
        decodedProof.setProofAtDepth(0);
        decodedProof.setNumberOfProofs(1);

        // Asset.
        DecodedProofResponse.DecodedProof.Asset asset = new DecodedProofResponse.DecodedProof.Asset();
        asset.setVersion(0);

        // Asset genesis.
        DecodedProofResponse.DecodedProof.Asset.AssetGenesis assetGenesis = new DecodedProofResponse.DecodedProof.Asset.AssetGenesis();
        assetGenesis.setGenesisPoint("db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839:1");
        assetGenesis.setName("activeRoylloCoin");
        assetGenesis.setMetaDataHash("5573656420627920526f796c6c6f=");
        assetGenesis.setAssetId("1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413");
        assetGenesis.setOutputIndex(1L);
        assetGenesis.setGenesisBootstrapInfo("39481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000100");
        assetGenesis.setVersion(0);
        asset.setAssetGenesis(assetGenesis);

        asset.setAssetType("NORMAL");
        asset.setAmount(BigInteger.valueOf(1003));
        asset.setLockTime(0);
        asset.setRelativeLockTime(0);
        asset.setScriptVersion(0);
        asset.setScriptKey("024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc");

        // Chain anchor.
        DecodedProofResponse.DecodedProof.Asset.ChainAnchor chainAnchor = new DecodedProofResponse.DecodedProof.Asset.ChainAnchor();
        chainAnchor.setAnchorTx("020000000001024186b51fb5050ce20332a18c940934faaf7b7f80874a9d9889b456a8927a20050200000000ffffffffd7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb16001000000000000000003e8030000000000002251207d84b124b559f1a0ab396cec2677d5a27a140caaef680cfdbecd7a16a4593762e8030000000000002251209588124b01b2a1f00db66c22b3c5560d58a7f6f8c09a98878723e87ee6eb10a3c90c0000000000001600148cb54fe42666c5e008316c342aa58c0d803392ca0247304402203b09ac49a17faf2c1bed11faccba212cfcfb01eb962656b9fd28f4a60fdc81980220561c9053d536e8cdcb771c13ba5957497e421c5ac6456b0c5be76ef908fc60c8012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c01407eb8a1f6bf214a8471f72a894cc99e6fe756b8f2258a6c786cb825767fc2762d2e1320da2eecabaa92872fa03d584d52e5ff5c80056be1488f44cc0aeb50559c00000000");
        chainAnchor.setAnchorTxId("b175d771faed137c011b831bea066f4610995cc61352a446c5feab0114e862dc");
        chainAnchor.setAnchorBlockHash("00000000ff54cb2d9fba5814b6699b1e674857e6f7c2d0ab27559c69438e4a47");
        chainAnchor.setAnchorOutpoint("b175d771faed137c011b831bea066f4610995cc61352a446c5feab0114e862dc");
        chainAnchor.setInternalKey("030411db4d023a8d55607fedc562d519b1854af7752d9e1f00279213380d439e88");
        asset.setChainAnchor(chainAnchor);

        asset.setPrevWitnesses(Collections.emptyList());

        decodedProof.setTxMerkleProof("07938006468f02d2d507be06a19e95dcb745c0c80d2f909b699b1e9be7d51ab20f013260064752b181c2b7211d73f34b6a898c7728eed82e4fa837d3032a0f9f940b660b9ab0110750f38696f8afb8c9e1efe82f134bb264ccb624d2a89afde4d9eb44c9b74161a524df3f8d0cee247be9984f6694ca31766d2a12470bfc4e0b5c33b2659a8b50f188deb330af74218788c0717f66f4f56832b2ec568e571e97dd0fc080ed0b6465878f8547163ddf71a6825290b08846484fab9ab9f84895feedd0ff6506a12b5c5e718cd7ee09f8474c852587b7a6c693819fd4add29752f67963");
        decodedProof.setInclusionProof("0004000000010121030411db4d023a8d55607fedc562d519b1854af7752d9e1f00279213380d439e880274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        decodedProof.setExclusionProofs(Stream.of("000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae029c007100010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024a00019fe1a55b2c9813569b7c7a3bf514bafc0cf43748d646a2bf39848870b06677a70000000000000387ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffbf012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff").collect(Collectors.toList()));
        decodedProofResponse.setDecodedProof(decodedProof);

        decodedProof.setAsset(asset);

        return decodedProofResponse;
    }

}
