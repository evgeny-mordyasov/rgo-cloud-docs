package rgo.cloud.docs.boot.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import rgo.cloud.common.api.rest.StatusCode;
import rgo.cloud.common.spring.test.WebTest;
import rgo.cloud.docs.db.api.repository.LanguageRepository;
import rgo.cloud.docs.rest.api.language.request.LanguageSaveRequest;
import rgo.cloud.docs.rest.api.language.request.LanguageUpdateRequest;
import rgo.cloud.docs.db.api.entity.Language;
import rgo.cloud.security.config.util.Endpoint;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static rgo.cloud.common.api.util.JsonUtil.toJson;
import static rgo.cloud.common.spring.util.RequestUtil.JSON;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;
import static rgo.cloud.docs.db.utils.EntityGenerator.createRandomLanguage;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class LanguageRestControllerTest extends WebTest {

    @Autowired
    private LanguageRepository languageRepository;

    @BeforeEach
    public void setUp() {
        truncateTables();
        initMvc();
    }

    @Test
    public void findAll_noOneHasBeenFound() throws Exception {
        int noOneHasBeenFound = 0;

        mvc.perform(get(Endpoint.Language.BASE_URL))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(noOneHasBeenFound)))
                .andExpect(jsonPath("$.total", is(noOneHasBeenFound)));
    }

    @Test
    public void findAll_foundOne() throws Exception {
        int foundOne = 1;
        Language saved = languageRepository.save(createRandomLanguage());

        mvc.perform(get(Endpoint.Language.BASE_URL))
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

        Language saved1 = languageRepository.save(createRandomLanguage());
        Language saved2 = languageRepository.save(createRandomLanguage());

        Integer[] identifiers = new Integer[]{ saved1.getEntityId().intValue(), saved2.getEntityId().intValue() };
        String[] names = new String[]{ saved1.getName(), saved2.getName() };

        mvc.perform(get(Endpoint.Language.BASE_URL))
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

        mvc.perform(get(Endpoint.Language.BASE_URL + "/" + fakeId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", nullValue()));
    }

    @Test
    public void findById_found() throws Exception {
        Language saved = languageRepository.save(createRandomLanguage());

        mvc.perform(get(Endpoint.Language.BASE_URL + "/" + saved.getEntityId()))
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

        mvc.perform(get(Endpoint.Language.BASE_URL + "?name=" + fakeName))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", nullValue()));
    }

    @Test
    public void findByName_found() throws Exception {
        Language saved = languageRepository.save(createRandomLanguage());

        mvc.perform(get(Endpoint.Language.BASE_URL + "?name=" + saved.getName()))
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
        LanguageSaveRequest rq = new LanguageSaveRequest(name);

        mvc.perform(post(Endpoint.Language.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()));

        Optional<Language> opt = languageRepository.findByName(name);

        assertTrue(opt.isPresent());
        assertEquals(name, opt.get().getName());
    }

    @Test
    public void save_duplicate() throws Exception {
        Language saved = languageRepository.save(createRandomLanguage());
        LanguageSaveRequest rq = new LanguageSaveRequest(saved.getName());

        mvc.perform(post(Endpoint.Language.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.VIOLATES_CONSTRAINT.name())))
                .andExpect(jsonPath("$.status.description", is("Language by name already exist.")));
    }

    @Test
    public void update() throws Exception {
        Language saved = languageRepository.save(createRandomLanguage());

        String newName = randomString();
        LanguageUpdateRequest rq = LanguageUpdateRequest.builder()
                .entityId(saved.getEntityId())
                .name(newName)
                .build();

        mvc.perform(put(Endpoint.Language.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()));

        Optional<Language> opt = languageRepository.findById(saved.getEntityId());

        assertTrue(opt.isPresent());
        assertEquals(newName, opt.get().getName());
    }

    @Test
    public void update_languageDoesNotExistByCurrentId() throws Exception {
        long currentId = generateId();

        LanguageUpdateRequest rq = LanguageUpdateRequest.builder()
                .entityId(currentId)
                .name(randomString())
                .build();

        mvc.perform(put(Endpoint.Language.BASE_URL)
                .content(toJson(rq))
                .contentType(JSON))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())))
                .andExpect(jsonPath("$.status.description", is("The language by id not found.")));
    }
}
