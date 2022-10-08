package org.royllo.explorer.api.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    protected static final String TARO_ASSET_ID_NUMBER_01 = "b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e";

}
