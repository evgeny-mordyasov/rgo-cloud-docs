package rgo.cloud.docs.boot;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rgo.cloud.security.config.util.Endpoint;

import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;
import static rgo.cloud.docs.boot.EntityGenerator.generateExtension;

public final class FileGenerator {
    private FileGenerator() {
    }

    public static MockMultipartFile createFile() {
        return new MockMultipartFile(
                "file",
                randomString() + "." + generateExtension(),
                MediaType.TEXT_PLAIN_VALUE,
                randomString().getBytes()
        );
    }

    public static MockMultipartFile createEmptyFile() {
        return new MockMultipartFile(
                "file",
                randomString() + "." + generateExtension(),
                MediaType.TEXT_PLAIN_VALUE,
                new byte[]{}
        );
    }

    public static MockMultipartFile createFileWithEmptyName() {
        return new MockMultipartFile(
                "file",
                "",
                MediaType.TEXT_PLAIN_VALUE,
                randomString().getBytes()
        );
    }

    public static MockMultipartFile createFileWithUnsupportedExtension() {
        return new MockMultipartFile(
                "file",
                randomString() + "." + randomString(),
                MediaType.TEXT_PLAIN_VALUE,
                randomString().getBytes()
        );
    }

    public static MockMultipartFile createBigFile() {
        return new MockMultipartFile(
                "file",
                randomString() + "." + generateExtension(),
                MediaType.TEXT_PLAIN_VALUE,
                RandomUtils.nextBytes(25_000_000)
        );
    }

    public static MockMultipartHttpServletRequestBuilder multipartPatch() {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(Endpoint.File.BASE_URL);

        builder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        return builder;
    }
}
