package rgo.cloud.docs.boot.util;

public final class FileUtil {
    private FileUtil() {
    }

    public static String getFileExtension(String fullFileName) {
        if (!fullFileName.contains(".")) {
            return "";
        }

        return fullFileName.substring(fullFileName.lastIndexOf(".") + 1);
    }

    public static String getFileName(String fullFileName) {
        if (!fullFileName.contains(".")) {
            return fullFileName;
        }

        return fullFileName.substring(0, fullFileName.lastIndexOf("."));
    }
}
