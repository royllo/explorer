package org.royllo.explorer.core.test.util;

import org.royllo.explorer.core.util.base.Base;
import org.royllo.explorer.core.util.enums.AssetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

/**
 * Utility classes for tests.
 */
@SuppressWarnings("SpellCheckingInspection")
public class BaseTest extends Base {

    /** Logger. */
    protected final Logger logger = LoggerFactory.getLogger(getClass().getName());

    /** Non existing database. */
    protected static final String BITCOIN_TRANSACTION_NON_EXISTING = "non_existing";

    /** txid of the first bitcoin transaction in database - one output. */
    protected static final String BITCOIN_TRANSACTION_1_TXID = "2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea";

    /** txid of the second bitcoin transaction in database - two outputs. */
    protected static final String BITCOIN_TRANSACTION_2_TXID = "46804b8a193cae200c99531f0ea90d81cc0c0e44e718b57e7b9ab5bb3926b946";

    /** txid of a third transaction not in our database but in the blockchain - three outputs. */
    protected static final String BITCOIN_TRANSACTION_3_TXID = "117ad75a79af2e7fdb2908baee9171fde4d6fb80c7322dcb895a2429f84f4d4a";

    /** txid of a taproot transaction in our database used for test. */
    protected static final String BITCOIN_TAPROOT_TRANSACTION_1_TXID = "taproot_test_transaction_number_1_d6fb80c7322dcb895a2429f84f4d4a";

    /** txid of a taproot transaction NOT in our database, but IN the blockchain. */
    protected static final String BITCOIN_TAPROOT_TRANSACTION_2_TXID = "d61a4957e5e756a7631246b1a00d685e4854f98f8c2835bafafed8b1d1e26be5";

    /** txid of a taproot transaction containing a taro asset on the testnet (output 1). */
    protected static final String BITCOIN_TESTNET_TARO_TRANSACTION_1_TXID = "d8a8016095b9fcd1f63c57342d375026ecbc72c885a54b676c6e62b216e15365";

    /** Taro asset number 1. */
    protected static final String ASSET_ID_NUMBER_01 = "b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e";

    /* ============================================================================================================== */
    /* My Royllo coin (living on testnet). */

    protected static final int MY_ROYLLO_COIN_ID = 1;
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
    protected static final String MY_ROYLLO_COIN_ANCHOR_TX= "020000000001014b8591404f8dc62002a3608fd35bc55243f15cf806a10c80ffb565aa7063390c0100000000ffffffff02e803000000000000225120890016973f4a07ecb7afc16f805219e3eaf1bc1d0635894c821114dbf6e2ca36bd11000000000000160014c35088956d26c44af4c00dfc8bf10d9a4c79320e0248304502210095b84eb101cd33d1b95bf1527d79653a09d75d8db259192bd9ff09a313eb9fbc02200eacd659b386ccea82a82f26458a0a6893eeb454cd50cc2c05e2ff7bf71d4b2d012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c00000000";
    protected static final String MY_ROYLLO_COIN_ANCHOR_TX_ID = "56fe33cc4fbdba40de3b710ba6b572f1e6b04ac93ab7103e586e4b897d0840c3";
    protected static final String MY_ROYLLO_COIN_ANCHOR_BLOCK_HASH = "0000000000000000000000000000000000000000000000000000000000000000";
    protected static final String MY_ROYLLO_COIN_ANCHOR_OUTPOINT = "56fe33cc4fbdba40de3b710ba6b572f1e6b04ac93ab7103e586e4b897d0840c3";
    protected static final String MY_ROYLLO_COIN_ANCHOR_INTERNAL_KEY = "03336830ceadb0fc7f67cf65b1b79d1b7dc6ecd3d13ff2af8b4ea6e7d14cee1fe6";
    /* ============================================================================================================== */

    /* ============================================================================================================== */
    /* Unknown Royllo coin (living on testnet). */

    protected static final String UNKNOWN_ROYLLO_COIN_RAW_PROOF = "0000000001fd03d40024750e110bb206b6de993ff90cfe9936b112e42b9f5c0afe2db929c07df18572b90000000101500000002080b8233c36f756820bca031a1af25171256708b74db796fc090000000000000051ccbb70dd4f539ee43aa2497b2e574fe933cd6f8e99bdeefc7a14fb903de073c77bff63ffff001db224071402ea02000000000101750e110bb206b6de993ff90cfe9936b112e42b9f5c0afe2db929c07df18572b90100000000ffffffff02e80300000000000022512074381d0361b8bb274bbec7ae6ef19578e2c9c6b789d9f6a390920bbb6a816bc9890700000000000016001443e6aa57dd19692fdb4ed0d2fe4395f9073ac9d00247304402200312366d908deb99e44849b1ad34ad2066436a657475ff4a304dc9d6c494635b0220544a9154e234f0deb5456775b5b5e253e93edb359703705ead13434147d91abf012102b959655e63ecfe92589f45672305ab3b9842fabb893679cb49677b40965ac20c0000000003e2072ad5e87840f0f77f575d97a52ea6ac07304878255c1cd7bd43ad800cb136aa45cc4fb6dd4cc8ca29cd961f77f55996c56c3b9b7bcae769c8b9d31fb5ae0e0cd0371ab880257eebe8e1d1ea875c5a4e836c8cf080f30a446ced8c74dae02bce211172847e5bdeba4c82491400fe18532579f4a10322e10232bc391a914e3ad7485857253f4a648a88ab5cae518c73a763e13580405f3077dd1e2b665eb6002f33e78b44907726edcf4c8ab9c658598b24910a70a2fd65a9c92638898d84a3b49e3d5a6c6da1cc2df751b03f72190aeac38fc855462e2dfa897ac00d57083bdf044b04e9000100014a750e110bb206b6de993ff90cfe9936b112e42b9f5c0afe2db929c07df18572b90000000111756e6b6e6f776e526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000000000201000303fd03ea066901670065000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008020000092102bd5b831d6980ce7a1c342953d97fe73f7d150667b05e39d0466ba66efda380dc059f000400000000012103153c70ab08b260e17ad3d52d517544d4168041257dba377ca1b4cfc3a2de7e850274004900010001209eaee900fc3948eda143238f220372a61b535c6b1b984b6b26acd4c014537c1502220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff9c221f59e840166a09cd365da351d907b18febc8f71fa2656b7bb60da8c00bbd";
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
    protected static final String UNKNOWN_ROYLLO_COIN_ANCHOR_TX= "02000000000101750e110bb206b6de993ff90cfe9936b112e42b9f5c0afe2db929c07df18572b90100000000ffffffff02e80300000000000022512074381d0361b8bb274bbec7ae6ef19578e2c9c6b789d9f6a390920bbb6a816bc9890700000000000016001443e6aa57dd19692fdb4ed0d2fe4395f9073ac9d00247304402200312366d908deb99e44849b1ad34ad2066436a657475ff4a304dc9d6c494635b0220544a9154e234f0deb5456775b5b5e253e93edb359703705ead13434147d91abf012102b959655e63ecfe92589f45672305ab3b9842fabb893679cb49677b40965ac20c00000000";
    protected static final String UNKNOWN_ROYLLO_COIN_ANCHOR_TX_ID = "db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839";
    protected static final String UNKNOWN_ROYLLO_COIN_ANCHOR_BLOCK_HASH = "0000000062f2e314fb8ff7cf691a6ac31a4525920234045d3b50c0f2d406efe7";
    protected static final String UNKNOWN_ROYLLO_COIN_ANCHOR_OUTPOINT = "db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839";
    protected static final String UNKNOWN_ROYLLO_COIN_ANCHOR_INTERNAL_KEY = "03153c70ab08b260e17ad3d52d517544d4168041257dba377ca1b4cfc3a2de7e85";
    protected static final String UNKNOWN_ROYLLO_COIN_TX_MERKLE_PROOF = "072ad5e87840f0f77f575d97a52ea6ac07304878255c1cd7bd43ad800cb136aa45cc4fb6dd4cc8ca29cd961f77f55996c56c3b9b7bcae769c8b9d31fb5ae0e0cd0371ab880257eebe8e1d1ea875c5a4e836c8cf080f30a446ced8c74dae02bce211172847e5bdeba4c82491400fe18532579f4a10322e10232bc391a914e3ad7485857253f4a648a88ab5cae518c73a763e13580405f3077dd1e2b665eb6002f33e78b44907726edcf4c8ab9c658598b24910a70a2fd65a9c92638898d84a3b49e3d5a6c6da1cc2df751b03f72190aeac38fc855462e2dfa897ac00d57083bdf044b";
    protected static final String UNKNOWN_ROYLLO_COIN_INCLUSION_PROOF = "000400000000012103153c70ab08b260e17ad3d52d517544d4168041257dba377ca1b4cfc3a2de7e850274004900010001209eaee900fc3948eda143238f220372a61b535c6b1b984b6b26acd4c014537c1502220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
    /* ============================================================================================================== */

}