package org.royllo.explorer.core.util.enums;

/**
 * File type.
 */
public enum FileType {

    /** Image file. */
    IMAGE("png", "jpg", "jpeg", "gif", "bmp"),

    /** Text file. */
    TEXT("txt"),

    /** JSON file. */
    JSON("json"),

    /** Video file. */
    VIDEO("mp4", "avi", "mkv", "mov", "wmv"),

    /** Audio file. */
    AUDIO("mp3", "wav", "ogg", "flac", "aac", "wma"),

    /** PDF file. */
    PDF("pdf"),

    /** Unknown file. */
    UNKNOWN("");

    /** Extensions. */
    private final String[] extensions;

    /**
     * Constructor.
     *
     * @param newExtensions extensions
     */
    FileType(final String... newExtensions) {
        this.extensions = newExtensions;
    }

    /**
     * Returns the type for the given extension.
     *
     * @param extension extension, for example : "png"
     * @return type
     */
    public static FileType getTypeByExtension(final String extension) {
        for (FileType fileType : FileType.values()) {
            for (String ext : fileType.extensions) {
                if (ext.equalsIgnoreCase(extension)) {
                    return fileType;
                }
            }
        }
        return UNKNOWN;
    }

}
