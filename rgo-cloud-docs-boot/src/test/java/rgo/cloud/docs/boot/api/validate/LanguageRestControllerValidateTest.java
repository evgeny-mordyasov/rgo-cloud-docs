package rgo.cloud.docs.boot.api.validate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import rgo.cloud.common.api.rest.StatusCode;
import rgo.cloud.common.spring.test.WebTest;
import rgo.cloud.docs.rest.api.language.request.LanguageSaveRequest;
import rgo.cloud.docs.rest.api.language.request.LanguageUpdateRequest;
import rgo.cloud.security.config.util.Endpoint;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static rgo.cloud.common.api.util.JsonUtil.toJson;
import static rgo.cloud.common.spring.util.RequestUtil.JSON;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class LanguageRestControllerValidateTest extends WebTest {

    @BeforeEach
    public void setUp() {
        initMvc();
    }

    @Test
    public void findById_idIsNotPositive() throws Exception {
        long entityId = -1L;
        String errorMessage = "The entityId is not positive.";

        mvc.perform(get(Endpoint.Language.BASE_URL + "/" + entityId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void findByName_nameIsEmpty() throws Exception {
        String name = " ";
        String errorMessage = "The name is empty.";

        mvc.perform(get(Endpoint.Language.BASE_URL + "?name=" + name))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void save_nameIsNull() throws Exception {
        String errorMessage = "The name is null.";

        LanguageSaveRequest rq = new LanguageSaveRequest(null);

        mvc.perform(post(Endpoint.Language.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void save_nameIsEmpty() throws Exception {
        String name = "";
        String errorMessage = "The name is empty.";

        LanguageSaveRequest rq = new LanguageSaveRequest(name);

        mvc.perform(post(Endpoint.Language.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void update_entityIdIsNull() throws Exception {
        String errorMessage = "The entityId is null.";

        LanguageUpdateRequest rq = LanguageUpdateRequest.builder()
                .entityId(null)
                .name(randomString())
                .build();

        mvc.perform(put(Endpoint.Language.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void update_entityIdIsNotPositive() throws Exception {
        long entityId = -generateId();
        String errorMessage = "The entityId is not positive.";

        LanguageUpdateRequest rq = LanguageUpdateRequest.builder()
                .entityId(entityId)
                .name(randomString())
                .build();

        mvc.perform(put(Endpoint.Language.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void update_nameIsNull() throws Exception {
        String errorMessage = "The name is null.";

        LanguageUpdateRequest rq = LanguageUpdateRequest.builder()
                .entityId(generateId())
                .name(null)
                .build();

        mvc.perform(put(Endpoint.Language.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void update_nameIsEmpty() throws Exception {
        String name = "";
        String errorMessage = "The name is empty.";

        LanguageUpdateRequest rq = LanguageUpdateRequest.builder()
                .entityId(generateId())
                .name(name)
                .build();

        mvc.perform(put(Endpoint.Language.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }
}
