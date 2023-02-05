package rgo.cloud.docs.boot.api.permit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import rgo.cloud.common.api.model.Role;
import rgo.cloud.common.api.rest.StatusCode;
import rgo.cloud.common.spring.test.CommonTest;
import rgo.cloud.docs.rest.api.classification.request.ClassificationSaveRequest;
import rgo.cloud.docs.rest.api.classification.request.ClassificationUpdateRequest;
import rgo.cloud.security.config.domain.ClientDetails;
import rgo.cloud.security.config.jwt.JwtProvider;
import rgo.cloud.security.config.jwt.properties.JwtProperties;
import rgo.cloud.security.config.util.Endpoint;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static rgo.cloud.common.api.util.JsonUtil.toJson;
import static rgo.cloud.common.api.util.RequestUtil.JSON;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class ClassificationRestControllerPermitTest extends CommonTest {

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
        mvc.perform(get(Endpoint.Classification.BASE_URL))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findAll_success_client() throws Exception {
        String jwt = createJwt(Role.USER);

        mvc.perform(get(Endpoint.Classification.BASE_URL)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findAll_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);

        mvc.perform(get(Endpoint.Classification.BASE_URL)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findById_success_anonymous() throws Exception {
        long fakeId = generateId();

        mvc.perform(get(Endpoint.Classification.BASE_URL + "/" + fakeId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findById_success_client() throws Exception {
        String jwt = createJwt(Role.USER);
        long fakeId = generateId();

        mvc.perform(get(Endpoint.Classification.BASE_URL + "/" + fakeId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findById_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        long fakeId = generateId();

        mvc.perform(get(Endpoint.Classification.BASE_URL + "/" + fakeId)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findByName_success_anonymous() throws Exception {
        String fakeName = randomString();

        mvc.perform(get(Endpoint.Classification.BASE_URL + "?name=" + fakeName))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findByName_success_client() throws Exception {
        String jwt = createJwt(Role.USER);
        String fakeName = randomString();

        mvc.perform(get(Endpoint.Classification.BASE_URL + "?name=" + fakeName)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void findByName_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        String fakeName = randomString();

        mvc.perform(get(Endpoint.Classification.BASE_URL + "?name=" + fakeName)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void save_forbidden_anonymous() throws Exception {
        ClassificationSaveRequest rq = new ClassificationSaveRequest(randomString());

        mvc.perform(post(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void save_forbidden_client() throws Exception {
        String jwt = createJwt(Role.USER);
        ClassificationSaveRequest rq = new ClassificationSaveRequest(randomString());

        mvc.perform(post(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void save_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        ClassificationSaveRequest rq = new ClassificationSaveRequest(randomString());

        mvc.perform(post(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())));
    }

    @Test
    public void update_forbidden_anonymous() throws Exception {
        ClassificationUpdateRequest rq = new ClassificationUpdateRequest(generateId(), randomString());

        mvc.perform(put(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void update_forbidden_client() throws Exception {
        String jwt = createJwt(Role.USER);
        ClassificationUpdateRequest rq = new ClassificationUpdateRequest(generateId(), randomString());

        mvc.perform(put(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void update_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        ClassificationUpdateRequest rq = new ClassificationUpdateRequest(generateId(), randomString());

        mvc.perform(put(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())));
    }

    @Test
    public void deleteById_forbidden_anonymous() throws Exception {
        long fakeId = generateId();

        mvc.perform(delete(Endpoint.Classification.BASE_URL + "/" + fakeId)
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void deleteById_forbidden_client() throws Exception {
        String jwt = createJwt(Role.USER);
        Long fakeId = generateId();

        mvc.perform(delete(Endpoint.Classification.BASE_URL + "/" + fakeId)
                .contentType(JSON)
                 .cookie(new Cookie(config.getAuthCookieName(), jwt)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.FORBIDDEN.name())));
    }

    @Test
    public void deleteById_success_admin() throws Exception {
        String jwt = createJwt(Role.ADMIN);
        Long fakeId = generateId();

        mvc.perform(delete(Endpoint.Classification.BASE_URL + "/" + fakeId)
                .contentType(JSON)
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
