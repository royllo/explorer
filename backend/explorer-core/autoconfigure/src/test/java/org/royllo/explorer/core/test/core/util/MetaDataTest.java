package org.royllo.explorer.core.test.core.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Meta data test class.
 */
@DisplayName("Meta data tests")
public class MetaDataTest {

    @Test
    @DisplayName("Decoding meta data")
    public void decodingMetaData() {

        // =============================================================================================================
        // Punk image on mainnet.
        // 5630236058450d73ef22ca1e4bea24b5fc05f98688fa6502775f79275445222f
        // https://explorer.royllo.org/asset/5630236058450d73ef22ca1e4bea24b5fc05f98688fa6502775f79275445222f
        final String punkMeta = "89504e470d0a1a0a0000000d4948445200000018000000180806000000e0773df80000006e4944415478da63601805a360d880ff38304d0da78a25ff89c4941b5e682f8b81a9660136c3d12ca1cc029b0c75304636182646ae0528ae07d11ef9da182e078951c5025a04d1c059802e36b47c704451008e29cd07ff89f105552dc011f6d4f50135cb229c41448b229ba25403006cf5f5c3b61b973a0000000049454e44ae426082";

        // Converting the meta and knowing what it is.
        try {

            // Decoding (same as using xxd -r -p)
            byte[] decodedBytes = Hex.decodeHex(punkMeta);

            // Detecting the type of file.
            final String mimeType = new Tika().detect(decodedBytes);

            // Testing the mime type.
            assertEquals("image/png", mimeType);
            assertEquals(".png", MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension());

        } catch (DecoderException | MimeTypeException e) {
            throw new RuntimeException(e);
        }

        // =============================================================================================================
        // Royllo coin on mainnet.
        final String roylloCoinMeta = "726f796c6c6f436f696e206f6e206d61696e6e657420627920526f796c6c6f";

        // Converting the meta and knowing what it is.
        try {

            // Decoding (same as using xxd -r -p)
            byte[] decodedBytes = Hex.decodeHex(roylloCoinMeta);

            // Detecting the type of file.
            final String mimeType = new Tika().detect(decodedBytes);

            // Testing the mime type.
            assertEquals("text/plain", mimeType);
            assertEquals(".txt", MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension());

        } catch (DecoderException | MimeTypeException e) {
            throw new RuntimeException(e);
        }

    }

}
