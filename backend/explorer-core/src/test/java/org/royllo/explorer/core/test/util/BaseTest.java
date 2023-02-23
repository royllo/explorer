package org.royllo.explorer.core.test.util;

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
    protected static final String ASSET_ID_NUMBER_01 = "b34b05956d828a7f7a0df598771c9f6df0378680c432480837852bcb94a8f21e";

    /* Test data living on the testnet. */

    /** Royllo coin - First coin created by me - Raw proof. */
    protected static final String ROYLLO_COIN_RAW_PROOF = "AAAAAAL9BDwAJBDC6+N2taWDvnmTVLgwr4mGBCuZpHd1LFUnq5CqK1crAAAAAAFQAAAAIGegXtiUZvfVqFzIXM+Ul6Ea0ocKGbVXeRYAAAAAAAAA88V9mnDK6ygsK+wQ7KfCeg/nwff6vxI6hejjG3e3ppSzbuFj//8AHfcJY/QC9wIAAAAAAQEQwuvjdrWlg755k1S4MK+JhgQrmaR3dSxVJ6uQqitXKwAAAAAA/////wLoAwAAAAAAACJRIEl5QgFinncSR5W9teZ0N9pg58KwtxVaxsqFGhe/u9T62wIUAAAAAAAiUSBCgsBJGj0pHAIH5pnVg2aT5RRCO77LppWEcSI74X/5+wJIMEUCIQD6tMoanMEyK7rpP8b2rIx087ZAwe0mVoYpuCxkAamPmwIgENrBagbUcOWF075vtCPOvlI9X57J/fvVE8P4t4czbdkBIQK+k2PkmF3Nwiw95Io50bOKY24IHopAQLzt0o3QBXxMBQAAAAAD/QECCNgtEMDuLNmpqUJZOmpfRiLqFw2aYQ4U5oWH8TweQpeRlrZDEsFy8XAbIOrDi2ZrkZxm7znVvSkgK7S/B2Ta49wxA67ssl5wpx9vdsc61OZNPi3AI0y6gS/q4+rok/ol1ad+s90SwnbVUgkJ47SxCRkj5grGRt1PrFv0Zjf+rJVpmZTmL8MZvk9dChCbc510zh+wdAan3BdudN0AOSrMQctsMb97k0nHtICgrjIRPivXUXdEUNO2SHLoqC9SsypXuDoleqRtyL1uga66yfapcEgOHBYhAKQShxShweEXXQsVb+Ok1B+O7rVz6Nqn2gbg3doES/fH/i/4zxHLLAbHZVTnBPAAAQABTxDC6+N2taWDvnmTVLgwr4mGBCuZpHd1LFUnq5CqK1crAAAAABBoYWJpYnRhcm9fcmVtb3RlFGhhYmlidGFyby5jb21fcmVtb3RlAAAAAAACAQADBf4BQG9ABmkBZwBlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAgAACSECA+AboGxeZjf10S0GaTD2D9iVIjqlz4EM++c0+e6OeHoFnwAEAAAAAAEhA30ykpyTT6FlRxaNVkG5o8GTpDZs/evCPmYMwsU4TEbsAnQASQABAAEgPgVFMqaoaedeG96KJeeUQXfWSKA0/lwUirf3dDU4zJ4CIgAA//////////////////////////////////////////8BJwABAAEiAAD//////////////////////////////////////////wYwAS4ABAAAAAEBIQLcSHLzaT/lCeYzvSz09KG7cDGpf10FayQj1Nabl6rTHwMDAgEBvBHvQ38qh6p5zALFW2Tb5hlkomhRe2JrsW+JXDHjKTv9B6MAJItUvaRjib4LIAdu+JHZhfKL09chEbykNqv8Wtf4ZjuFAAAAAAFQAABgIKp/g5866TLs7shSVp67lnMLTGj5SrWe6RQAAAAAAAAAfYgH/VE/hEuIsQEzMW3sPtBF6wySS/DXEfxSoQJXzTGljOFjQV0iGT8vm9wC/QFjAgAAAAABAotUvaRjib4LIAdu+JHZhfKL09chEbykNqv8Wtf4ZjuFAQAAAAD/////i1S9pGOJvgsgB274kdmF8ovT1yERvKQ2q/xa1/hmO4UAAAAAAAAAAAAD6AMAAAAAAAAiUSA9IQPuVFvpjbeV1z4R/YY0s8C9ddkTNAJSC2HoHyHIregDAAAAAAAAIlEgUai+/8Zd3X7xelRG7HWkqk+aWMERe8R0VTxvNBXKbCY+zRMAAAAAACJRINqgGvK1f1IpAc/1nKQ2DiPaSkHWu0qknqmPtn2NIRvHAUBlGhmFAycyVnnN7vFCI4h26fRS3xbQDzqwVtbUudst1Zxiv/5WsajTzq/0QGcbyJCoG/eL7/obfCqbl+idWInBAUDzUTcJn5wvzDEoP7vkiVTKu4LKMPRyi800488/ygMl6nsKKgmnizlM+0cky4CPYcdWt8aXt4qE/Wl9O+8jENqqAAAAAAPiB4WGOIm06NBFx7jkGoxP2hfVp/npBoT99oeOOr2DShfGjGvX13J3mw9bMkLPbe4lHetJ/pwnnY1t+ZTD9Z3Fp8LS8WQioOgFhuZMDFFVpkAUJ3sWLX/PNArkawLbYitBfXNJBaHOgK9Ydt4lPyWl+ggcdZv6HPoPcWZEfo7MkGLbaepIee3Lt90lJLxKLD50EwXkjvtE5AcLHTc+v4RNw3aBE4FYQhF1lPIpqaKkJ2C2i+l6UJTqXIFZByqnfgVWtKm7KIaL5WUd0hxpJoA2dyJkJP04S8N3wKBxMRuWgTr7dAT9AqAAAQABTxDC6+N2taWDvnmTVLgwr4mGBCuZpHd1LFUnq5CqK1crAAAAABBoYWJpYnRhcm9fcmVtb3RlFGhhYmlidGFyby5jb21fcmVtb3RlAAAAAAACAQADAQoG/QIbAf0CFwBlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAC/QGsSgAB5NRF1CXSOcmsCA7ruti701k17esd4DkHknGTvKlyybkAAAAAAUBvNv////////////////////////////////////////9//QFeAAEAAU8QwuvjdrWlg755k1S4MK+JhgQrmaR3dSxVJ6uQqitXKwAAAAAQaGFiaWJ0YXJvX3JlbW90ZRRoYWJpYnRhcm8uY29tX3JlbW90ZQAAAAAAAgEAAwX+AUBvNgatAasAZYtUvaRjib4LIAdu+JHZhfKL09chEbykNqv8Wtf4ZjuFAAAAAD4FRTKmqGnnXhveiiXnlEF31kigNP5cFIq393Q1OMyeAgPgG6BsXmY39dEtBmkw9g/YlSI6pc+BDPvnNPnujnh6AUIBQOcBOWi0inzLOOcRrYChn+G4JyWe9eJlO6s4pYa9PKXiSs9jhJn507nvN3D3am3rGN8L59gXo5Whx5noCeHzct8HKMbUVMC2KVHdr5Jiz2Vteqtd2BzaNSwvi8Lns2tfwA/RAAAAAAFAb0AIAgAACSECV+BOR3G04Fq5J3zkatO6g72pRZit4NNLYohjWD1wyyMIAgAACSECj1cBE6L59z9epRdOVwJVSLvamNAyPwX1tWvm++NAxO4FnwAEAAAAAQEhA+riUffgTwRYogPwsYZf9qSPZu7VB09YbeMsTCO0eWIkAnQASQABAAEgPgVFMqaoaedeG96KJeeUQXfWSKA0/lwUirf3dDU4zJ4CIgAA//////////////////////////////////////////8BJwABAAEiAAD//////////////////////////////////////////wb4AscABAAAAAABIQJ6GjV81gvs1dD4gom7Br/bN320g9vfVd4cOArP+8KYbgKcAHEAAQABID4FRTKmqGnnXhveiiXnlEF31kigNP5cFIq393Q1OMyeAkoAAQik60qmGCuF2rjsQQM4XPAulUxC/2fwPMvHLDeu2Y6IAAAAAAFAbzb/////////////////////////////////////////3wEnAAEAASIAAP//////////////////////////////////////////LgAEAAAAAgEhAhsLhWslxtgqGnR7DWI87F8C/47W24vggHC6nqnBr2NsAwMCAQEHnwAEAAAAAAEhAnoaNXzWC+zV0PiCibsGv9s3fbSD299V3hw4Cs/7wphuAnQASQABAAEgPgVFMqaoaedeG96KJeeUQXfWSKA0/lwUirf3dDU4zJ4CIgAA//////////////////////////////////////////8BJwABAAEiAAD//////////////////////////////////////////6NumG4nl1ZBFr46o4uvgs0ocbYbB1NTHzX7mTrGcqqx";

    /** Royllo coin - First coin created by me - Raw proof. */
    protected static final long ROYLLO_COIN_PROOF_INDEX = 1;

    /** Royllo coin - First coin created by me - Proof id generated. */
    protected static final String ROYLLO_COIN_PROOF_ID = "13c6a622d93c12d608b9d8189c715c4bffda254eb0dde0376dcb641e2e7b8222";
}