package rgo.cloud.docs.db.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.spring.test.PersistenceTest;
import rgo.cloud.docs.db.api.entity.*;
import rgo.cloud.docs.db.config.PersistenceConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;
import static rgo.cloud.docs.db.utils.EntityGenerator.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class TranslationRepositoryTest extends PersistenceTest {
    
    @Autowired
    private TranslationRepository translationRepository;
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private ClassificationRepository classificationRepository;
    
    @Autowired
    private LanguageRepository languageRepository;

    private Language savedLanguage;

    private Document savedDocument;

    @BeforeEach
    public void setUp() {
        truncateTables();
        initInternalEntities();
    }

    private void initInternalEntities() {
        savedLanguage = languageRepository.save(createRandomLanguage());
        Classification savedClassification = classificationRepository.save(createRandomClassification());
        savedDocument = documentRepository.save(createRandomDocument(savedClassification));
    }

    @Test
    public void findAll_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;

        List<Translation> found = translationRepository.findAll();

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findAll_foundOne() {
        int foundOne = 1;
        translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));

        List<Translation> found = translationRepository.findAll();

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findAll_foundALot() {
        int foundALot = 2;
        translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));
        translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));

        List<Translation> found = translationRepository.findAll();

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findByDocumentId_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;
        long fakeDocumentId = generateId();

        List<Translation> found = translationRepository.findByDocumentId(fakeDocumentId);

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findByDocumentId_foundOne() {
        int foundOne = 1;
        translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));

        List<Translation> found = translationRepository.findByDocumentId(savedDocument.getEntityId());

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findByDocumentId_foundALot() {
        int foundALot = 2;
        translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));
        translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));

        List<Translation> found = translationRepository.findByDocumentId(savedDocument.getEntityId());

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findByClassificationId_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;
        long fakeClassificationId = generateId();

        List<Translation> found = translationRepository.findByClassificationId(fakeClassificationId);

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findByClassificationId_foundOne() {
        int foundOne = 1;
        translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));

        List<Translation> found = translationRepository.findByClassificationId(savedDocument.getClassification().getEntityId());

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findByClassificationId_foundALot() {
        int foundALot = 2;
        translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));
        translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));

        List<Translation> found = translationRepository.findByClassificationId(savedDocument.getClassification().getEntityId());

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findByFullName_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;
        String fakeFullName = randomString();

        List<Translation> found = translationRepository.findByFullName(fakeFullName);

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findByFullName_foundOne() {
        int foundOne = 1;
        translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));

        List<Translation> found = translationRepository.findByFullName(savedDocument.getName());

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findByFullName_foundALot() {
        int foundALot = 3;
        String name = randomString();

        Classification savedClassification = classificationRepository.save(createRandomClassification());
        Document saved1 = documentRepository.save(createRandomDocument(savedClassification));
        Document saved2 = documentRepository.save(createRandomDocument(savedClassification));
        Document saved3 = documentRepository.save(createRandomDocument(savedClassification));

        saved1 = documentRepository.patchFileName(saved1.toBuilder().fullName(saved1.getFullName() + name).build());
        saved2 = documentRepository.patchFileName(saved2.toBuilder().fullName(name + saved1.getFullName()).build());
        saved3 = documentRepository.patchFileName(saved3.toBuilder().fullName(name).build());

        translationRepository.save(createRandomTranslation(saved1, savedLanguage));
        translationRepository.save(createRandomTranslation(saved2, savedLanguage));
        translationRepository.save(createRandomTranslation(saved3, savedLanguage));

        List<Translation> documents = translationRepository.findByFullName(name);

        assertFalse(documents.isEmpty());
        assertEquals(foundALot, documents.size());
    }

    @Test
    public void findByKey_notFound() {
        TranslationKey fakeKey = TranslationKey.builder()
                .documentId(generateId())
                .languageId(generateId())
                .build();

        Optional<Translation> found = translationRepository.findByKey(fakeKey);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findByKey_found() {
        Translation saved = translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));

        Optional<Translation> found =
                translationRepository.findByKey(saved.key());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getDocument().toString(), found.get().getDocument().toString());
        assertEquals(saved.getLanguage().toString(), found.get().getLanguage().toString());
        assertNull(found.get().getData());
    }

    @Test
    public void findByKeyWithData_notFound() {
        TranslationKey fakeKey = TranslationKey.builder()
                .documentId(generateId())
                .languageId(generateId())
                .build();

        Optional<Translation> found = translationRepository.findByKeyWithData(fakeKey);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findByKeyWithData_notFound_found() {
        Translation saved = translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));

        Optional<Translation> found =
                translationRepository.findByKeyWithData(saved.key());

        assertTrue(found.isPresent());
        assertEquals(saved.getDocument().getFullName(), found.get().getDocument().getFullName());
        assertNotNull(found.get().getData());
    }

    @Test
    public void save() {
        Translation created = createRandomTranslation(savedDocument, savedLanguage);

        Translation saved = translationRepository.save(created);

        assertEquals(created.getDocument().toString(), saved.getDocument().toString());
        assertEquals(created.getLanguage().toString(), saved.getLanguage().toString());
        assertNull(saved.getData());
    }

    @Test
    public void save_languageNotFound() {
        Translation created = createRandomTranslation(savedDocument, createRandomLanguage());

        assertThrows(EntityNotFoundException.class, () -> translationRepository.save(created), "Language by id not found.");
    }

    @Test
    public void deleteById() {
        Translation saved = translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));
        translationRepository.deleteById(saved.getEntityId());

        Optional<Translation> found =
                translationRepository.findByKey(saved.key());

        assertTrue(found.isEmpty());
    }

    @Test
    public void deleteById_cascadeDocument() {
        Translation saved = translationRepository.save(createRandomTranslation(savedDocument, savedLanguage));
        documentRepository.deleteById(saved.getDocument().getEntityId());

        Optional<Translation> found =
                translationRepository.findByKey(saved.key());

        assertTrue(found.isEmpty());
    }
}
