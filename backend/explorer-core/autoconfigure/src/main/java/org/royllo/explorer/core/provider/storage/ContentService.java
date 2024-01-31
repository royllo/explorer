package org.royllo.explorer.core.provider.storage;

/**
 * Content service.
 */
public interface ContentService {

    /**
     * Store a file.
     *
     * @param fileContent file content
     * @param fileName    file name
     */
    void storeFile(byte[] fileContent, String fileName);

    /**
     * Check if a file exists.
     *
     * @param fileName file name
     * @return true if file exists, false otherwise
     */
    boolean fileExists(String fileName);

    /**
     * Delete a file.
     *
     * @param fileName file name
     */
    void deleteFile(String fileName);

}
