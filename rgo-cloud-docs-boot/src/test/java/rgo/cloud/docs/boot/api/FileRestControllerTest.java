package rgo.cloud.docs.boot.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import rgo.cloud.common.api.rest.StatusCode;
import rgo.cloud.common.spring.test.CommonTest;
import rgo.cloud.docs.facade.FileFacade;
import rgo.cloud.docs.service.TranslationService;
import rgo.cloud.docs.service.DocumentService;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;
import rgo.cloud.docs.db.api.repository.LanguageRepository;
import rgo.cloud.docs.facade.api.FileDto;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.db.api.entity.Document;
import rgo.cloud.docs.db.api.entity.Translation;
import rgo.cloud.docs.db.api.entity.TranslationKey;
import rgo.cloud.docs.db.api.entity.Language;
import rgo.cloud.security.config.util.Endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static rgo.cloud.common.spring.util.RequestUtil.JSON;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;
import static rgo.cloud.docs.boot.EntityGenerator.*;
import static rgo.cloud.docs.boot.EntityGenerator.createRandomTranslation;
import static rgo.cloud.docs.boot.FileGenerator.createFile;
import static rgo.cloud.docs.boot.FileGenerator.multipartPatch;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class FileRestControllerTest extends CommonTest {

    @Autowired
    private FileFacade facade;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ClassificationRepository classificationRepository;

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

        mvc.perform(get(Endpoint.File.BASE_URL))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(noOneHasBeenFound)))
                .andExpect(jsonPath("$.total", is(noOneHasBeenFound)));
    }

    @Test
    public void findAll_foundOne() throws Exception {
        int foundOne = 1;

        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());
        FileDto file = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage));

        mvc.perform(get(Endpoint.File.BASE_URL))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[*].document", notNullValue()))
                .andExpect(jsonPath("$.list[*].document.entityId", containsInAnyOrder(file.getDocument().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.fullName", containsInAnyOrder(file.getDocument().getFullName())))
                .andExpect(jsonPath("$.list[*].document.name", containsInAnyOrder(file.getDocument().getName())))
                .andExpect(jsonPath("$.list[*].document.extension", containsInAnyOrder(file.getDocument().getExtension())))
                .andExpect(jsonPath("$.list[*].document.classification.entityId", containsInAnyOrder(file.getDocument().getClassification().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.classification.name", containsInAnyOrder(file.getDocument().getClassification().getName())))
                .andExpect(jsonPath("$.list[*].resources", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[*].resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.list[*].resources[*].language.entityId", containsInAnyOrder(file.getResources().get(0).getLanguage().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].resources[*].language.name", containsInAnyOrder(file.getResources().get(0).getLanguage().getName())))
                .andExpect(jsonPath("$.list[*].resources[*].resource", containsInAnyOrder(file.getResources().get(0).getResource())))
                .andExpect(jsonPath("$.total", is(foundOne)));
    }

    @Test
    public void findAll_foundOne_twoLanguages() throws Exception {
        int foundOne = 1;
        int twoResources = 2;

        Language language1 = languageRepository.save(createRandomLanguage());
        Language language2 = languageRepository.save(createRandomLanguage());

        Classification savedClassification = classificationRepository.save(createRandomClassification());

        FileDto file = facade.save(createRandomTranslation(createRandomDocument(savedClassification), language1));
        FileDto patchFile = facade.patch(createRandomTranslation(file.getDocument(), language2));

        mvc.perform(get(Endpoint.File.BASE_URL))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[*].document", notNullValue()))
                .andExpect(jsonPath("$.list[*].document.entityId", containsInAnyOrder(patchFile.getDocument().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.fullName", containsInAnyOrder(patchFile.getDocument().getFullName())))
                .andExpect(jsonPath("$.list[*].document.name", containsInAnyOrder(patchFile.getDocument().getName())))
                .andExpect(jsonPath("$.list[*].document.extension", containsInAnyOrder(patchFile.getDocument().getExtension())))
                .andExpect(jsonPath("$.list[*].document.classification.entityId", containsInAnyOrder(patchFile.getDocument().getClassification().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.classification.name", containsInAnyOrder(patchFile.getDocument().getClassification().getName())))
                .andExpect(jsonPath("$.list[0].resources", hasSize(twoResources)))
                .andExpect(jsonPath("$.list[0].resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.list[0].resources[*].language.entityId",
                        containsInAnyOrder(patchFile.getResources().get(0).getLanguage().getEntityId().intValue(), patchFile.getResources().get(1).getLanguage().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[0].resources[*].language.name",
                        containsInAnyOrder(patchFile.getResources().get(0).getLanguage().getName(), patchFile.getResources().get(1).getLanguage().getName())))
                .andExpect(jsonPath("$.list[0].resources[*].resource",
                        containsInAnyOrder(patchFile.getResources().get(0).getResource(), patchFile.getResources().get(1).getResource())))
                .andExpect(jsonPath("$.total", is(foundOne)));
    }

    @Test
    public void findAll_foundALot() throws Exception {
        int foundALot = 2;
        int oneResource = 1;

        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        FileDto file1 = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage));
        FileDto file2 = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage));

        mvc.perform(get(Endpoint.File.BASE_URL))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundALot)))
                .andExpect(jsonPath("$.list[*].document", notNullValue()))
                .andExpect(jsonPath("$.list[*].document.entityId",
                        containsInAnyOrder(file1.getDocument().getEntityId().intValue(), file2.getDocument().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.fullName",
                        containsInAnyOrder(file1.getDocument().getFullName(), file2.getDocument().getFullName())))
                .andExpect(jsonPath("$.list[*].document.name",
                        containsInAnyOrder(file1.getDocument().getName(), file2.getDocument().getName())))
                .andExpect(jsonPath("$.list[*].document.extension",
                        containsInAnyOrder(file1.getDocument().getExtension(), file2.getDocument().getExtension())))
                .andExpect(jsonPath("$.list[*].document.classification.entityId",
                        containsInAnyOrder(file1.getDocument().getClassification().getEntityId().intValue(), file2.getDocument().getClassification().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.classification.name",
                        containsInAnyOrder(file1.getDocument().getClassification().getName(), file2.getDocument().getClassification().getName())))
                .andExpect(jsonPath("$.list[0].resources", hasSize(oneResource)))
                .andExpect(jsonPath("$.list[0].resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.list[*].resources[*].language.entityId",
                        containsInAnyOrder(file1.getResources().get(0).getLanguage().getEntityId().intValue(), file2.getResources().get(0).getLanguage().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].resources[*].language.name",
                        containsInAnyOrder(file1.getResources().get(0).getLanguage().getName(), file2.getResources().get(0).getLanguage().getName())))
                .andExpect(jsonPath("$.list[*].resources[*].resource",
                        containsInAnyOrder(file1.getResources().get(0).getResource(), file2.getResources().get(0).getResource())))
                .andExpect(jsonPath("$.total", is(foundALot)));
    }

    @Test
    public void findByClassificationId_noOneHasBeenFound() throws Exception {
        int noOneHasBeenFound = 0;
        long fakeClassificationId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "?classificationId=" + fakeClassificationId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(noOneHasBeenFound)))
                .andExpect(jsonPath("$.total", is(noOneHasBeenFound)));
    }

    @Test
    public void findByClassificationId_foundOne() throws Exception {
        int foundOne = 1;

        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());
        FileDto file = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage));

        mvc.perform(get(Endpoint.File.BASE_URL + "?classificationId=" + savedClassification.getEntityId()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[*].document", notNullValue()))
                .andExpect(jsonPath("$.list[*].document.entityId", containsInAnyOrder(file.getDocument().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.fullName", containsInAnyOrder(file.getDocument().getFullName())))
                .andExpect(jsonPath("$.list[*].document.name", containsInAnyOrder(file.getDocument().getName())))
                .andExpect(jsonPath("$.list[*].document.extension", containsInAnyOrder(file.getDocument().getExtension())))
                .andExpect(jsonPath("$.list[*].document.classification.entityId", containsInAnyOrder(savedClassification.getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.classification.name", containsInAnyOrder(savedClassification.getName())))
                .andExpect(jsonPath("$.list[*].resources", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[*].resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.list[*].resources[*].language.entityId", containsInAnyOrder(file.getResources().get(0).getLanguage().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].resources[*].language.name", containsInAnyOrder(file.getResources().get(0).getLanguage().getName())))
                .andExpect(jsonPath("$.list[*].resources[*].resource", containsInAnyOrder(file.getResources().get(0).getResource())))
                .andExpect(jsonPath("$.total", is(foundOne)));
    }

    @Test
    public void findByClassificationId_foundOne_differentClassification() throws Exception {
        int foundOne = 1;

        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification1 = classificationRepository.save(createRandomClassification());
        Classification savedClassification2 = classificationRepository.save(createRandomClassification());

        FileDto file1 = facade.save(createRandomTranslation(createRandomDocument(savedClassification1), savedLanguage));

        facade.save(createRandomTranslation(createRandomDocument(savedClassification2), savedLanguage));
        facade.save(createRandomTranslation(createRandomDocument(savedClassification2), savedLanguage));

        mvc.perform(get(Endpoint.File.BASE_URL + "?classificationId=" + savedClassification1.getEntityId()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[*].document", notNullValue()))
                .andExpect(jsonPath("$.list[*].document.entityId", containsInAnyOrder(file1.getDocument().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.fullName", containsInAnyOrder(file1.getDocument().getFullName())))
                .andExpect(jsonPath("$.list[*].document.name", containsInAnyOrder(file1.getDocument().getName())))
                .andExpect(jsonPath("$.list[*].document.extension", containsInAnyOrder(file1.getDocument().getExtension())))
                .andExpect(jsonPath("$.list[*].document.classification.entityId", containsInAnyOrder(savedClassification1.getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.classification.name", containsInAnyOrder(savedClassification1.getName())))
                .andExpect(jsonPath("$.list[*].resources", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[*].resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.list[*].resources[*].language.entityId", containsInAnyOrder(file1.getResources().get(0).getLanguage().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].resources[*].language.name", containsInAnyOrder(file1.getResources().get(0).getLanguage().getName())))
                .andExpect(jsonPath("$.list[*].resources[*].resource", containsInAnyOrder(file1.getResources().get(0).getResource())))
                .andExpect(jsonPath("$.total", is(foundOne)));
    }

    @Test
    public void findByFullName_noOneHasBeenFound() throws Exception {
        int noOneHasBeenFound = 0;
        String fakeName = randomString();

        mvc.perform(get(Endpoint.File.BASE_URL + "?name=" + fakeName))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(noOneHasBeenFound)))
                .andExpect(jsonPath("$.total", is(noOneHasBeenFound)));
    }

    @Test
    public void findByFullName_foundOne() throws Exception {
        int foundOne = 1;

        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());
        FileDto file = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage));

        mvc.perform(get(Endpoint.File.BASE_URL + "?name=" + file.getDocument().getName()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[*].document", notNullValue()))
                .andExpect(jsonPath("$.list[*].document.entityId", containsInAnyOrder(file.getDocument().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.fullName", containsInAnyOrder(file.getDocument().getFullName())))
                .andExpect(jsonPath("$.list[*].document.name", containsInAnyOrder(file.getDocument().getName())))
                .andExpect(jsonPath("$.list[*].document.extension", containsInAnyOrder(file.getDocument().getExtension())))
                .andExpect(jsonPath("$.list[*].document.classification.entityId", containsInAnyOrder(savedClassification.getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].document.classification.name", containsInAnyOrder(savedClassification.getName())))
                .andExpect(jsonPath("$.list[*].resources", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[*].resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.list[*].resources[*].language.entityId", containsInAnyOrder(file.getResources().get(0).getLanguage().getEntityId().intValue())))
                .andExpect(jsonPath("$.list[*].resources[*].language.name", containsInAnyOrder(file.getResources().get(0).getLanguage().getName())))
                .andExpect(jsonPath("$.list[*].resources[*].resource", containsInAnyOrder(file.getResources().get(0).getResource())))
                .andExpect(jsonPath("$.total", is(foundOne)));
    }

    @Test
    public void findByFullName_foundALot() throws Exception {
        int foundALot = 3;
        String name = randomString();

        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        Document saved1 = createRandomDocument(savedClassification);
        Document saved2 = createRandomDocument(savedClassification);
        Document saved3 = createRandomDocument(savedClassification);

        saved1 = saved1.toBuilder().fullName(saved1.getName() + name).build();
        saved2 = saved2.toBuilder().fullName(name + saved1.getName()).build();
        saved3 = saved3.toBuilder().fullName(name).build();

        FileDto file1 = facade.save(createRandomTranslation(saved1, savedLanguage));
        FileDto file2 = facade.save(createRandomTranslation(saved2, savedLanguage));
        FileDto file3 = facade.save(createRandomTranslation(saved3, savedLanguage));

        String[] names = new String[] {
                file1.getDocument().getFullName(),
                file2.getDocument().getFullName(),
                file3.getDocument().getFullName()
        };

        mvc.perform(get(Endpoint.File.BASE_URL + "?name=" + name))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundALot)))
                .andExpect(jsonPath("$.list[*].document", notNullValue()))
                .andExpect(jsonPath("$.list[*].document.fullName", containsInAnyOrder(names)))
                .andExpect(jsonPath("$.total", is(foundALot)));
    }

    @Test
    public void findByDocumentId_notFound() throws Exception {
        long fakeDocumentId = generateId();

        mvc.perform(get(Endpoint.File.BASE_URL + "/" + fakeDocumentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", nullValue()));
    }

    @Test
    public void findByDocumentId_found() throws Exception {
        int foundOne = 1;

        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());
        FileDto file = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage));

        mvc.perform(get(Endpoint.File.BASE_URL + "/" + file.getDocument().getEntityId()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", notNullValue()))
                .andExpect(jsonPath("$.object.document", notNullValue()))
                .andExpect(jsonPath("$.object.document.entityId", is(file.getDocument().getEntityId().intValue())))
                .andExpect(jsonPath("$.object.document.fullName", is(file.getDocument().getFullName())))
                .andExpect(jsonPath("$.object.document.name", is(file.getDocument().getName())))
                .andExpect(jsonPath("$.object.document.extension", is(file.getDocument().getExtension())))
                .andExpect(jsonPath("$.object.document.classification.entityId", is(file.getDocument().getClassification().getEntityId().intValue())))
                .andExpect(jsonPath("$.object.document.classification.name", is(file.getDocument().getClassification().getName())))
                .andExpect(jsonPath("$.object.resources", hasSize(foundOne)))
                .andExpect(jsonPath("$.object.resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.object.resources[*].language.entityId", containsInAnyOrder(file.getResources().get(0).getLanguage().getEntityId().intValue())))
                .andExpect(jsonPath("$.object.resources[*].language.name", containsInAnyOrder(file.getResources().get(0).getLanguage().getName())))
                .andExpect(jsonPath("$.object.resources[*].resource", containsInAnyOrder(file.getResources().get(0).getResource())));
    }

    @Test
    public void findByDocumentId_twoLanguages() throws Exception {
        int twoResources = 2;

        Language savedLanguage1 = languageRepository.save(createRandomLanguage());
        Language savedLanguage2 = languageRepository.save(createRandomLanguage());

        Classification savedClassification = classificationRepository.save(createRandomClassification());

        FileDto file = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage1));
        FileDto patchFile = facade.patch(createRandomTranslation(file.getDocument(), savedLanguage2));

        mvc.perform(get(Endpoint.File.BASE_URL + "/" + file.getDocument().getEntityId()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", notNullValue()))
                .andExpect(jsonPath("$.object.document", notNullValue()))
                .andExpect(jsonPath("$.object.document.entityId", is(patchFile.getDocument().getEntityId().intValue())))
                .andExpect(jsonPath("$.object.document.fullName", is(patchFile.getDocument().getFullName())))
                .andExpect(jsonPath("$.object.document.name", is(patchFile.getDocument().getName())))
                .andExpect(jsonPath("$.object.document.extension", is(patchFile.getDocument().getExtension())))
                .andExpect(jsonPath("$.object.document.classification.entityId", is(patchFile.getDocument().getClassification().getEntityId().intValue())))
                .andExpect(jsonPath("$.object.document.classification.name", is(patchFile.getDocument().getClassification().getName())))
                .andExpect(jsonPath("$.object.resources", hasSize(twoResources)))
                .andExpect(jsonPath("$.object.resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.object.resources[*].language.entityId",
                        containsInAnyOrder(patchFile.getResources().get(0).getLanguage().getEntityId().intValue(), patchFile.getResources().get(1).getLanguage().getEntityId().intValue())))
                .andExpect(jsonPath("$.object.resources[*].language.name",
                        containsInAnyOrder(patchFile.getResources().get(0).getLanguage().getName(), patchFile.getResources().get(1).getLanguage().getName())))
                .andExpect(jsonPath("$.object.resources[*].resource",
                        containsInAnyOrder(patchFile.getResources().get(0).getResource(), patchFile.getResources().get(1).getResource())));
    }

    @Test
    public void getFreeLanguages_documentIdIsFake() throws Exception {
        long fakeDocumentId = generateId();
        String errorMessage = "The document not found by documentId.";

        mvc.perform(get(Endpoint.File.BASE_URL + "/free-languages/" + fakeDocumentId))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())))
                .andExpect(jsonPath("$.status.description", is(errorMessage)));
    }

    @Test
    public void getFreeLanguages_allLanguagesUsed() throws Exception {
        int foundOne = 0;

        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());
        FileDto file = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage));

        mvc.perform(get(Endpoint.File.BASE_URL + "/free-languages/" + file.getDocument().getEntityId()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundOne)));
    }

    @Test
    public void getFreeLanguages_oneOfTwoLanguagesUsed() throws Exception {
        int foundOne = 1;

        Language lang1 = languageRepository.save(createRandomLanguage());
        Language lang2 = languageRepository.save(createRandomLanguage());

        Classification savedClassification = classificationRepository.save(createRandomClassification());
        FileDto file = facade.save(createRandomTranslation(createRandomDocument(savedClassification), lang1));

        mvc.perform(get(Endpoint.File.BASE_URL + "/free-languages/" + file.getDocument().getEntityId()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.list", hasSize(foundOne)))
                .andExpect(jsonPath("$.list[0].entityId", is(lang2.getEntityId().intValue())))
                .andExpect(jsonPath("$.list[0].name", is(lang2.getName())));
    }

    @Test
    public void findResource() throws Exception {
        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        FileDto savedFile = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage));

        downloadDocument(savedFile.getDocument().getEntityId(), savedLanguage.getEntityId());
    }

    @Test
    public void checkNumberOfDocumentDownloads_oneLanguage_oneFile() {
        int clients = ThreadLocalRandom.current().nextInt(1, 100);

        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());
        FileDto file = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage));

        ExecutorService executor = Executors.newFixedThreadPool(clients);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < clients; i++) {
            futures.add(executor.submit(() -> {
                try {
                    downloadDocument(file.getDocument().getEntityId(), savedLanguage.getEntityId());
                } catch (Exception ignored) {}
            }));
        }

        wait(futures);

        Optional<FileDto> opt = facade.findByDocumentId(file.getDocument().getEntityId());
        assertTrue(opt.isPresent());
        assertEquals(clients, opt.get().getDownloads());
    }

    @Test
    public void checkNumberOfDocumentDownloads_manyLanguages_oneFile() {
        int clients = ThreadLocalRandom.current().nextInt(1,100);
        int numberOfLanguages = ThreadLocalRandom.current().nextInt(2,10);

        List<Language> languages = new ArrayList<>();
        for (int i = 0; i < numberOfLanguages; i++) {
            languages.add(languageRepository.save(createRandomLanguage()));
        }

        Classification savedClassification = classificationRepository.save(createRandomClassification());

        FileDto file = facade.save(createRandomTranslation(createRandomDocument(savedClassification), languages.get(0)));
        for (int i = 1; i <= numberOfLanguages - 1; i++) {
            facade.patch(createRandomTranslation(file.getDocument(), languages.get(i)));
        }

        ExecutorService executor = Executors.newFixedThreadPool(clients);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < clients; i++) {
            futures.add(executor.submit(() -> {
                try {
                    int languageIndex = ThreadLocalRandom.current().nextInt(numberOfLanguages);
                    downloadDocument(file.getDocument().getEntityId(), languages.get(languageIndex).getEntityId());
                } catch (Exception ignored) {}
            }));
        }

        wait(futures);

        Optional<FileDto> opt = facade.findByDocumentId(file.getDocument().getEntityId());
        assertTrue(opt.isPresent());
        assertEquals(clients, opt.get().getDownloads());
    }

    @Test
    public void checkNumberOfDocumentDownloads_oneLanguage_manyFiles() {
        int clients = ThreadLocalRandom.current().nextInt(1,100);
        int numberOfFiles = ThreadLocalRandom.current().nextInt(1, 25);

        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        List<FileDto> files = new ArrayList<>();
        for (int i = 0; i < numberOfFiles; i++) {
            files.add(facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage)));
        }

        ExecutorService executor = Executors.newFixedThreadPool(clients);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < clients; i++) {
            futures.add(executor.submit(() -> {
                try {
                    int fileIndex = ThreadLocalRandom.current().nextInt(numberOfFiles);
                    downloadDocument(files.get(fileIndex).getDocument().getEntityId(), savedLanguage.getEntityId());
                } catch (Exception ignored) {}
            }));
        }

        wait(futures);

        List<FileDto> list = facade.findAll();
        assertFalse(list.isEmpty());

        long downloads = list.stream()
                .map(FileDto::getDownloads)
                .reduce(0L, Long::sum);
        assertEquals(clients, downloads);
    }

    @Test
    public void checkNumberOfDocumentDownloads_manyLanguages_manyFiles() {
        int clients = ThreadLocalRandom.current().nextInt(1,100);
        int numberOfFiles = ThreadLocalRandom.current().nextInt(25);
        int numberOfLanguages = ThreadLocalRandom.current().nextInt(2,10);

        List<Language> languages = new ArrayList<>();
        for (int i = 0; i < numberOfLanguages; i++) {
            languages.add(languageRepository.save(createRandomLanguage()));
        }

        Classification savedClassification = classificationRepository.save(createRandomClassification());

        for (int i = 0; i < numberOfFiles; i++) {
            var currentFile = facade.save(createRandomTranslation(createRandomDocument(savedClassification), languages.get(0)));

            for (int j = 1; j <= numberOfLanguages - 1; j++) {
                facade.patch(createRandomTranslation(currentFile.getDocument(), languages.get(j)));
            }
        }

        List<FileDto> filesWithLanguages = facade.findAll();

        ExecutorService executor = Executors.newFixedThreadPool(clients);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < clients; i++) {
            futures.add(executor.submit(() -> {
                try {
                    int fileIndex = ThreadLocalRandom.current().nextInt(numberOfFiles);
                    int fileLanguages = filesWithLanguages.get(fileIndex).getResources().size();
                    int languageIndex = ThreadLocalRandom.current().nextInt(fileLanguages);
                    downloadDocument(filesWithLanguages.get(fileIndex).getDocument().getEntityId(),
                            filesWithLanguages.get(fileIndex).getResources().get(languageIndex).getLanguage().getEntityId());
                } catch (Exception ignored) {}
            }));
        }

        wait(futures);

        List<FileDto> list = facade.findAll();
        assertFalse(list.isEmpty());

        long downloads = list.stream()
                .map(FileDto::getDownloads)
                .reduce(0L, Long::sum);
        assertEquals(clients, downloads);
    }

    private void downloadDocument(Long documentId, Long languageId) throws Exception {
        mvc.perform(get(Endpoint.File.BASE_URL + Endpoint.File.RESOURCE + "?documentId=" + documentId + "&languageId=" + languageId))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

    private void wait(List<Future<?>> futures) {
        futures.forEach(v -> {
            try {
                v.get();
            } catch (Exception ignored) {}
        });
    }

    @Test
    public void save() throws Exception {
        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        MockMultipartFile file = createFile();

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", savedLanguage.getEntityId().toString())
                .param("classificationId", savedClassification.getEntityId().toString()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", notNullValue()))
                .andExpect(jsonPath("$.object.document", notNullValue()))
                .andExpect(jsonPath("$.object.document.fullName", is(file.getOriginalFilename())))
                .andExpect(jsonPath("$.object.document.classification.entityId", is(savedClassification.getEntityId().intValue())))
                .andExpect(jsonPath("$.object.document.classification.name", is(savedClassification.getName())))
                .andExpect(jsonPath("$.object.resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.object.resources[*].language.entityId", containsInAnyOrder(savedLanguage.getEntityId().intValue())))
                .andExpect(jsonPath("$.object.resources[*].language.name", containsInAnyOrder(savedLanguage.getName())));
    }

    @Test
    public void save_rollbackTransactional() throws Exception {
        long fakeLanguageId = generateId();
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        MockMultipartFile file = createFile();

        mvc.perform(multipart(Endpoint.File.BASE_URL)
                .file(file)
                .param("languageId", Long.toString(fakeLanguageId))
                .param("classificationId", savedClassification.getEntityId().toString()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())))
                .andExpect(jsonPath("$.status.description", notNullValue()));

        List<Document> list = documentService.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    public void patch_twoResources() throws Exception {
        int twoResources = 2;

        Language savedLanguage1 = languageRepository.save(createRandomLanguage());
        Language savedLanguage2 = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        FileDto savedFile = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage1));

        MockMultipartFile file = createFile();

        mvc.perform(multipartPatch()
                .file(file)
                .param("documentId", savedFile.getDocument().getEntityId().toString())
                .param("languageId", savedLanguage2.getEntityId().toString()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", notNullValue()))
                .andExpect(jsonPath("$.object.document", notNullValue()))
                .andExpect(jsonPath("$.object.document.entityId", is(savedFile.getDocument().getEntityId().intValue())))
                .andExpect(jsonPath("$.object.document.fullName", is(savedFile.getDocument().getFullName())))
                .andExpect(jsonPath("$.object.document.name", is(savedFile.getDocument().getName())))
                .andExpect(jsonPath("$.object.document.extension", is(savedFile.getDocument().getExtension())))
                .andExpect(jsonPath("$.object.document.classification.entityId", is(savedFile.getDocument().getClassification().getEntityId().intValue())))
                .andExpect(jsonPath("$.object.document.classification.name", is(savedFile.getDocument().getClassification().getName())))
                .andExpect(jsonPath("$.object.resources", hasSize(twoResources)))
                .andExpect(jsonPath("$.object.resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.object.resources[*].language.entityId",
                        containsInAnyOrder(savedLanguage1.getEntityId().intValue(), savedLanguage2.getEntityId().intValue())))
                .andExpect(jsonPath("$.object.resources[*].language.name",
                        containsInAnyOrder(savedLanguage1.getName(), savedLanguage2.getName())));
    }

    @Test
    public void patchFileName() throws Exception {
        int twoResources = 2;

        Language savedLanguage1 = languageRepository.save(createRandomLanguage());
        Language savedLanguage2 = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        FileDto savedFile = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage1));
        FileDto patchFile = facade.patch(createRandomTranslation(savedFile.getDocument(), savedLanguage2));

        String newFileName = randomString();
        mvc.perform(patch(Endpoint.File.BASE_URL + Endpoint.File.UPDATE_NAME)
                .param("documentId", Long.toString(patchFile.getDocument().getEntityId()))
                .param("fileName", newFileName))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()))
                .andExpect(jsonPath("$.object", notNullValue()))
                .andExpect(jsonPath("$.object.document", notNullValue()))
                .andExpect(jsonPath("$.object.document.entityId", is(savedFile.getDocument().getEntityId().intValue())))
                .andExpect(jsonPath("$.object.document.fullName", is(newFileName + "." + savedFile.getDocument().getExtension())))
                .andExpect(jsonPath("$.object.document.name", is(newFileName)))
                .andExpect(jsonPath("$.object.document.extension", is(savedFile.getDocument().getExtension())))
                .andExpect(jsonPath("$.object.document.classification.entityId", is(savedFile.getDocument().getClassification().getEntityId().intValue())))
                .andExpect(jsonPath("$.object.document.classification.name", is(savedFile.getDocument().getClassification().getName())))
                .andExpect(jsonPath("$.object.resources", hasSize(twoResources)))
                .andExpect(jsonPath("$.object.resources[*].language", notNullValue()))
                .andExpect(jsonPath("$.object.resources[*].language.entityId",
                        containsInAnyOrder(savedLanguage1.getEntityId().intValue(), savedLanguage2.getEntityId().intValue())))
                .andExpect(jsonPath("$.object.resources[*].language.name",
                        containsInAnyOrder(savedLanguage1.getName(), savedLanguage2.getName())));
    }

    @Test
    public void deleteByKey_notFound() throws Exception {
        long fakeDocumentId = generateId();
        long fakeLanguageId = generateId();

        mvc.perform(delete(Endpoint.File.BASE_URL)
                .param("documentId", Long.toString(fakeDocumentId))
                .param("languageId", Long.toString(fakeLanguageId)))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.ENTITY_NOT_FOUND.name())))
                .andExpect(jsonPath("$.status.description", notNullValue()));
    }

    @Test
    public void deleteByKey() throws Exception {
        Language savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        FileDto savedFile = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage));

        mvc.perform(delete(Endpoint.File.BASE_URL)
                .param("documentId", savedFile.getDocument().getEntityId().toString())
                .param("languageId", savedLanguage.getEntityId().toString()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()));

        TranslationKey key = TranslationKey.builder()
                .documentId(savedFile.getDocument().getEntityId())
                .languageId(savedLanguage.getEntityId())
                .build();
        Optional<Translation> opt1 = translationService.findByKey(key);
        assertTrue(opt1.isEmpty());

        Optional<Document> opt2 = documentService.findById(savedFile.getDocument().getEntityId());
        assertTrue(opt2.isEmpty());
    }

    @Test
    public void deleteByKey_twoLanguages() throws Exception {
        Language savedLanguage1 = languageRepository.save(createRandomLanguage());
        Language savedLanguage2 = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        FileDto savedFile = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage1));
        FileDto patchFile = facade.patch(createRandomTranslation(savedFile.getDocument(), savedLanguage2));

        mvc.perform(delete(Endpoint.File.BASE_URL)
                .param("documentId", patchFile.getDocument().getEntityId().toString())
                .param("languageId", savedLanguage1.getEntityId().toString()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()));

        TranslationKey key = TranslationKey.builder()
                .documentId(patchFile.getDocument().getEntityId())
                .languageId(savedLanguage1.getEntityId())
                .build();
        Optional<Translation> opt = translationService.findByKey(key);
        assertTrue(opt.isEmpty());

        List<Translation> list = translationService.findByDocumentId(patchFile.getDocument().getEntityId());
        assertEquals(1, list.size());
    }

    @Test
    public void deleteByDocumentId_twoLanguages() throws Exception {
        Language savedLanguage1 = languageRepository.save(createRandomLanguage());
        Language savedLanguage2 = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());

        FileDto savedFile = facade.save(createRandomTranslation(createRandomDocument(savedClassification), savedLanguage1));
        FileDto patchFile = facade.patch(createRandomTranslation(savedFile.getDocument(), savedLanguage2));

        mvc.perform(delete(Endpoint.File.BASE_URL + "/" + patchFile.getDocument().getEntityId()))
                .andExpect(content().contentType(JSON))
                .andExpect(jsonPath("$.status.code", is(StatusCode.SUCCESS.name())))
                .andExpect(jsonPath("$.status.description", nullValue()));

        List<Translation> list = translationService.findByDocumentId(patchFile.getDocument().getEntityId());
        assertEquals(0, list.size());

        Optional<Document> opt = documentService.findById(patchFile.getDocument().getEntityId());
        assertTrue(opt.isEmpty());
    }
}
