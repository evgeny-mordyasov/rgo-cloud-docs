package rgo.cloud.docs.boot.api.validate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import rgo.cloud.common.api.rest.StatusCode;
import rgo.cloud.common.api.rest.rule.Endpoint;
import rgo.cloud.docs.internal.api.rest.classification.request.ClassificationSaveRequest;
import rgo.cloud.docs.internal.api.rest.classification.request.ClassificationUpdateRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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
public class ClassificationRestControllerValidateTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void findById_idIsNotPositive() throws Exception {
        long entityId = -1L;
        String errorMessage = "The entityId is not positive.";

        mvc.perform(get(Endpoint.Classification.BASE_URL + "/" + entityId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void findByName_nameIsEmpty() throws Exception {
        String name = " ";
        String errorMessage = "The name is empty.";

        mvc.perform(get(Endpoint.Classification.BASE_URL + "?name=" + name))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void save_nameIsNull() throws Exception {
        String errorMessage = "The name is null.";

        ClassificationSaveRequest rq = new ClassificationSaveRequest(null);

        mvc.perform(post(Endpoint.Classification.BASE_URL)
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

        ClassificationSaveRequest rq = new ClassificationSaveRequest(name);

        mvc.perform(post(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void update_entityIdIsNull() throws Exception {
        final String errorMessage = "The entityId is null.";

        ClassificationUpdateRequest rq = ClassificationUpdateRequest.builder()
                .entityId(null)
                .name(randomString())
                .build();

        mvc.perform(put(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void update_entityIdIsNotPositive() throws Exception {
        final Long entityId = -generateId();
        final String errorMessage = "The entityId is not positive.";

        ClassificationUpdateRequest rq = ClassificationUpdateRequest.builder()
                .entityId(entityId)
                .name(randomString())
                .build();

        mvc.perform(put(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void update_nameIsNull() throws Exception {
        final String errorMessage = "The name is null.";

        ClassificationUpdateRequest rq = ClassificationUpdateRequest.builder()
                .entityId(generateId())
                .name(null)
                .build();

        mvc.perform(put(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void update_nameIsEmpty() throws Exception {
        final String name = "";
        final String errorMessage = "The name is empty.";

        ClassificationUpdateRequest rq = ClassificationUpdateRequest.builder()
                .entityId(generateId())
                .name(name)
                .build();

        mvc.perform(put(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }

    @Test
    public void deleteById_idIsNotPositive() throws Exception {
        final long entityId = -1L;
        final String errorMessage = "The entityId is not positive.";

        mvc.perform(delete(Endpoint.Classification.BASE_URL + "/" + entityId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.INVALID_RQ.name())))
                .andExpect(jsonPath("$.status.description", equalTo(errorMessage)));
    }
}
