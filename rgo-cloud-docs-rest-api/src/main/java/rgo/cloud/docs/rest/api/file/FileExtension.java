package rgo.cloud.docs.rest.api.file;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum FileExtension {
    DOC,
    DOCX,
    RTF,
    TXT,
    PDF;

    private static final Set<String> extensions = Arrays.stream(FileExtension.values())
            .map(v -> v.name().toLowerCase(Locale.ENGLISH))
            .collect(Collectors.toUnmodifiableSet());

    public static boolean isFromWhitelist(String ext) {
        return extensions.contains(
                ext.toLowerCase(Locale.ENGLISH));
    }
}
