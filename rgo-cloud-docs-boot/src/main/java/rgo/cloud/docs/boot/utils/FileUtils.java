package rgo.cloud.docs.boot.utils;

public final class FileUtils {
    private FileUtils() {
    }

    public static String getFileExtension(String fullFileName) {
        if (fullFileName == null || !fullFileName.contains(".")) {
            return "";
        }

        return fullFileName.substring(fullFileName.lastIndexOf(".") + 1);
    }

    public static String getFileName(String fullFileName) {
        if (fullFileName == null) {
            return "";
        }

        if (!fullFileName.contains(".")) {
            return fullFileName;
        }

        return fullFileName.substring(0, fullFileName.lastIndexOf("."));
    }
}
