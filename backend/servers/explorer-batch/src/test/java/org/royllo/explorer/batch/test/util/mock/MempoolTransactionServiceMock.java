package org.royllo.explorer.batch.test.util.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.royllo.explorer.batch.test.util.BaseTest;
import org.royllo.explorer.core.provider.mempool.GetTransactionResponse;
import org.royllo.explorer.core.provider.mempool.MempoolTransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * {@link MempoolTransactionService} mock.
 */
@Profile("mempoolTransactionServiceMock")
@Configuration
public class MempoolTransactionServiceMock extends BaseTest {

    @Bean
    @Primary
    public MempoolTransactionService mempoolTransactionService() throws IOException {
        final MempoolTransactionService mockedService = Mockito.mock(MempoolTransactionService.class);

        GetTransactionResponse.Status status;
        GetTransactionResponse.VOut vout1;
        GetTransactionResponse.VOut vout2;
        GetTransactionResponse.VOut vout3;
        GetTransactionResponse response;

        // =============================================================================================================
        // Non-existing transaction.
        Mockito.when(mockedService.getTransaction(BITCOIN_TRANSACTION_NON_EXISTING)).thenReturn(Mono.empty());

        // =============================================================================================================
        // UNKNOWN_ROYLLO_COIN_GENESIS_POINT_TXID - Unknown Royllo coin transaction
        // curl -ssL https://mempool.space/api/tx/b97285f17dc029b92dfe0a5c9f2be412b13699fe0cf93f99deb606b20b110e75 | jq
        status = new GetTransactionResponse.Status();
        status.setBlockHeight(2422164);

        vout1 = new GetTransactionResponse.VOut();
        vout1.setScriptPubKey("5120592efc989c327d03dbf68484b3e7b84d3b122b6e11951f8c6093d818422db4d5");
        vout1.setScriptPubKeyAsm("OP_PUSHNUM_1 OP_PUSHBYTES_32 592efc989c327d03dbf68484b3e7b84d3b122b6e11951f8c6093d818422db4d5");
        vout1.setScriptPubKeyType("v1_p2tr");
        vout1.setScriptPubKeyAddress("tb1ptyh0exyuxf7s8klksjzt8eacf5a3y2mwzx23lrrqj0vpss3dkn2s5c5v79");
        vout1.setValue(new BigDecimal("1000"));

        vout2 = new GetTransactionResponse.VOut();
        vout2.setScriptPubKey("0014db6d89fb2231cf81c02599e12fce7edaaaca0a18");
        vout2.setScriptPubKeyAsm("OP_0 OP_PUSHBYTES_20 db6d89fb2231cf81c02599e12fce7edaaaca0a18");
        vout2.setScriptPubKeyType("v0_p2wpkh");
        vout2.setScriptPubKeyAddress("tb1qmdkcn7ezx88crsp9n8sjlnn7m24v5zsch8xtev");
        vout2.setValue(new BigDecimal("3235"));

        response = new GetTransactionResponse();
        response.setStatus(status);
        response.getVout().add(vout1);
        response.getVout().add(vout2);
        Mockito.when(mockedService.getTransaction(UNKNOWN_ROYLLO_COIN_GENESIS_POINT_TXID)).thenReturn(Mono.just(response));

        // =============================================================================================================
        // BITCOIN_TRANSACTION_1_TXID - Transaction already on our database.
        // curl -ssL https://mempool.space/api/tx/2a5726687859bb1ec8a8cfeac78db8fa16b5b1c31e85be9c9812dfed68df43ea | jq
        status = new GetTransactionResponse.Status();
        status.setBlockHeight(720);

        vout1 = new GetTransactionResponse.VOut();
        vout1.setScriptPubKey("410415ca91c387efac4ea86f0196b2a1d831f75a488f115055636f0022c51df508197ad4fc31f553d48052b05b0a3b6030a84a0441adae97734129bb8ea0ddfd4004ac");
        vout1.setScriptPubKeyAsm("OP_PUSHBYTES_65 0415ca91c387efac4ea86f0196b2a1d831f75a488f115055636f0022c51df508197ad4fc31f553d48052b05b0a3b6030a84a0441adae97734129bb8ea0ddfd4004 OP_CHECKSIG");
        vout1.setScriptPubKeyType("p2pk");
        vout1.setScriptPubKeyAddress("");
        vout1.setValue(new BigDecimal("5000000000"));

        response = new GetTransactionResponse();
        response.setStatus(status);
        response.getVout().add(vout1);
        Mockito.when(mockedService.getTransaction(BITCOIN_TRANSACTION_1_TXID)).thenReturn(Mono.just(response));

        // =============================================================================================================
        // BITCOIN_TRANSACTION_2_TXID - Transaction already on our database.
        // curl -ssL https://mempool.space/api/tx/46804b8a193cae200c99531f0ea90d81cc0c0e44e718b57e7b9ab5bb3926b946 | jq
        status = new GetTransactionResponse.Status();
        status.setBlockHeight(766);

        vout1 = new GetTransactionResponse.VOut();
        vout1.setScriptPubKey("76a914607c727d45ba97358a3fa06be04a514cfb12f4dd88ac");
        vout1.setScriptPubKeyAsm("OP_DUP OP_HASH160 OP_PUSHBYTES_20 607c727d45ba97358a3fa06be04a514cfb12f4dd OP_EQUALVERIFY OP_CHECKSIG");
        vout1.setScriptPubKeyType("p2pkh");
        vout1.setScriptPubKeyAddress("19oAxAJhAKmEFb2pcA9qiX8sV8hxKeXjzd");
        vout1.setValue(new BigDecimal("10000000"));

        vout2 = new GetTransactionResponse.VOut();
        vout2.setScriptPubKey("0020701a8d401c84fb13e6baf169d59684e17abd9fa216c8cc5b9fc63d622ff8c58d");
        vout2.setScriptPubKeyAsm("OP_0 OP_PUSHBYTES_32 701a8d401c84fb13e6baf169d59684e17abd9fa216c8cc5b9fc63d622ff8c58d");
        vout2.setScriptPubKeyType("v0_p2wsh");
        vout2.setScriptPubKeyAddress("bc1qwqdg6squsna38e46795at95yu9atm8azzmyvckulcc7kytlcckxswvvzej");
        vout2.setValue(new BigDecimal("47765975"));

        response = new GetTransactionResponse();
        response.setStatus(status);
        response.getVout().add(vout1);
        response.getVout().add(vout2);
        Mockito.when(mockedService.getTransaction(BITCOIN_TRANSACTION_2_TXID)).thenReturn(Mono.just(response));

        // =============================================================================================================
        // BITCOIN_TRANSACTION_3_TXID - Transaction not in our database but in the blockchain.
        // curl -ssL https://mempool.space/api/tx/117ad75a79af2e7fdb2908baee9171fde4d6fb80c7322dcb895a2429f84f4d4a | jq
        status = new GetTransactionResponse.Status();
        status.setBlockHeight(754381);

        vout1 = new GetTransactionResponse.VOut();
        vout1.setScriptPubKey("76a9140aa7e954ae2c972225309f0992e3ecd698a90f5f88ac");
        vout1.setScriptPubKeyAsm("OP_DUP OP_HASH160 OP_PUSHBYTES_20 0aa7e954ae2c972225309f0992e3ecd698a90f5f OP_EQUALVERIFY OP_CHECKSIG");
        vout1.setScriptPubKeyType("p2pkh");
        vout1.setScriptPubKeyAddress("1yLucPwZwVuNxMFTXyyX5qTk3YFNpAGEz");
        vout1.setValue(new BigDecimal("2036308"));

        vout2 = new GetTransactionResponse.VOut();
        vout2.setScriptPubKey("76a914611385f68799bfc360feacc3095de4e51a9c1bd688ac");
        vout2.setScriptPubKeyAsm("OP_DUP OP_HASH160 OP_PUSHBYTES_20 611385f68799bfc360feacc3095de4e51a9c1bd6 OP_EQUALVERIFY OP_CHECKSIG");
        vout2.setScriptPubKeyType("p2pkh");
        vout2.setScriptPubKeyAddress("19rHw7N8TJgbJUrS6LCD3tz7KMyTDKAFwH");
        vout2.setValue(new BigDecimal("1048100"));

        vout3 = new GetTransactionResponse.VOut();
        vout3.setScriptPubKey("a914ae6d0c0ec1acb1612b76c81793d44ef1a1dde27787");
        vout3.setScriptPubKeyAsm("OP_HASH160 OP_PUSHBYTES_20 ae6d0c0ec1acb1612b76c81793d44ef1a1dde277 OP_EQUAL");
        vout3.setScriptPubKeyType("p2sh");
        vout3.setScriptPubKeyAddress("3HbJ8eWmHDDa1JhafYYaEdLUWkVpDfX5HM");
        vout3.setValue(new BigDecimal("279180"));

        response = new GetTransactionResponse();
        response.setStatus(status);
        response.getVout().add(vout1);
        response.getVout().add(vout2);
        response.getVout().add(vout3);
        Mockito.when(mockedService.getTransaction(BITCOIN_TRANSACTION_3_TXID)).thenReturn(Mono.just(response));

        // =============================================================================================================
        // BITCOIN_TAPROOT_TRANSACTION_2_TXID - Taproot transaction NOT in our database, but IN the blockchain.
        // curl -ssL https://mempool.space/api/tx/d61a4957e5e756a7631246b1a00d685e4854f98f8c2835bafafed8b1d1e26be5 | jq

        status = new GetTransactionResponse.Status();
        status.setBlockHeight(742158);

        vout1 = new GetTransactionResponse.VOut();
        vout1.setScriptPubKey("5120b037c6aa6784da1cfb00a5b0caaafd70556aa832cb6b67ba5e934f483d6a7f23");
        vout1.setScriptPubKeyAsm("OP_PUSHNUM_1 OP_PUSHBYTES_32 b037c6aa6784da1cfb00a5b0caaafd70556aa832cb6b67ba5e934f483d6a7f23");
        vout1.setScriptPubKeyType("v1_p2tr");
        vout1.setScriptPubKeyAddress("bc1pkqmud2n8sndpe7cq5kcv42hawp2k42pjed4k0wj7jd85s0t20u3srt7l2r");
        vout1.setValue(new BigDecimal("43124666573"));

        response = new GetTransactionResponse();
        response.setStatus(status);
        response.getVout().add(vout1);
        Mockito.when(mockedService.getTransaction(BITCOIN_TAPROOT_TRANSACTION_2_TXID)).thenReturn(Mono.just(response));

        // =============================================================================================================
        // Active royllo coin - Taproot transaction NOT in our database, but IN the blockchain.
        // curl -ssL https://mempool.space/testnet/api/tx/db848f3114a248aed35008febbf04505652cb296726d4e1a998d08ca351e4839 | jq
        status = new GetTransactionResponse.Status();
        status.setBlockHeight(2422165);

        vout1 = new GetTransactionResponse.VOut();
        vout1.setScriptPubKey("512074381d0361b8bb274bbec7ae6ef19578e2c9c6b789d9f6a390920bbb6a816bc9");
        vout1.setScriptPubKeyAsm("OP_PUSHNUM_1 OP_PUSHBYTES_32 74381d0361b8bb274bbec7ae6ef19578e2c9c6b789d9f6a390920bbb6a816bc9");
        vout1.setScriptPubKeyType("v1_p2tr");
        vout1.setScriptPubKeyAddress("tb1pwsup6qmphzajwja7c7hxauv40r3vn34h38vldgusjg9mk65pd0ys9lnfnq");
        vout1.setValue(new BigDecimal("1000"));

        vout2 = new GetTransactionResponse.VOut();
        vout2.setScriptPubKey("001443e6aa57dd19692fdb4ed0d2fe4395f9073ac9d0");
        vout2.setScriptPubKeyAsm("OP_0 OP_PUSHBYTES_20 43e6aa57dd19692fdb4ed0d2fe4395f9073ac9d0");
        vout2.setScriptPubKeyType("v0_p2wpkh");
        vout2.setScriptPubKeyAddress("tb1qg0n2547ar95jlk6w6rf0usu4lyrn4jwshay0fc");
        vout2.setValue(new BigDecimal("1929"));

        response = new GetTransactionResponse();
        response.setStatus(status);
        response.getVout().add(vout1);
        response.getVout().add(vout2);
        Mockito.when(mockedService.getTransaction(ACTIVE_ROYLLO_COIN_GENESIS_POINT_TXID)).thenReturn(Mono.just(response));

        // Test coin
        final ClassPathResource testCoinGenesisPointTransaction = new ClassPathResource("tapd/TestCoin/TestCoin-mempool-genesis-point.json");
        final ClassPathResource testCoinProof1AnchorTransaction = new ClassPathResource("tapd/TestCoin/TestCoin-mempool-proof-1-anchor.json");
        final ClassPathResource testCoinProof2AnchorTransaction = new ClassPathResource("tapd/TestCoin/TestCoin-mempool-proof-2-anchor.json");
        final ClassPathResource testCoinProof3AnchorTransaction = new ClassPathResource("tapd/TestCoin/TestCoin-mempool-proof-3-anchor.json");
        GetTransactionResponse testCoinGenesisPointResponse = new ObjectMapper().readValue(testCoinGenesisPointTransaction.getInputStream(), GetTransactionResponse.class);
        GetTransactionResponse testCoinProof1AnchorResponse = new ObjectMapper().readValue(testCoinProof1AnchorTransaction.getInputStream(), GetTransactionResponse.class);
        GetTransactionResponse testCoinProof2AnchorResponse = new ObjectMapper().readValue(testCoinProof2AnchorTransaction.getInputStream(), GetTransactionResponse.class);
        GetTransactionResponse testCoinProof3AnchorResponse = new ObjectMapper().readValue(testCoinProof3AnchorTransaction.getInputStream(), GetTransactionResponse.class);
        Mockito.when(mockedService.getTransaction(TESTCOIN_GENESIS_POINT_TXID)).thenReturn(Mono.just(testCoinGenesisPointResponse));
        Mockito.when(mockedService.getTransaction(TESTCOIN_RAW_PROOF_1_ANCHOR_TXID)).thenReturn(Mono.just(testCoinProof1AnchorResponse));
        Mockito.when(mockedService.getTransaction(TESTCOIN_RAW_PROOF_2_ANCHOR_TXID)).thenReturn(Mono.just(testCoinProof2AnchorResponse));
        Mockito.when(mockedService.getTransaction(TESTCOIN_RAW_PROOF_3_ANCHOR_TXID)).thenReturn(Mono.just(testCoinProof3AnchorResponse));

        return mockedService;
    }

}
