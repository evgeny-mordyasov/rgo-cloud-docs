package rgo.cloud.docs.boot.api.permit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import rgo.cloud.common.api.model.Role;
import rgo.cloud.common.api.rest.StatusCode;
import rgo.cloud.common.spring.test.CommonTest;
import rgo.cloud.security.config.domain.ClientDetails;
import rgo.cloud.security.config.jwt.JwtProvider;
import rgo.cloud.security.config.jwt.properties.JwtProperties;
import rgo.cloud.security.config.util.Endpoint;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static rgo.cloud.common.spring.util.RequestUtil.JSON;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;
import static rgo.cloud.docs.boot.FileGenerator.createFile;
import static rgo.cloud.docs.boot.FileGenerator.multipartPatch;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class FileRestControllerPermitTest extends CommonTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtProperties config;

    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setup() throws Exception {
        truncateTables();
        initSecurityMvc();
    }

    @Test
    public void findAll_success_anonymous() throws Exception {
        mvc.perform(get(Endpoint.File.BASE_URL))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findAll_success_client() throws Exception {
        String jwt = createJwt(Role.USER);

        mvc.perform(get(Endpoint.File.BASE_URL)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findAll_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);

        mvc.perform(get(Endpoint.File.BASE_URL)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findByDocumentId_success_anonymous() throws Exception {
        long fakeDocumentId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "/" + fakeDocumentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findByDocumentId_success_client() throws Exception {
        String jwt = createJwt(Role.USER);
        long fakeDocumentId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "/" + fakeDocumentId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findByDocumentId_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        long fakeDocumentId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "/" + fakeDocumentId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findByClassificationId_success_anonymous() throws Exception {
        long fakeClassificationId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "?classificationId=" + fakeClassificationId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findByClassificationId_success_client() throws Exception {
        String jwt = createJwt(Role.USER);
        long fakeClassificationId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "?classificationId=" + fakeClassificationId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findByClassificationId_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        long fakeClassificationId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "?classificationId=" + fakeClassificationId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void getFreeLanguages_success_anonymous() throws Exception {
        long fakeDocumentId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "/free-languages/" + fakeDocumentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    @Test
    public void getFreeLanguages_success_client() throws Exception {
        String jwt = createJwt(Role.USER);
        long fakeDocumentId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "/free-languages/" + fakeDocumentId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    @Test
    public void getFreeLanguages_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        long fakeDocumentId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "/free-languages/" + fakeDocumentId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    @Test
    public void findResource_unauthorized_anonymous() throws Exception {
        long documentId = generateId();
        long languageId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + Endpoint.File.RESOURCE + "?documentId=" + documentId + "&languageId=" + languageId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.UNAUTHORIZED.name())));
    }

    @Test
    public void findResource_success_client() throws Exception {
        String jwt = createJwt(Role.USER);
        long documentId = generateId();
        long languageId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + Endpoint.File.RESOURCE + "?documentId=" + documentId + "&languageId=" + languageId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    @Test
    public void findResource_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        long documentId = generateId();
        long languageId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + Endpoint.File.RESOURCE + "?documentId=" + documentId + "&languageId=" + languageId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    @Test
    public void save_unauthorized_anonymous() throws Exception {
        long classificationId = generateId();
        long languageId = generateId();
        MockMultipartFile file = createFile();

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", Long.toString(languageId))
                .param("classificationId", Long.toString(classificationId)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.UNAUTHORIZED.name())));
    }

    @Test
    public void save_forbidden_client() throws Exception {
        String jwt = createJwt(Role.USER);
        long classificationId = generateId();
        long languageId = generateId();
        MockMultipartFile file = createFile();

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", Long.toString(languageId))
                .param("classificationId", Long.toString(classificationId))
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void save_forbidden_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        long classificationId = generateId();
        long languageId = generateId();
        MockMultipartFile file = createFile();

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", Long.toString(languageId))
                .param("classificationId", Long.toString(classificationId))
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    @Test
    public void patch_unauthorized_anonymous() throws Exception {
        long documentId = generateId();
        long languageId = generateId();
        MockMultipartFile file = createFile();

        mvc.perform(multipartPatch()
                .file(file)
                .param("languageId", Long.toString(languageId))
                .param("documentId", Long.toString(documentId)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.UNAUTHORIZED.name())));
    }

    @Test
    public void patch_forbidden_client() throws Exception {
        String jwt = createJwt(Role.USER);
        long documentId = generateId();
        long languageId = generateId();
        MockMultipartFile file = createFile();

        mvc.perform(multipartPatch()
                .file(file)
                .param("languageId", Long.toString(languageId))
                .param("documentId", Long.toString(documentId))
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void patch_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        long documentId = generateId();
        long languageId = generateId();
        MockMultipartFile file = createFile();

        mvc.perform(multipartPatch()
                .file(file)
                .param("languageId", Long.toString(languageId))
                .param("documentId", Long.toString(documentId))
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    @Test
    public void patchFileName_unauthorized_anonymous() throws Exception {
        long documentId = generateId();
        String fileName = randomString();

        mvc.perform(patch(Endpoint.File.BASE_URL + Endpoint.File.UPDATE_NAME)
                .param("documentId", Long.toString(documentId))
                .param("fileName", fileName))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.UNAUTHORIZED.name())));
    }

    @Test
    public void patchFileName_forbidden_client() throws Exception {
        String jwt = createJwt(Role.USER);
        long documentId = generateId();
        String fileName = randomString();

        mvc.perform(patch(Endpoint.File.BASE_URL + Endpoint.File.UPDATE_NAME)
                .param("documentId", Long.toString(documentId))
                .param("fileName", fileName)
                .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void patchFileName_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        long documentId = generateId();
        String fileName = randomString();

        mvc.perform(patch(Endpoint.File.BASE_URL + Endpoint.File.UPDATE_NAME)
                .param("documentId", Long.toString(documentId))
                .param("fileName", fileName)
                .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    @Test
    public void deleteByKey_unauthorized_anonymous() throws Exception {
        long documentId = generateId();
        long languageId = generateId();

        mvc.perform(delete(Endpoint.File.BASE_URL + "?documentId=" + documentId + "&languageId=" + languageId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.UNAUTHORIZED.name())));
    }

    @Test
    public void deleteByKey_forbidden_client() throws Exception {
        String jwt = createJwt(Role.USER);
        long documentId = generateId();
        long languageId = generateId();

        mvc.perform(delete(Endpoint.File.BASE_URL + "?documentId=" + documentId + "&languageId=" + languageId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void deleteByKey_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        long documentId = generateId();
        long languageId = generateId();

        mvc.perform(delete(Endpoint.File.BASE_URL + "?documentId=" + documentId + "&languageId=" + languageId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    @Test
    public void deleteByDocumentId_unauthorized_anonymous() throws Exception {
        long documentId = generateId();

        mvc.perform(delete(Endpoint.File.BASE_URL + "/" + documentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.UNAUTHORIZED.name())));
    }

    @Test
    public void deleteByDocumentId_forbidden_client() throws Exception {
        String jwt = createJwt(Role.USER);
        long documentId = generateId();

        mvc.perform(delete(Endpoint.File.BASE_URL + "/" + documentId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void deleteByDocumentId_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        long documentId = generateId();

        mvc.perform(delete(Endpoint.File.BASE_URL + "/" + documentId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    private String createJwt(Role role) {
        String mail = randomString();
        ClientDetails client = new ClientDetails(
                new ClientDetails.Client(generateId(), mail, randomString(), role, true));
        when(userDetailsService.loadUserByUsername(any())).thenReturn(client);

        return jwtProvider.createToken(mail);
    }
}
