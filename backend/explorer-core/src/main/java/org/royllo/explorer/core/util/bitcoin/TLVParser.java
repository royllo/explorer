package org.royllo.explorer.core.util.bitcoin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class responsible for parsing an encoded-text in TLV (Tag-Length-Value) format.
 */
public class TLVParser {

    /** Logger. */
    private static final Logger LOGGER = LogManager.getLogger(TLVParser.class);

    /**
     * The minimum TLV size allowed.
     */
    private static final int MIN_TLV_SIZE = 7;

    /**
     * Holds the original TLV encoded-text.
     */
    private final String tlv;

    /**
     * Holds all tags keeping their insertion order.
     */
    private final Map<String, String> tlvData = new LinkedHashMap<>();


    /**
     * Creates a parser for the specified <code>tlv</code>.
     *
     * @param newTlv the TLV encoded-text that will be parsed
     */
    public TLVParser(final String newTlv) {
        this.tlv = newTlv;
        parses(newTlv);
    }

    /**
     * Parses TLV encoded-text into a Map.
     *
     * @param newTlv  tlv
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    private void parses(final String newTlv) {

        if (newTlv == null) {
            LOGGER.error("Invalid TLV data: can not be null");
            return;
        }

        if (newTlv.length() < MIN_TLV_SIZE) {
            LOGGER.error("Invalid TLV data: must be at least 7 characters long (tlv='" + newTlv + "')");
            return;
        }

        try {
            int i = 0;
            do {
                String tag = tlv.substring(i, i + 3);
                int len = Integer.parseInt(tlv.substring(i + 3, i + 6));
                String value = tlv.substring(i + 6, i + 6 + len);

                tlvData.put(tag, value);

                i = i + 6 + len;
            } while (i < tlv.length());
        } catch (Exception e) {
            LOGGER.error("Unexpected error tlv='" + newTlv + "' : " + e.getMessage());
        }
    }

    /**
     * Finds the tag value by the specified <code>tagName</code>.
     *
     * @param newTagName the tag name
     * @return the tag value or <code>null</code> if not found
     */
    public String getTag(final String newTagName) {
        return tlvData.get(newTagName);
    }

    /**
     * Finds the tag value by the specified <code>tagName</code>. In case the tag is
     * not found, returns the <code>defaultValue</code>.
     *
     * @param tagName      the tag name
     * @param defaultValue the default value that will used in case a tag is not
     *                     found
     * @return the tag value or <code>defaultValue</code> if not found
     */
    public String getTag(final String tagName, final String defaultValue) {
        String tagValue = getTag(tagName);
        if (tagValue == null) {
            return defaultValue;
        }
        return tagValue;
    }

    /**
     * Returns the total of tags found.
     *
     * @return int
     */
    public int getTotalOfTags() {
        return tlvData.size();
    }

    @SuppressWarnings("checkstyle:DesignForExtension")
    @Override
    public String toString() {
        return "TLVParser [tlv=" + tlv
                + ", totalOfTags=" + getTotalOfTags()
                + ", tlvData=" + tlvData
                + "]";
    }

}
