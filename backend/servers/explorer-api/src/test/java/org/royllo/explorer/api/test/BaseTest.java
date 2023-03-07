package org.royllo.explorer.api.test;

import org.royllo.explorer.core.util.enums.AssetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

/**
 * Utility classes for tests.
 */
@SuppressWarnings("SpellCheckingInspection")
public class BaseTest {

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
    protected static final String MY_ROYLLO_COIN_ANCHRO_INTERNAL_KEY = "03336830ceadb0fc7f67cf65b1b79d1b7dc6ecd3d13ff2af8b4ea6e7d14cee1fe6";
    /* ============================================================================================================== */

}
