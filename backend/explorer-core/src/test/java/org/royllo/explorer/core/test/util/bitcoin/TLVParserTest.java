package org.royllo.explorer.core.test.util.bitcoin;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class TLVParserTest {

    @Test
    public void test() {

    }

    /**
     * Reads TLV values for a given hex string.
     */
    public static byte[][] readTLV(String tlvHexString, int tag) {
        return readTLV(hexStringToByteArray(tlvHexString), tag);
    }

    /**
     * Reads TLV values for a given byte array.
     */
    public static byte[][] readTLV(byte[] tlv, int tag) {
        if (tlv == null || tlv.length < 1) {
            throw new IllegalArgumentException("Invalid TLV");
        }

        int c = 0;
        ArrayList al = new ArrayList();

        ByteArrayInputStream is = null;
        try {
            is = new ByteArrayInputStream(tlv);

            while ((c = is.read()) != -1) {
                if (c == tag){
                    if ((c = is.read()) != -1){
                        byte[] value = new byte[c];
                        is.read(value,0,c);
                        al.add(value);
                    }
                }
            }
        } finally {
            if (is != null) {
                try{
                    is.close();
                }catch (IOException e){
                    System.out.println("=> " + e.getMessage());
                }
            }
        }
        System.out.println("Got " + al.size() + " values for tag "
                + Integer.toHexString(tag));
        byte[][] vals = new byte[al.size()][];
        al.toArray(vals);
        return vals;
    }

    /**
     * Converts a hex string to byte array.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
