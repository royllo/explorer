package org.royllo.explorer.batch.test.util;

import org.royllo.explorer.core.util.base.Base;
import org.royllo.explorer.core.util.enums.AssetType;

import java.math.BigInteger;

/**
 * Utility classes for tests.
 */
@SuppressWarnings("SpellCheckingInspection")
public class BaseTest extends Base {

    /** Non existing database. */
    protected static final String BITCOIN_TRANSACTION_NON_EXISTING = "non_existing";
    /** txid of the first bitcoin transaction in database - one output. */
    protected static final String BITCOIN_TRANSACTION_1_TXID = "2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea";
    /** txid of the second bitcoin transaction in database - two outputs. */
    protected static final String BITCOIN_TRANSACTION_2_TXID = "46804b8a193cae200c99531f0ea90d81cc0c0e44e718b57e7b9ab5bb3926b946";
    /** txid of a third transaction not in our database but in the blockchain - three outputs. */
    protected static final String BITCOIN_TRANSACTION_3_TXID = "117ad75a79af2e7fdb2908baee9171fde4d6fb80c7322dcb895a2429f84f4d4a";
    /** txid of a taproot transaction NOT in our database, but IN the blockchain. */
    protected static final String BITCOIN_TAPROOT_TRANSACTION_2_TXID = "d61a4957e5e756a7631246b1a00d685e4854f98f8c2835bafafed8b1d1e26be5";

    /* ============================================================================================================== */
    /* My Royllo coin (living on testnet). */
    protected static final int MY_ROYLLO_COIN_VERSION = 0;
    protected static final String MY_ROYLLO_COIN_GENESIS_POINT_TXID = "0c396370aa65b5ff800ca106f85cf14352c55bd38f60a30220c68d4f4091854b";
    protected static final int MY_ROYLLO_COIN_GENESIS_POINT_VOUT = 1;
    protected static final String MY_ROYLLO_COIN_NAME = "myRoylloCoin";
    protected static final String MY_ROYLLO_COIN_META = "5573656420627920526f796c6c6f";
    protected static final String MY_ROYLLO_COIN_ASSET_ID = "692453c6d7d54f508adaf09df86573018579ac749501991f0853baedaa16faf9";
    protected static final int MY_ROYLLO_COIN_OUTPUT_INDEX = 0;
    protected static final String MY_ROYLLO_COIN_GENESIS_BOOTSTRAP_INFORMATION = "4b8591404f8dc62002a3608fd35bc55243f15cf806a10c80ffb565aa7063390c000000010c6d79526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000000";
    protected static final int MY_ROYLLO_COIN_GENESIS_VERSION = 0;
    protected static final AssetType MY_ROYLLO_COIN_ASSET_TYPE = AssetType.NORMAL;
    protected static final BigInteger MY_ROYLLO_COIN_AMOUNT = BigInteger.valueOf(1000L);
    protected static final int MY_ROYLLO_COIN_LOCK_TIME = 0;
    protected static final int MY_ROYLLO_COIN_RELATIVE_LOCK_TIME = 0;
    protected static final int MY_ROYLLO_COIN_SCRIPT_VERSION = 0;
    protected static final String MY_ROYLLO_COIN_SCRIPT_KEY = "0251e05abec04e300351523e6cbdbceadae4332896ca59dbdad6801e2fc35c1e71";
    protected static final String MY_ROYLLO_COIN_ANCHOR_TX = "020000000001014b8591404f8dc62002a3608fd35bc55243f15cf806a10c80ffb565aa7063390c0100000000ffffffff02e803000000000000225120890016973f4a07ecb7afc16f805219e3eaf1bc1d0635894c821114dbf6e2ca36bd11000000000000160014c35088956d26c44af4c00dfc8bf10d9a4c79320e0248304502210095b84eb101cd33d1b95bf1527d79653a09d75d8db259192bd9ff09a313eb9fbc02200eacd659b386ccea82a82f26458a0a6893eeb454cd50cc2c05e2ff7bf71d4b2d012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c00000000";
    protected static final String MY_ROYLLO_COIN_ANCHOR_TX_ID = "56fe33cc4fbdba40de3b710ba6b572f1e6b04ac93ab7103e586e4b897d0840c3";
    protected static final String MY_ROYLLO_COIN_ANCHOR_BLOCK_HASH = "0000000000000000000000000000000000000000000000000000000000000000";
    protected static final String MY_ROYLLO_COIN_ANCHOR_OUTPOINT = "56fe33cc4fbdba40de3b710ba6b572f1e6b04ac93ab7103e586e4b897d0840c3";
    protected static final String MY_ROYLLO_COIN_ANCHOR_INTERNAL_KEY = "03336830ceadb0fc7f67cf65b1b79d1b7dc6ecd3d13ff2af8b4ea6e7d14cee1fe6";
    protected static final String MY_ROYLLO_COIN_TX_MERKLE_PROOF = "078815347d5fdd9f01473804e9c888f1bac2bcabac72ddb35e58c8e9f4701fc324b1b6be16e92d8ce50454dba490446b93da58d4f0bea2b44911975d2d8aa13896f8fc7133fec0a0b35f938099a62d6173aa1540bd2a141fb5e49ad8409a7d4682499f594761d218af93adadc18a6eb6d6d9f72b5b3f9f62f0b8cecc9bc343bb9373cb8b9b1f270663f5502c11f63ee8b1f94426959dcc080317bccc91f0b1f704548644f7ad66cfba3e60ff7ed9bdca643903b06edafee103b54f7d95a04b797158f5223828d1f70a2be98cb7f7c8c36e48b3f35c99f004879c01add7c6f5e3225e";
    protected static final String MY_ROYLLO_COIN_INCLUSION_PROOF = "000400000000012103336830ceadb0fc7f67cf65b1b79d1b7dc6ecd3d13ff2af8b4ea6e7d14cee1fe6027400490001000120692453c6d7d54f508adaf09df86573018579ac749501991f0853baedaa16faf902220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
    protected static final String MY_ROYLLO_COIN_RAW_PROOF = "0000000001fd03d000244b8591404f8dc62002a3608fd35bc55243f15cf806a10c80ffb565aa7063390c00000001015000006020713234eeaad00b44193c578408e3757e424c99a4c05cce4c325417cc00000000a808982cc61b2133da021e78b7a6743fceb17d0af513179bd94aacdf35d6b6050d64ff63bcfe3319631abe2302eb020000000001014b8591404f8dc62002a3608fd35bc55243f15cf806a10c80ffb565aa7063390c0100000000ffffffff02e803000000000000225120890016973f4a07ecb7afc16f805219e3eaf1bc1d0635894c821114dbf6e2ca36bd11000000000000160014c35088956d26c44af4c00dfc8bf10d9a4c79320e0248304502210095b84eb101cd33d1b95bf1527d79653a09d75d8db259192bd9ff09a313eb9fbc02200eacd659b386ccea82a82f26458a0a6893eeb454cd50cc2c05e2ff7bf71d4b2d012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c0000000003e2078815347d5fdd9f01473804e9c888f1bac2bcabac72ddb35e58c8e9f4701fc324b1b6be16e92d8ce50454dba490446b93da58d4f0bea2b44911975d2d8aa13896f8fc7133fec0a0b35f938099a62d6173aa1540bd2a141fb5e49ad8409a7d4682499f594761d218af93adadc18a6eb6d6d9f72b5b3f9f62f0b8cecc9bc343bb9373cb8b9b1f270663f5502c11f63ee8b1f94426959dcc080317bccc91f0b1f704548644f7ad66cfba3e60ff7ed9bdca643903b06edafee103b54f7d95a04b797158f5223828d1f70a2be98cb7f7c8c36e48b3f35c99f004879c01add7c6f5e3225e04e400010001454b8591404f8dc62002a3608fd35bc55243f15cf806a10c80ffb565aa7063390c000000010c6d79526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000000000201000303fd03e806690167006500000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000802000009210251e05abec04e300351523e6cbdbceadae4332896ca59dbdad6801e2fc35c1e71059f000400000000012103336830ceadb0fc7f67cf65b1b79d1b7dc6ecd3d13ff2af8b4ea6e7d14cee1fe6027400490001000120692453c6d7d54f508adaf09df86573018579ac749501991f0853baedaa16faf902220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7a223a9c46816a8f7a94eca15335268b7496f0db3113c3bb5883f3a7b528d092";
    protected static final String MY_ROYLLO_COIN_PROOF_ID = "19c7a8309fad48bdbdefe6983e93e2e98aaa367aa8c31fa4caf739abac503d69";

    /* ============================================================================================================== */
    /* Unknown Royllo coin (living on testnet). */
    protected static final String UNKNOWN_ROYLLO_COIN_RAW_PROOF = "0000000001fd03d40024750e110bb206b6de993ff90cfe9936b112e42b9f5c0afe2db929c07df18572b90000000101500000002080b8233c36f756820bca031a1af25171256708b74db796fc090000000000000051ccbb70dd4f539ee43aa2497b2e574fe933cd6f8e99bdeefc7a14fb903de073c77bff63ffff001db224071402ea02000000000101750e110bb206b6de993ff90cfe9936b112e42b9f5c0afe2db929c07df18572b90100000000ffffffff02e80300000000000022512074381d0361b8bb274bbec7ae6ef19578e2c9c6b789d9f6a390920bbb6a816bc9890700000000000016001443e6aa57dd19692fdb4ed0d2fe4395f9073ac9d00247304402200312366d908deb99e44849b1ad34ad2066436a657475ff4a304dc9d6c494635b0220544a9154e234f0deb5456775b5b5e253e93edb359703705ead13434147d91abf012102b959655e63ecfe92589f45672305ab3b9842fabb893679cb49677b40965ac20c0000000003e2072ad5e87840f0f77f575d97a52ea6ac07304878255c1cd7bd43ad800cb136aa45cc4fb6dd4cc8ca29cd961f77f55996c56c3b9b7bcae769c8b9d31fb5ae0e0cd0371ab880257eebe8e1d1ea875c5a4e836c8cf080f30a446ced8c74dae02bce211172847e5bdeba4c82491400fe18532579f4a10322e10232bc391a914e3ad7485857253f4a648a88ab5cae518c73a763e13580405f3077dd1e2b665eb6002f33e78b44907726edcf4c8ab9c658598b24910a70a2fd65a9c92638898d84a3b49e3d5a6c6da1cc2df751b03f72190aeac38fc855462e2dfa897ac00d57083bdf044b04e9000100014a750e110bb206b6de993ff90cfe9936b112e42b9f5c0afe2db929c07df18572b90000000111756e6b6e6f776e526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000000000201000303fd03ea066901670065000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008020000092102bd5b831d6980ce7a1c342953d97fe73f7d150667b05e39d0466ba66efda380dc059f000400000000012103153c70ab08b260e17ad3d52d517544d4168041257dba377ca1b4cfc3a2de7e850274004900010001209eaee900fc3948eda143238f220372a61b535c6b1b984b6b26acd4c014537c1502220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff9c221f59e840166a09cd365da351d907b18febc8f71fa2656b7bb60da8c00bbd";
    protected static final String UNKNOWN_ROYLLO_COIN_RAW_PROOF_PROOF_ID = "a6cde389a35fcb8582fbbf1515f0f43ed3dc7b78dc4537c01d2505ddc25036de";
    protected static final int UNKNOWN_ROYLLO_COIN_VERSION = 0;
    protected static final String UNKNOWN_ROYLLO_COIN_GENESIS_POINT_TXID = "b97285f17dc029b92dfe0a5c9f2be412b13699fe0cf93f99deb606b20b110e75";
    protected static final int UNKNOWN_ROYLLO_COIN_GENESIS_POINT_VOUT = 1;
    protected static final String UNKNOWN_ROYLLO_COIN_NAME = "unknownRoylloCoin";
    protected static final String UNKNOWN_ROYLLO_COIN_META = "5573656420627920526f796c6c6f";
    protected static final String UNKNOWN_ROYLLO_COIN_ASSET_ID = "9eaee900fc3948eda143238f220372a61b535c6b1b984b6b26acd4c014537c15";
    protected static final int UNKNOWN_ROYLLO_COIN_OUTPUT_INDEX = 0;
    protected static final String UNKNOWN_ROYLLO_COIN_GENESIS_BOOTSTRAP_INFORMATION = "750e110bb206b6de993ff90cfe9936b112e42b9f5c0afe2db929c07df18572b90000000111756e6b6e6f776e526f796c6c6f436f696e0e5573656420627920526f796c6c6f0000000000";
    protected static final int UNKNOWN_ROYLLO_COIN_GENESIS_VERSION = 0;
    protected static final AssetType UNKNOWN_ROYLLO_COIN_ASSET_TYPE = AssetType.NORMAL;
    protected static final BigInteger UNKNOWN_ROYLLO_COIN_AMOUNT = BigInteger.valueOf(1002);
    protected static final int UNKNOWN_ROYLLO_COIN_LOCK_TIME = 0;
    protected static final int UNKNOWN_ROYLLO_COIN_RELATIVE_LOCK_TIME = 0;
    protected static final int UNKNOWN_ROYLLO_COIN_SCRIPT_VERSION = 0;
    protected static final String UNKNOWN_ROYLLO_COIN_SCRIPT_KEY = "02bd5b831d6980ce7a1c342953d97fe73f7d150667b05e39d0466ba66efda380dc";
    protected static final String UNKNOWN_ROYLLO_COIN_ANCHOR_TX = "02000000000101750e110bb206b6de993ff90cfe9936b112e42b9f5c0afe2db929c07df18572b90100000000ffffffff02e80300000000000022512074381d0361b8bb274bbec7ae6ef19578e2c9c6b789d9f6a390920bbb6a816bc9890700000000000016001443e6aa57dd19692fdb4ed0d2fe4395f9073ac9d00247304402200312366d908deb99e44849b1ad34ad2066436a657475ff4a304dc9d6c494635b0220544a9154e234f0deb5456775b5b5e253e93edb359703705ead13434147d91abf012102b959655e63ecfe92589f45672305ab3b9842fabb893679cb49677b40965ac20c00000000";
    protected static final String UNKNOWN_ROYLLO_COIN_ANCHOR_TX_ID = "db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839";
    protected static final String UNKNOWN_ROYLLO_COIN_ANCHOR_BLOCK_HASH = "0000000062f2e314fb8ff7cf691a6ac31a4525920234045d3b50c0f2d406efe7";
    protected static final String UNKNOWN_ROYLLO_COIN_ANCHOR_OUTPOINT = "db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839";
    protected static final String UNKNOWN_ROYLLO_COIN_ANCHOR_INTERNAL_KEY = "03153c70ab08b260e17ad3d52d517544d4168041257dba377ca1b4cfc3a2de7e85";
    protected static final String UNKNOWN_ROYLLO_COIN_TX_MERKLE_PROOF = "072ad5e87840f0f77f575d97a52ea6ac07304878255c1cd7bd43ad800cb136aa45cc4fb6dd4cc8ca29cd961f77f55996c56c3b9b7bcae769c8b9d31fb5ae0e0cd0371ab880257eebe8e1d1ea875c5a4e836c8cf080f30a446ced8c74dae02bce211172847e5bdeba4c82491400fe18532579f4a10322e10232bc391a914e3ad7485857253f4a648a88ab5cae518c73a763e13580405f3077dd1e2b665eb6002f33e78b44907726edcf4c8ab9c658598b24910a70a2fd65a9c92638898d84a3b49e3d5a6c6da1cc2df751b03f72190aeac38fc855462e2dfa897ac00d57083bdf044b";
    protected static final String UNKNOWN_ROYLLO_COIN_INCLUSION_PROOF = "000400000000012103153c70ab08b260e17ad3d52d517544d4168041257dba377ca1b4cfc3a2de7e850274004900010001209eaee900fc3948eda143238f220372a61b535c6b1b984b6b26acd4c014537c1502220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";

    /* ============================================================================================================== */
    /* Active royllo coin (living on testnet). */
    protected static final String ACTIVE_ROYLLO_COIN_ASSET_ID = "1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413";

    /* ============================================================================================================== */
    protected static final String ACTIVE_ROYLLO_COIN_GENESIS_POINT_TXID = "db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839";
    protected static final String ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF = "0000000001fd0393002439481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db00000001015004e00020e6c99a0dff85ede53ea46fef1c2c7c06fb0e2f4a904e43f21f00000000000000dd5aa62693ed7a74bb2fe8c449fc358b48717b4ba7c2fa000a91e199b6d112926b7dff63bcfe3319ba3fe85f02ea0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d60000000003a205e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c76151104e8000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd03eb0669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc059f000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8d2726a957285fe1e6f4c9916deb2086f5cd3fa67327caa039dbc85d57c9576f";
    protected static final String ACTIVE_ROYLLO_COIN_PROOF_1_RAWPROOF_PROOF_ID = "14e2075827c687217bede3f703cfbc94345717213f4fd34d83b68f8268040691";
    protected static final String ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF = "0000000002fd0393002439481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db00000001015004e00020e6c99a0dff85ede53ea46fef1c2c7c06fb0e2f4a904e43f21f00000000000000dd5aa62693ed7a74bb2fe8c449fc358b48717b4ba7c2fa000a91e199b6d112926b7dff63bcfe3319ba3fe85f02ea0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d60000000003a205e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c76151104e8000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd03eb0669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc059f000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8d2726a957285fe1e6f4c9916deb2086f5cd3fa67327caa039dbc85d57c9576ffd05a60024d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb1600000000101500000002056e41b012000c3de4265eac3671a8278ab65cbb05dfd66586a8178bf0000000094b2a4d08f54f11f05276326894ba9cc7a27a3d99247e669bd505422e8e8330c308bff63ffff001de1101dd102fd0180020000000001024186b51fb5050ce20332a18c940934faaf7b7f80874a9d9889b456a8927a20050200000000ffffffffd7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb16001000000000000000003e8030000000000002251207d84b124b559f1a0ab396cec2677d5a27a140caaef680cfdbecd7a16a4593762e8030000000000002251209588124b01b2a1f00db66c22b3c5560d58a7f6f8c09a98878723e87ee6eb10a3c90c0000000000001600148cb54fe42666c5e008316c342aa58c0d803392ca0247304402203b09ac49a17faf2c1bed11faccba212cfcfb01eb962656b9fd28f4a60fdc81980220561c9053d536e8cdcb771c13ba5957497e421c5ac6456b0c5be76ef908fc60c8012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c01407eb8a1f6bf214a8471f72a894cc99e6fe756b8f2258a6c786cb825767fc2762d2e1320da2eecabaa92872fa03d584d52e5ff5c80056be1488f44cc0aeb50559c0000000003e207938006468f02d2d507be06a19e95dcb745c0c80d2f909b699b1e9be7d51ab20f013260064752b181c2b7211d73f34b6a898c7728eed82e4fa837d3032a0f9f940b660b9ab0110750f38696f8afb8c9e1efe82f134bb264ccb624d2a89afde4d9eb44c9b74161a524df3f8d0cee247be9984f6694ca31766d2a12470bfc4e0b5c33b2659a8b50f188deb330af74218788c0717f66f4f56832b2ec568e571e97dd0fc080ed0b6465878f8547163ddf71a6825290b08846484fab9ab9f84895feedd0ff6506a12b5c5e718cd7ee09f8474c852587b7a6c693819fd4add29752f6796304fd0156000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd038706ad01ab0065d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb160000000011781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc014201402b9b32468fc0a3e8809763fdd191a720aba0f6fc0e46642e192484114cf1b002ac82e56cb4e8aaf8fcd9c7742e2b4fa6c195af91cedaa1a670ab46cb4a3811920728b48931d124c3c1911c5671ffabc9477c82b62cd62d3920d2cadd5878d31f882100000000000003eb08020000092102b0664f13bfc71ddcb8e0eb004b5b486cda6d0dfd33340b85d43f618b484e1517059f000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae0274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff06c901c70004000000010121030411db4d023a8d55607fedc562d519b1854af7752d9e1f00279213380d439e88029c007100010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024a000151f716888109ae4abf80fa5120d1eaca3e36ec8a2a45849e573001373de715170000000000000064ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffbf012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff48740a7a6841d86a30a38719fce36e4e30393e63acd4e5132ee2544dce048d37";
    protected static final String ACTIVE_ROYLLO_COIN_PROOF_2_RAWPROOF_PROOF_ID = "23a6c9e1db87a8993490c7578c7ae6d85fee3bc16b9fc7d3c4c756f7452262e1";
    protected static final String ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF = "0000000002fd0393002439481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db00000001015004e00020e6c99a0dff85ede53ea46fef1c2c7c06fb0e2f4a904e43f21f00000000000000dd5aa62693ed7a74bb2fe8c449fc358b48717b4ba7c2fa000a91e199b6d112926b7dff63bcfe3319ba3fe85f02ea0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d60000000003a205e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c76151104e8000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd03eb0669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc059f000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8d2726a957285fe1e6f4c9916deb2086f5cd3fa67327caa039dbc85d57c9576ffd07830024d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb1600000000101500000002056e41b012000c3de4265eac3671a8278ab65cbb05dfd66586a8178bf0000000094b2a4d08f54f11f05276326894ba9cc7a27a3d99247e669bd505422e8e8330c308bff63ffff001de1101dd102fd0180020000000001024186b51fb5050ce20332a18c940934faaf7b7f80874a9d9889b456a8927a20050200000000ffffffffd7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb16001000000000000000003e8030000000000002251207d84b124b559f1a0ab396cec2677d5a27a140caaef680cfdbecd7a16a4593762e8030000000000002251209588124b01b2a1f00db66c22b3c5560d58a7f6f8c09a98878723e87ee6eb10a3c90c0000000000001600148cb54fe42666c5e008316c342aa58c0d803392ca0247304402203b09ac49a17faf2c1bed11faccba212cfcfb01eb962656b9fd28f4a60fdc81980220561c9053d536e8cdcb771c13ba5957497e421c5ac6456b0c5be76ef908fc60c8012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c01407eb8a1f6bf214a8471f72a894cc99e6fe756b8f2258a6c786cb825767fc2762d2e1320da2eecabaa92872fa03d584d52e5ff5c80056be1488f44cc0aeb50559c0000000003e207938006468f02d2d507be06a19e95dcb745c0c80d2f909b699b1e9be7d51ab20f013260064752b181c2b7211d73f34b6a898c7728eed82e4fa837d3032a0f9f940b660b9ab0110750f38696f8afb8c9e1efe82f134bb264ccb624d2a89afde4d9eb44c9b74161a524df3f8d0cee247be9984f6694ca31766d2a12470bfc4e0b5c33b2659a8b50f188deb330af74218788c0717f66f4f56832b2ec568e571e97dd0fc080ed0b6465878f8547163ddf71a6825290b08846484fab9ab9f84895feedd0ff6506a12b5c5e718cd7ee09f8474c852587b7a6c693819fd4add29752f6796304fd0292000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f000000010002010003016406fd021301fd020f0065000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002fd01a44a00018205ee72be16f3b195b465cb095b97e811edddabb0dcec197f9e682d05a31ca00000000000000387ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7ffd0156000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd038706ad01ab0065d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb160000000011781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc014201402b9b32468fc0a3e8809763fdd191a720aba0f6fc0e46642e192484114cf1b002ac82e56cb4e8aaf8fcd9c7742e2b4fa6c195af91cedaa1a670ab46cb4a3811920728b48931d124c3c1911c5671ffabc9477c82b62cd62d3920d2cadd5878d31f882100000000000003eb08020000092102b0664f13bfc71ddcb8e0eb004b5b486cda6d0dfd33340b85d43f618b484e151708020000092102576e6cf95f0d7724f2e17afcd74a690231bf4e1ecb1963229af3fe33edcd58ca059f0004000000010121030411db4d023a8d55607fedc562d519b1854af7752d9e1f00279213380d439e880274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff06c901c7000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae029c007100010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024a00019fe1a55b2c9813569b7c7a3bf514bafc0cf43748d646a2bf39848870b06677a70000000000000387ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffbf012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff079f000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae0274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe362126b922539fc3451739b11745035fcc950ee86d0edace720399be6951c7e";
    protected static final String ACTIVE_ROYLLO_COIN_PROOF_3_RAWPROOF_PROOF_ID = "e537eddf83dcb34723121860b49579eb4e766ace01bbb81fc7fec233835f2e1e";

}