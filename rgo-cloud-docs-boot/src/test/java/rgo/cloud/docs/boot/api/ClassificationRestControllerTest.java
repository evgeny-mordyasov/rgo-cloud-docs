package rgo.cloud.docs.boot.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import rgo.cloud.common.api.rest.StatusCode;
import rgo.cloud.common.spring.test.CommonTest;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;
import rgo.cloud.docs.internal.api.rest.classification.request.ClassificationSaveRequest;
import rgo.cloud.docs.internal.api.rest.classification.request.ClassificationUpdateRequest;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.security.config.util.Endpoint;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static rgo.cloud.common.api.util.JsonUtil.toJson;
import static rgo.cloud.common.api.util.RequestUtil.JSON;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;
import static rgo.cloud.docs.boot.EntityGenerator.createRandomClassification;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class ClassificationRestControllerTest extends CommonTest {

    @Autowired
    private ClassificationRepository classificationRepository;

    @BeforeEach
    public void setUp() {
        truncateTables();
        initMvc();
    }

    @Test
    public void findAll_noOneHasBeenFound() throws Exception {
        int noOneHasBeenFound = 0;

        mvc.perform(get(Endpoint.Classification.BASE_URL))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(noOneHasBeenFound)))
                .andExpect(jsonPath("$.total", is(noOneHasBeenFound)));
    }

    @Test
    public void findAll_foundOne() throws Exception {
        int foundOne = 1;
        Classification saved = classificationRepository.save(createRandomClassification());

        mvc.perform(get(Endpoint.Classification.BASE_URL))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[0].entityId", is(saved.getEntityId().intValue())))
                .andExpect(jsonPath("$.list[0].name", is(saved.getName())))
                .andExpect(jsonPath("$.total", is(foundOne)));
    }

    @Test
    public void findAll_foundALot() throws Exception {
        int foundALot = 2;

        Classification saved1 = classificationRepository.save(createRandomClassification());
        Classification saved2 = classificationRepository.save(createRandomClassification());

        Integer[] identifiers = new Integer[]{ saved1.getEntityId().intValue(), saved2.getEntityId().intValue() };
        String[] names = new String[]{ saved1.getName(), saved2.getName() };

        mvc.perform(get(Endpoint.Classification.BASE_URL))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundALot)))
                .andExpect(jsonPath("$.list[*].entityId", containsInAnyOrder(identifiers)))
                .andExpect(jsonPath("$.list[*].name", containsInAnyOrder(names)))
                .andExpect(jsonPath("$.total", is(foundALot)));
    }

    @Test
    public void findById_notFound() throws Exception {
        long fakeId = generateId();

        mvc.perform(get(Endpoint.Classification.BASE_URL + "/" + fakeId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", nullValue()));
    }

    @Test
    public void findById_found() throws Exception {
        Classification saved = classificationRepository.save(createRandomClassification());

        mvc.perform(get(Endpoint.Classification.BASE_URL + "/" + saved.getEntityId()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", notNullValue()))
                .andExpect(jsonPath("$.object.entityId", is(saved.getEntityId().intValue())))
                .andExpect(jsonPath("$.object.name", is(saved.getName())));
    }

    @Test
    public void findByName_notFound() throws Exception {
        String fakeName = randomString();

        mvc.perform(get(Endpoint.Classification.BASE_URL + "?name=" + fakeName))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", nullValue()));
    }

    @Test
    public void findByName_found() throws Exception {
        Classification saved = classificationRepository.save(createRandomClassification());

        mvc.perform(get(Endpoint.Classification.BASE_URL + "?name=" + saved.getName()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", notNullValue()))
                .andExpect(jsonPath("$.object.entityId", is(saved.getEntityId().intValue())))
                .andExpect(jsonPath("$.object.name", is(saved.getName())));
    }

    @Test
    public void save() throws Exception {
        String name = randomString();
        ClassificationSaveRequest rq = new ClassificationSaveRequest(name);

        mvc.perform(post(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()));

        Optional<Classification> opt = classificationRepository.findByName(name);

        assertTrue(opt.isPresent());
        assertEquals(name, opt.get().getName());
    }

    @Test
    public void save_duplicate() throws Exception {
        Classification saved = classificationRepository.save(createRandomClassification());
        ClassificationSaveRequest rq = new ClassificationSaveRequest(saved.getName());

        mvc.perform(post(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.VIOLATES_CONSTRAINT.name())))
                .andExpect(jsonPath("$.status.description", is("Classification by name already exist.")));
    }

    @Test
    public void update() throws Exception {
        Classification saved = classificationRepository.save(createRandomClassification());

        String newName = randomString();
        ClassificationUpdateRequest rq = ClassificationUpdateRequest.builder()
                .entityId(saved.getEntityId())
                .name(newName)
                .build();

        mvc.perform(put(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()));

        Optional<Classification> opt = classificationRepository.findById(saved.getEntityId());

        assertTrue(opt.isPresent());
        assertEquals(newName, opt.get().getName());
    }

    @Test
    public void update_classificationDoesNotExistByCurrentId() throws Exception {
        long currentId = generateId();

        ClassificationUpdateRequest rq = ClassificationUpdateRequest.builder()
                .entityId(currentId)
                .name(randomString())
                .build();

        mvc.perform(put(Endpoint.Classification.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())))
                .andExpect(jsonPath("$.status.description", is("The classification by id not found.")));
    }

    @Test
    public void deleteById() throws Exception {
        Classification saved = classificationRepository.save(createRandomClassification());

        mvc.perform(delete(Endpoint.Classification.BASE_URL + "/" + saved.getEntityId())
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()));

        Optional<Classification> opt = classificationRepository.findById(saved.getEntityId());

        assertTrue(opt.isEmpty());
    }

    @Test
    public void deleteById_notFound() throws Exception {
        long fakeId = generateId();

        mvc.perform(delete(Endpoint.Classification.BASE_URL + "/" + fakeId)
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())))
                .andExpect(jsonPath("$.status.description", is("The classification by id not found.")));
    }
}
