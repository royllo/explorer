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

}
