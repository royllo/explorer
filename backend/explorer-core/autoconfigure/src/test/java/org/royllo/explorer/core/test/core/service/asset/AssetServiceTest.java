package org.royllo.explorer.core.test.core.service.asset;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.dto.bitcoin.BitcoinTransactionOutputDTO;
import org.royllo.explorer.core.repository.asset.AssetGroupRepository;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.bitcoin.BitcoinService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.math.BigInteger.ONE;
import static java.util.Calendar.MARCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_HOST;
import static org.royllo.explorer.core.provider.storage.LocalFileServiceImplementation.WEB_SERVER_PORT;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_DTO;
import static org.royllo.explorer.core.util.constants.TaprootAssetsConstants.ASSET_ALIAS_LENGTH;
import static org.royllo.explorer.core.util.enums.AssetType.NORMAL;
import static org.royllo.explorer.core.util.enums.FileType.IMAGE;
import static org.royllo.explorer.core.util.enums.FileType.JSON;
import static org.royllo.explorer.core.util.enums.FileType.TEXT;
import static org.royllo.explorer.core.util.enums.FileType.UNKNOWN;
import static org.royllo.test.MempoolData.ROYLLO_COIN_GENESIS_TXID;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_NFT_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_NFT_ASSET_ID_ALIAS;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_1_FROM_TEST;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_2_ASSET_ID_ALIAS;
import static org.royllo.test.TapdData.SET_OF_ROYLLO_NFT_3_ASSET_ID;
import static org.royllo.test.TapdData.TRICKY_ROYLLO_COIN_ASSET_ID;

@SpringBootTest
@DirtiesContext
@DisplayName("AssetService tests")
public class AssetServiceTest extends TestWithMockServers {

    @Autowired
    private BitcoinService bitcoinService;

    @Autowired
    private AssetGroupRepository assetGroupRepository;

    @Autowired
    private AssetService assetService;

    @Test
    @DisplayName("addAsset()")
    public void addAsset() {
        // We retrieve a bitcoin transaction output from database for our test.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 0);
        assertTrue(bto.isPresent());

        // =============================================================================================================
        // First test - Trying to save an existing asset.
        AssertionError e = assertThrows(AssertionError.class, () -> assetService.addAsset(AssetDTO.builder().id(1L).build()));
        assertEquals("Asset already exists", e.getMessage());

        // =============================================================================================================
        // Second test - Bitcoin transaction is null.
        e = assertThrows(AssertionError.class, () -> assetService.addAsset(AssetDTO.builder().build()));
        assertEquals("Bitcoin transaction is required", e.getMessage());

        // =============================================================================================================
        // Third test - AssetId is already in the database.
        e = assertThrows(AssertionError.class, () -> assetService.addAsset(AssetDTO.builder()
                .creator(ANONYMOUS_USER_DTO)
                .assetId(ROYLLO_COIN_ASSET_ID)
                .genesisPoint(bto.get())
                .metaDataHash("metadata")
                .name("name")
                .outputIndex(0)
                .version(0)
                .type(NORMAL)
                .amount(ONE)
                .build()));
        assertTrue(e.getMessage().endsWith("already registered"));

        // =============================================================================================================
        // We add a first asset.
        // The bitcoin transaction output doesn't exist in database, the mock returns it and transaction is created.
        // There is no asset group, no one should be created.
        int assetGroupCount = assetGroupRepository.findAll().size();
        final AssetDTO asset1 = assetService.addAsset(AssetDTO.builder()
                .creator(ANONYMOUS_USER_DTO)
                .assetId("my asset id")
                .genesisPoint(BitcoinTransactionOutputDTO.builder()
                        .txId(ROYLLO_COIN_GENESIS_TXID)
                        .vout(0)
                        .build())
                .metaDataHash("my meta data hash")
                .name("testCoin")
                .outputIndex(8)
                .version(0)
                .type(NORMAL)
                .amount(ONE)
                .build());

        // Testing asset value.
        assertNotNull(asset1.getId());
        assertEquals(ANONYMOUS_USER_DTO.getId(), asset1.getCreator().getId());
        assertEquals("my asset id", asset1.getAssetId());
        assertNotNull(asset1.getAssetIdAlias());
        assertEquals(ASSET_ALIAS_LENGTH, asset1.getAssetIdAlias().length());
        // Genesis.
        assertNotNull(asset1.getGenesisPoint());
        assertNotNull(asset1.getGenesisPoint().getId());
        verifyTransaction(asset1.getGenesisPoint(), ROYLLO_COIN_GENESIS_TXID);
        // Asset value data.
        assertEquals("my meta data hash", asset1.getMetaDataHash());
        assertEquals("testCoin", asset1.getName());
        assertEquals(8, asset1.getOutputIndex());
        assertEquals(0, asset1.getVersion());
        assertEquals(NORMAL, asset1.getType());
        assertEquals(0, asset1.getAmount().compareTo(ONE));
        // Asset group.
        assertNull(asset1.getAssetGroup());
        assertEquals(assetGroupCount, assetGroupRepository.findAll().size());

        // =============================================================================================================
        // We add a second asset.
        // Creator is not set.
        // The transaction already exists in database.
        // An asset group is set, but it doesn't exist in database for the moment.
        final AssetDTO asset2 = assetService.addAsset(AssetDTO.builder()
                .assetId("assetId2")
                .genesisPoint(bto.get())
                .metaDataHash("metaData2")
                .name("testCoin2")
                .outputIndex(9)
                .version(1)
                .type(NORMAL)
                .amount(new BigInteger("11"))
                .assetGroup(AssetGroupDTO.builder()
                        .assetGroupId("tweakedGroupKey-1")
                        .rawGroupKey("rawGroupKey-1")
                        .tweakedGroupKey("tweakedGroupKey-1")
                        .assetWitness("assetIdSig-1")
                        .build())
                .build());

        // Testing asset value.
        assertNotNull(asset2.getId());
        assertEquals(ANONYMOUS_USER_DTO.getId(), asset2.getCreator().getId());
        assertEquals("assetId2", asset2.getAssetId());
        assertNotNull(asset2.getAssetIdAlias());
        assertEquals(ASSET_ALIAS_LENGTH, asset2.getAssetIdAlias().length());
        // Genesis.
        assertNotNull(asset2.getGenesisPoint());
        assertNotNull(asset2.getGenesisPoint().getId());
        // Asset value data.
        assertEquals("metaData2", asset2.getMetaDataHash());
        assertEquals("testCoin2", asset2.getName());
        assertEquals(9, asset2.getOutputIndex());
        assertEquals(1, asset2.getVersion());
        assertEquals(NORMAL, asset2.getType());
        assertEquals(0, asset2.getAmount().compareTo(new BigInteger("11")));
        // Asset group.
        assertNotNull(asset2.getAssetGroup());
        assertEquals(assetGroupCount + 1, assetGroupRepository.findAll().size());
        assertNotNull(asset2.getAssetGroup().getId());
        assertEquals("tweakedGroupKey-1", asset2.getAssetGroup().getAssetGroupId());
        assertEquals("rawGroupKey-1", asset2.getAssetGroup().getRawGroupKey());
        assertEquals("tweakedGroupKey-1", asset2.getAssetGroup().getTweakedGroupKey());
        assertEquals("assetIdSig-1", asset2.getAssetGroup().getAssetWitness());

        // =============================================================================================================
        // We add a third asset.
        // The transaction already exists in database.
        // An asset group is set, but it already exists in the database.
        // We check that a new asset group is not created.
        final AssetDTO asset3 = assetService.addAsset(AssetDTO.builder()
                .assetId("assetId3")
                .genesisPoint(bto.get())
                .metaDataHash("metaData3")
                .name("testCoin3")
                .outputIndex(9)
                .version(1)
                .type(NORMAL)
                .amount(new BigInteger("111"))
                .assetGroup(AssetGroupDTO.builder()
                        .assetGroupId("tweakedGroupKey-1")
                        .tweakedGroupKey("tweakedGroupKey-1")
                        .rawGroupKey("rawGroupKey-1")
                        .assetWitness("assetIdSig-1")
                        .build())
                .build());
        // Asset group.
        assertNotNull(asset3.getAssetGroup());
        assertEquals(assetGroupCount + 1, assetGroupRepository.findAll().size());
        assertEquals("assetId3", asset3.getAssetId());
        assertNotNull(asset3.getAssetIdAlias());
        assertEquals(ASSET_ALIAS_LENGTH, asset3.getAssetIdAlias().length());
        assertEquals("tweakedGroupKey-1", asset3.getAssetGroup().getTweakedGroupKey());
        assertEquals("assetIdSig-1", asset3.getAssetGroup().getAssetWitness());
        assertEquals("rawGroupKey-1", asset3.getAssetGroup().getRawGroupKey());
        assertEquals("tweakedGroupKey-1", asset3.getAssetGroup().getTweakedGroupKey());
    }

    @Test
    @DisplayName("addAsset() with asset group")
    public void addAssetWithAssetGroup() {
        // We retrieve a bitcoin transaction output from database for our test.
        final Optional<BitcoinTransactionOutputDTO> bto = bitcoinService.getBitcoinTransactionOutput(ROYLLO_COIN_GENESIS_TXID, 0);
        assertTrue(bto.isPresent());

        // 4 assets : 1 with no asset group, 2 with the same asset group and 1 with another asset group.
        AssetDTO asset1 = AssetDTO.builder()
                .assetId("asset10000000000000000000000000000000000000000000000000000000000")
                .genesisPoint(bto.get())
                .build();
        AssetDTO asset2 = AssetDTO.builder()
                .assetId("asset20000000000000000000000000000000000000000000000000000000000")
                .genesisPoint(bto.get())
                .assetGroup(AssetGroupDTO.builder()
                        .assetGroupId("assetGroupId1")
                        .tweakedGroupKey("assetGroup1").build())
                .build();
        AssetDTO asset3 = AssetDTO.builder()
                .assetId("asset30000000000000000000000000000000000000000000000000000000000")
                .genesisPoint(bto.get())
                .assetGroup(AssetGroupDTO.builder()
                        .assetGroupId("assetGroupId1")
                        .tweakedGroupKey("assetGroup1").build())
                .build();
        AssetDTO asset4 = AssetDTO.builder()
                .assetId("asset40000000000000000000000000000000000000000000000000000000000")
                .genesisPoint(bto.get())
                .assetGroup(AssetGroupDTO.builder()
                        .assetGroupId("assetGroupId2")
                        .tweakedGroupKey("assetGroup2").build())
                .build();

        // Asset creation.
        assetService.addAsset(asset1);
        assetService.addAsset(asset2);
        assetService.addAsset(asset3);
        assetService.addAsset(asset4);

        // Asset retrieval.
        AssetDTO asset1Created = assetService.getAssetByAssetId("asset10000000000000000000000000000000000000000000000000000000000").orElse(null);
        AssetDTO asset2Created = assetService.getAssetByAssetId("asset20000000000000000000000000000000000000000000000000000000000").orElse(null);
        AssetDTO asset3Created = assetService.getAssetByAssetId("asset30000000000000000000000000000000000000000000000000000000000").orElse(null);
        AssetDTO asset4Created = assetService.getAssetByAssetId("asset40000000000000000000000000000000000000000000000000000000000").orElse(null);

        // Verification.
        assertNotNull(asset1Created);
        assertNotNull(asset1Created.getId());
        assertNull(asset1Created.getAssetGroup());

        assertNotNull(asset2Created);
        assertNotNull(asset2Created.getId());
        assertNotNull(asset2Created.getAssetGroup());
        assertNotNull(asset2Created.getAssetGroup().getId());
        assertEquals("assetGroup1", asset2Created.getAssetGroup().getTweakedGroupKey());

        assertNotNull(asset3Created);
        assertNotNull(asset3Created.getId());
        assertNotNull(asset3Created.getAssetGroup());
        assertNotNull(asset3Created.getAssetGroup().getId());
        assertEquals("assetGroup1", asset3Created.getAssetGroup().getTweakedGroupKey());

        assertNotNull(asset4Created);
        assertNotNull(asset4Created.getId());
        assertNotNull(asset4Created.getAssetGroup());
        assertNotNull(asset4Created.getAssetGroup().getId());
        assertEquals("assetGroup2", asset4Created.getAssetGroup().getTweakedGroupKey());
    }

    @Test
    @DisplayName("updateAsset()")
    public void updateAsset() {
        // Date used for test.
        LocalDate localDate = LocalDate.of(2019, MARCH, 12);
        LocalTime localTime = LocalTime.of(12, 44);
        ZoneId zoneId = ZoneId.of("GMT+05:30");
        ZonedDateTime testDate = ZonedDateTime.of(localDate, localTime, zoneId);

        // We create an asset.
        final AssetDTO asset1 = assetService.addAsset(AssetDTO.builder()
                .creator(ANONYMOUS_USER_DTO)
                .assetId("asset00000000000000000000000000000000000000000000000000000000001")
                .genesisPoint(BitcoinTransactionOutputDTO.builder()
                        .txId(ROYLLO_COIN_GENESIS_TXID)
                        .vout(0)
                        .build())
                .metaDataHash("my meta data hash")
                .name("testCoin")
                .outputIndex(8)
                .version(0)
                .type(NORMAL)
                .amount(ONE)
                .build());

        // We test the data of our created asset.
        assertNotNull(asset1.getId());
        assertNotNull(asset1.getAssetId());
        assertNull(asset1.getMetaDataFileName());
        assertEquals(0, ONE.compareTo(asset1.getAmount()));
        assertNull(asset1.getIssuanceDate());

        // Now, we update the asset with the metadata, the new amount, the new creation date.
        final String imageMeta = "89504e470d0a1a0a0000000d4948445200000018000000180806000000e0773df80000006e4944415478da63601805a360d880ff38304d0da78a25ff89c4941b5e682f8b81a9660136c3d12ca1cc029b0c75304636182646ae0528ae07d11ef9da182e078951c5025a04d1c059802e36b47c704451008e29cd07ff89f105552dc011f6d4f50135cb229c41448b229ba25403006cf5f5c3b61b973a0000000049454e44ae426082";
        assetService.updateAsset(asset1.getAssetId(), imageMeta, new BigInteger("100"), testDate);

        // We test the data.
        Optional<AssetDTO> assetUpdated = assetService.getAssetByAssetId(asset1.getAssetId());
        assertTrue(assetUpdated.isPresent());
        assertEquals(asset1.getAssetId() + ".png", assetUpdated.get().getMetaDataFileName());
        assertEquals(0, new BigInteger("100").compareTo(assetUpdated.get().getAmount()));
        assertTrue(testDate.isEqual(assetUpdated.get().getIssuanceDate()));

        // The file should be available.
        var client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/" + assetUpdated.get().getMetaDataFileName())
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(200, response.code());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        // We test that it gives a 404 error if you search for a non-existing file.
        request = new Request.Builder()
                .url("http://" + WEB_SERVER_HOST + ":" + WEB_SERVER_PORT + "/NON_EXISTING_FILE.png")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertEquals(404, response.code());
        } catch (IOException e) {
            fail("Error while retrieving the file" + e.getMessage());
        }

        // We update with nothing, nothing should change.
        assetService.updateAsset(asset1.getAssetId(), null, null, null);

        // We test the data.
        assetUpdated = assetService.getAssetByAssetId(asset1.getAssetId());
        assertTrue(assetUpdated.isPresent());
        assertEquals(asset1.getAssetId() + ".png", assetUpdated.get().getMetaDataFileName());
        assertEquals(0, new BigInteger("100").compareTo(assetUpdated.get().getAmount()));
        assertTrue(testDate.isEqual(assetUpdated.get().getIssuanceDate()));

        // =============================================================================================================
        // We test with a JSON File.
        final String adamCoinMetadata = "227b226465736372697074696f6e223a20224120636f696e2064656469636174656420746f204164616d2066726f6d2047656e657369732c20746865206669727374206d616e206f6e2065617274682e20416c736f2061206669727374206173736574206d696e74656420696e20546972616d6973752077616c6c6574206f6e206d61696e6e65742e222c20226e616d65223a20224164616d436f696e222c20226163726f6e796d223a20224143222c202275736572223a20226661756365745f757365725f31222c2022656d61696c223a2022222c20226d696e7465645f7573696e67223a202268747470733a2f2f746573746e65742e7461726f77616c6c65742e6e65742f222c2022696d6167655f64617461223a2022646174613a696d6167652f6a70673b6261736536342c2f396a2f34414151536b5a4a5267414241514141415141424141442f32774244414d694b6c712b57666369766f362f6831636a752f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f327742444164586834662f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f2f7741415243414367414b41444153494141684542417845422f38514148774141415155424151454241514541414141414141414141414543417751464267634943516f4c2f3851417452414141674544417749454177554642415141414146394151494441415152425249684d5545474531466842794a7846444b426b61454949304b78775256533066416b4d324a7967676b4b4668635947526f6c4a69636f4b536f304e5459334f446b3651305246526b644953557054564656575631685a576d4e6b5a575a6e61476c7163335231646e643465587144684957476834694a69704b546c4a57576c35695a6d714b6a704b576d7036697071724b7a744c57327437693575734c44784d584778386a4a79744c54314e585731396a5a32754869342b546c3575666f3665727838765030396662332b506e362f38514148774541417745424151454241514542415141414141414141414543417751464267634943516f4c2f385141745245414167454342415144424163464241514141514a3341414543417845454253457842684a425551646863524d694d6f454946454b526f62484243534d7a55764156596e4c524368596b4e4f456c3852635947526f6d4a7967704b6a55324e7a67354f6b4e4552555a4853456c4b55315256566c64595756706a5a47566d5a326870616e4e3064585a3365486c36676f4f456859614869496d4b6b704f556c5a61586d4a6d616f714f6b7061616e714b6d7173724f3074626133754c6d367773504578636248794d6e4b3074505531646258324e6e613475506b3565626e364f6e7138765030396662332b506e362f396f4144414d4241414952417845415077434d6a42704b6d70724c6e703170697552305575302b6c49526a725347464646464142525252514155555555414646464641425252525141555555554146464646414252525251424e52535a48725453343755795239495642706d382b6c42633044734e49776355555539597965547750317044475534527365325072556f3272393066352b74495750722b564b3444664b50714b504a392f3070324365782f45306667507a6f4161596a36696d6c4748616e382b2f35307566582f43693479476970574150616d4663644b5968744646464142525252514155555555414646464641425252556b532f784838503861414652416f334e312f6c2f77445870784f66384b43632f7742422f582f5051556f475063306745436b396550616e594136554667426b314730685033525141386e74546161417a594f65445432544a42427869697777413570574978696d5959444f546e7350386154635239345557415870307065473664615145487052534161792b6c4d71587231707369344f5254454d6f6f6f7067464646464142525252514171727559437032394f77362f30464d68484262384b643149392b542f414a2f4b67425648633936474f31636d6e564337626a6a4f425373416e332b57622f363153717058766b5647713835794d65347151664b4f76483871594471544e423745476a48507451415647344a4a39425478366b2b76383652766d484641454979447855696e497068427a78316f35567552696b41382b7448576c7050616b4d6a497763556c4f666e42707455494b4b4b4b4143696969674364526949652f7744576c48336a2b5648384b2f682f4b68652f314e4a674448436d6f534d6438314d78414850725565437a4768414b754d444834342f6e2b46504f4d41446b31477041552b7450586b6261594337654b43636744756154357830352b7638416a536b5935787a514162514254547a2f414a37555964757449526a6967426a636367307056334737673030395451474936476743526334356f50576b5535424a3961576b4d61333354544b6c49345030714b6843436969696d41555555554154352b5666772f6c5372332b744d5535516533394b6550764833352f7a2b6c5377456b2b35544e35505370534d67696f56516e706a696d67486244672b76387853786e6e3844516f4b484a4f6330787547794b5945394a6b35786a38615253536f497853382b3141436e70554a4f4455684f4279616a54356d79653141434d75464761622b465062356a545370484e41446b2b37532f77434e47414647442b4648656b4d582b467638397159553944542f414f48366d696d684d6a3247676f52556c464d5679476969696b4d66476531536476702f4c2f3841562f4b6f4163484e5441385a2f77412f35464a6753557a6f782f4f6c48484835663455704761414774794b615275464b66656b397159434258586c66307054493354627a533434366b5530357a316f41546c75744f4879703961546236304830394b41416355756565656c4a547541754f357044457a514f6e316f417a394b584f4f6151444a4479414f314d6f4a796330565168636e3170435365706f6f6f414b4b4b4b41436e6f324f4430706c4641453252305054742f6e2b564f42787733352f3537314372646a55675048504970414b787a785463656c4c6a2b36632f352f774139615436385544444a704f63394b576c41396141454753616474413638306f474254533265425341516e4a6f41794f507850394253376637332b663841507451547836436741506f4f6c5275326542306f5a73384470546159676f6f6f706746464646414252525251415555555541464b435230707972334e506f734b34774d506f6165435436476b363030726a6c614c447550494864615441715065337253377a53416b7750536c336363635644755072535a7a525943517550716159574a704b4b5942525252514155555555414646464641425467704e4b4550656e3078584762423730465051302b696756776f6f6f6f414b4b4b4b414954316f71586150536a6150536764794b696e4d6d4f52546151776f6f6f6f414b4b4b4b414369696967416f705655734f4b556f5251422f2f396b3d227d22";
        assetService.updateAsset(asset1.getAssetId(), adamCoinMetadata, null, null);

        // We test the data.
        assetUpdated = assetService.getAssetByAssetId(asset1.getAssetId());
        assertTrue(assetUpdated.isPresent());
        assertEquals(asset1.getAssetId() + ".json", assetUpdated.get().getMetaDataFileName());
        assertEquals(JSON, assetUpdated.get().getMetaDataFileType());
        assertEquals(0, new BigInteger("100").compareTo(assetUpdated.get().getAmount()));
        assertTrue(testDate.isEqual(assetUpdated.get().getIssuanceDate()));
    }

    @Test
    @DisplayName("getAsset()")
    public void getAsset() {
        // =============================================================================================================
        // Non-existing asset.
        Optional<AssetDTO> asset = assetService.getAsset(0);
        assertFalse(asset.isPresent());

        // =============================================================================================================
        // Existing asset on testnet and in our database initialization script ("My Royllo coin") .
        // Asset id is 1 as My Royllo Coin is the only coin inserted in default database.
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertEquals(ROYLLO_COIN_ASSET_ID, asset.get().getAssetId());
        assertEquals("roylloCoin", asset.get().getAssetIdAlias());
        assertNotNull(asset.get().getCreator());
        assertEquals(ANONYMOUS_ID, asset.get().getCreator().getId());
        verifyAsset(asset.get(), ROYLLO_COIN_ASSET_ID);

        // getAsset() on an asset that has no asset group
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertNull(asset.get().getAssetGroup());
    }

    @Test
    @DisplayName("getAssetByAssetId()")
    public void getAssetByAssetId() {
        // =============================================================================================================
        // Non-existing asset.
        Optional<AssetDTO> asset = assetService.getAssetByAssetId("NON_EXISTING_ASSET_ID");
        assertFalse(asset.isPresent());

        // =============================================================================================================
        // Existing asset on testnet and in our database initialization script ("roylloCoin") .
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertEquals(ROYLLO_COIN_ASSET_ID, asset.get().getAssetId());
        assertNotNull(asset.get().getCreator());
        assertEquals(ANONYMOUS_ID, asset.get().getCreator().getId());
        verifyAsset(asset.get(), ROYLLO_COIN_ASSET_ID);
        assertEquals(TEXT, asset.get().getMetaDataFileType());

        // getAsset() on an asset that has no asset group
        asset = assetService.getAsset(1);
        assertTrue(asset.isPresent());
        assertNull(asset.get().getAssetGroup());

        // Testing another asset in test data.
        asset = assetService.getAssetByAssetId(ROYLLO_NFT_ASSET_ID);
        assertTrue(asset.isPresent());
        assertNotNull(asset.get().getAssetId());
        assertEquals(ROYLLO_NFT_ASSET_ID_ALIAS, asset.get().getAssetIdAlias());
        assertEquals(IMAGE, asset.get().getMetaDataFileType());

        // Testing with an asset id alias
        asset = assetService.getAssetByAssetId(SET_OF_ROYLLO_NFT_2_ASSET_ID_ALIAS);
        assertTrue(asset.isPresent());
        assertNotNull(asset.get().getAssetId());
        assertEquals(SET_OF_ROYLLO_NFT_2_ASSET_ID, asset.get().getAssetId());
        assertEquals(SET_OF_ROYLLO_NFT_2_ASSET_ID_ALIAS, asset.get().getAssetIdAlias());
        assertEquals(UNKNOWN, asset.get().getMetaDataFileType());
    }

    @Test
    @DisplayName("getAssetByAssetId()")
    public void getAssetsByAssetGroupId() {
        // Test with an asset group that doesn't exist.
        assertEquals(0, assetService.getAssetsByAssetGroupId("NON_EXISTING_ASSET_GROUP_ID", 1, 5).getTotalElements());

        // Test with an asset group with three assets.
        final String tweakedGroupKey = SET_OF_ROYLLO_NFT_1_FROM_TEST.getDecodedProofResponse(0).getAsset().getAssetGroup().getTweakedGroupKey();
        // with a page size containing everything.
        assertEquals(3, assetService.getAssetsByAssetGroupId(tweakedGroupKey, 1, 5).getTotalElements());
        assertEquals(1, assetService.getAssetsByAssetGroupId(tweakedGroupKey, 1, 5).getTotalPages());
    }


    @Test
    @DisplayName("getAssetsByUsername()")
    public void getAssetsByUsername() {
        // Straumat has 2 assets.
        Page<AssetDTO> straumatAssets = assetService.getAssetsByUsername("straumat", 1, 10);
        assertNotNull(straumatAssets);
        assertEquals(2, straumatAssets.getTotalElements());
        assertTrue(straumatAssets.stream().anyMatch(assetDTO -> SET_OF_ROYLLO_NFT_2_ASSET_ID.equals(assetDTO.getAssetId())));
        assertTrue(straumatAssets.stream().anyMatch(assetDTO -> TRICKY_ROYLLO_COIN_ASSET_ID.equals(assetDTO.getAssetId())));

        // newUser has 1 asset.
        Page<AssetDTO> newUserAssets = assetService.getAssetsByUsername("newUser", 1, 10);
        assertNotNull(newUserAssets);
        assertEquals(1, newUserAssets.getTotalElements());
        assertTrue(newUserAssets.stream().anyMatch(assetDTO -> SET_OF_ROYLLO_NFT_3_ASSET_ID.equals(assetDTO.getAssetId())));
    }

}
