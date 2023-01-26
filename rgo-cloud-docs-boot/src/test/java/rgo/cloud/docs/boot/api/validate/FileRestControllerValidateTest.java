package rgo.cloud.docs.boot.api.validate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import rgo.cloud.common.api.rest.StatusCode;
import rgo.cloud.common.spring.test.CommonTest;
import rgo.cloud.security.config.util.Endpoint;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static rgo.cloud.common.api.util.RequestUtil.JSON;
import static rgo.cloud.docs.boot.FileGenerator.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class FileRestControllerValidateTest extends CommonTest {

    @BeforeEach
    public void setUp() {
      initMvc();
    }

    @Test
    public void findByDocumentId_idIsNotPositive() throws Exception {
        long documentId = -1L;
        String errorMessage = "The documentId is not positive.";

        mvc.perform(get(Endpoint.File.BASE_URL + "/" + documentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void findByClassificationId_idIsNotPositive() throws Exception {
        long classificationId = -1L;
        String errorMessage = "The classificationId is not positive.";

        mvc.perform(get(Endpoint.File.BASE_URL + "?classificationId=" + classificationId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void getFreeLanguages_idIsNotPositive() throws Exception {
        long documentId = -1L;
        String errorMessage = "The documentId is not positive.";

        mvc.perform(get(Endpoint.File.BASE_URL + "/free-languages/" + documentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void findResource_documentIdIsNotPositive() throws Exception {
        long documentId = -1L;
        long languageId = 1L;
        String errorMessage = "The documentId is not positive.";

        mvc.perform(get(Endpoint.File.BASE_URL + Endpoint.File.RESOURCE + "?documentId=" + documentId + "&languageId=" + languageId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void findResource_languageIdIsNotPositive() throws Exception {
        long documentId = 1L;
        long languageId = -1L;
        String errorMessage = "The languageId is not positive.";

        mvc.perform(get(Endpoint.File.BASE_URL + Endpoint.File.RESOURCE + "?documentId=" + documentId + "&languageId=" + languageId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void findResource_documentIdAndLanguageIdAreNotPositive() throws Exception {
        long documentId = -1L;
        long languageId = -1L;
        String errorMessage = "The documentId is not positive. The languageId is not positive.";

        mvc.perform(get(Endpoint.File.BASE_URL + Endpoint.File.RESOURCE + "?documentId=" + documentId + "&languageId=" + languageId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }
    
    @Test
    public void save_languageIdIsNotPositive() throws Exception {
        String classificationId = "1";
        String languageId = "-1";
        MockMultipartFile file = createFile();
        String errorMessage = "The languageId is not positive.";

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", languageId)
                .param("classificationId", classificationId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void save_classificationIdIsNotPositive() throws Exception {
        String classificationId = "-1";
        String languageId = "1";
        MockMultipartFile file = createFile();
        String errorMessage = "The classificationId is not positive.";

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", languageId)
                .param("classificationId", classificationId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void save_fileIsEmpty() throws Exception {
        String classificationId = "1";
        String languageId = "1";
        MockMultipartFile file = createEmptyFile();
        String errorMessage = "The file is missing.";

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", languageId)
                .param("classificationId", classificationId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void save_fileNameIsEmpty() throws Exception {
        String classificationId = "1";
        String languageId = "1";
        MockMultipartFile file = createFileWithEmptyName();
        String errorMessage = "The fileName is empty.";

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", languageId)
                .param("classificationId", classificationId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void save_extensionUnsupported() throws Exception {
        String classificationId = "1";
        String languageId = "1";
        MockMultipartFile file = createFileWithUnsupportedExtension();
        String errorMessage = "The file extension not supported.";

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", languageId)
                .param("classificationId", classificationId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void save_fileIsBig() throws Exception {
        String classificationId = "1";
        String languageId = "1";
        MockMultipartFile file = createBigFile();
        String errorMessage = "The file size exceeds 5 MB.";

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", languageId)
                .param("classificationId", classificationId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void patch_languageIdIsNotPositive() throws Exception {
        String documentId = "1";
        String languageId = "-1";
        MockMultipartFile file = createFile();
        String errorMessage = "The languageId is not positive.";

        mvc.perform(multipartPatch()
                .file(file)
                .param("languageId", languageId)
                .param("documentId", documentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void patch_documentIdIsNotPositive() throws Exception {
        String documentId = "-1";
        String languageId = "1";
        MockMultipartFile file = createFile();
        String errorMessage = "The documentId is not positive.";

        mvc.perform(multipartPatch()
                .file(file)
                .param("languageId", languageId)
                .param("documentId", documentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void patch_fileIsEmpty() throws Exception {
        String documentId = "1";
        String languageId = "1";
        MockMultipartFile file = createEmptyFile();
        String errorMessage = "The file is missing.";

        mvc.perform(multipartPatch()
                .file(file)
                .param("languageId", languageId)
                .param("documentId", documentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void patch_fileNameIsEmpty() throws Exception {
        String documentId = "1";
        String languageId = "1";
        MockMultipartFile file = createFileWithEmptyName();
        String errorMessage = "The fileName is empty.";

        mvc.perform(multipartPatch()
                .file(file)
                .param("languageId", languageId)
                .param("documentId", documentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void patch_extensionUnsupported() throws Exception {
        String documentId = "1";
        String languageId = "1";
        MockMultipartFile file = createFileWithUnsupportedExtension();
        String errorMessage = "The file extension not supported.";

        mvc.perform(multipartPatch()
                .file(file)
                .param("languageId", languageId)
                .param("documentId", documentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void patch_fileIsBig() throws Exception {
        String documentId = "1";
        String languageId = "1";
        MockMultipartFile file = createBigFile();
        String errorMessage = "The file size exceeds 5 MB.";

        mvc.perform(multipartPatch()
                .file(file)
                .param("languageId", languageId)
                .param("documentId", documentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void deleteByDocumentIdAndLanguageId_documentIdIsNotPositive() throws Exception {
        long documentId = -1L;
        long languageId = 1L;
        String errorMessage = "The documentId is not positive.";

        mvc.perform(delete(Endpoint.File.BASE_URL + "?documentId=" + documentId + "&languageId=" + languageId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void deleteByDocumentIdAndLanguageId_languageIdIsNotPositive() throws Exception {
        long documentId = 1L;
        long languageId = -1L;
        String errorMessage = "The languageId is not positive.";

        mvc.perform(delete(Endpoint.File.BASE_URL + "?documentId=" + documentId + "&languageId=" + languageId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void deleteByDocumentId_documentIdIsNotPositive() throws Exception {
        long documentId = -1L;
        String errorMessage = "The documentId is not positive.";

        mvc.perform(delete(Endpoint.File.BASE_URL + "/" + documentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }
} 
