package org.royllo.explorer.core.provider.storage;

/**
 * File service.
 */
public interface FileService {

    /**
     * Store file.
     *
     * @param fileContent file content
     * @param fileName    file name
     */
    void storeFile(byte[] fileContent, String fileName);

}
