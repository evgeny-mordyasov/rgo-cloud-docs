package rgo.cloud.docs.boot.storage.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.docs.boot.CommonTest;
import rgo.cloud.docs.internal.api.storage.Classification;
import rgo.cloud.docs.internal.api.storage.Document;
import rgo.cloud.docs.internal.api.storage.DocumentLanguage;
import rgo.cloud.docs.internal.api.storage.Language;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.docs.boot.EntityGenerator.*;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentLanguageRepositoryTest extends CommonTest {
    
    @Autowired
    private DocumentLanguageRepository documentLanguageRepository;
    
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

        List<DocumentLanguage> found = documentLanguageRepository.findAll();

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findAll_foundOne() {
        int foundOne = 1;
        documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));

        List<DocumentLanguage> found = documentLanguageRepository.findAll();

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findAll_foundALot() {
        int foundALot = 2;
        documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));
        documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));

        List<DocumentLanguage> found = documentLanguageRepository.findAll();

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findByDocumentId_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;
        long fakeDocumentId = generateId();

        List<DocumentLanguage> found = documentLanguageRepository.findByDocumentId(fakeDocumentId);

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findByDocumentId_foundOne() {
        int foundOne = 1;
        documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));

        List<DocumentLanguage> found = documentLanguageRepository.findByDocumentId(savedDocument.getEntityId());

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findByDocumentId_foundALot() {
        int foundALot = 2;
        documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));
        documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));

        List<DocumentLanguage> found = documentLanguageRepository.findByDocumentId(savedDocument.getEntityId());

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findByClassificationId_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;
        long fakeClassificationId = generateId();

        List<DocumentLanguage> found = documentLanguageRepository.findByClassificationId(fakeClassificationId);

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findByClassificationId_foundOne() {
        int foundOne = 1;
        documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));

        List<DocumentLanguage> found = documentLanguageRepository.findByClassificationId(savedDocument.getClassification().getEntityId());

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findByClassificationId_foundALot() {
        int foundALot = 2;
        documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));
        documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));

        List<DocumentLanguage> found = documentLanguageRepository.findByClassificationId(savedDocument.getClassification().getEntityId());

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findByDocumentIdAndLanguageId_notFound() {
        long fakeDocumentId = generateId();
        long fakeLanguageId = generateId();

        Optional<DocumentLanguage> found = documentLanguageRepository.findByDocumentIdAndLanguageId(fakeDocumentId, fakeLanguageId);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findByDocumentIdAndLanguageId_found() {
        DocumentLanguage saved = documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));

        Optional<DocumentLanguage> found =
                documentLanguageRepository.findByDocumentIdAndLanguageId(saved.getDocument().getEntityId(), saved.getLanguage().getEntityId());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getDocument().toString(), found.get().getDocument().toString());
        assertEquals(saved.getLanguage().toString(), found.get().getLanguage().toString());
        assertNull(found.get().getData());
    }

    @Test
    public void findByDocumentIdAndLanguageIdWithData_notFound() {
        long fakeDocumentId = generateId();
        long fakeLanguageId = generateId();

        Optional<DocumentLanguage> found = documentLanguageRepository.findByDocumentIdAndLanguageIdWithData(fakeDocumentId, fakeLanguageId);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findByDocumentIdAndLanguageIdWithData_notFound_found() {
        DocumentLanguage saved = documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));

        Optional<DocumentLanguage> found =
                documentLanguageRepository.findByDocumentIdAndLanguageIdWithData(saved.getDocument().getEntityId(), saved.getLanguage().getEntityId());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getDocument().toString(), found.get().getDocument().toString());
        assertEquals(saved.getLanguage().toString(), found.get().getLanguage().toString());
        assertNotNull(found.get().getData());
    }

    @Test
    public void save() {
        DocumentLanguage created = createRandomDocumentLanguage(savedDocument, savedLanguage);

        DocumentLanguage saved = documentLanguageRepository.save(created);

        assertEquals(created.getDocument().toString(), saved.getDocument().toString());
        assertEquals(created.getLanguage().toString(), saved.getLanguage().toString());
        assertNull(saved.getData());
    }

    @Test
    public void save_languageNotFound() {
        DocumentLanguage created = createRandomDocumentLanguage(savedDocument, createRandomLanguage());

        assertThrows(EntityNotFoundException.class, () -> documentLanguageRepository.save(created), "Language by id not found.");
    }

    @Test
    public void deleteById() {
        DocumentLanguage saved = documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));
        documentLanguageRepository.deleteById(saved.getEntityId());

        Optional<DocumentLanguage> found =
                documentLanguageRepository.findByDocumentIdAndLanguageId(saved.getDocument().getEntityId(), saved.getLanguage().getEntityId());

        assertTrue(found.isEmpty());
    }

    @Test
    public void deleteById_cascadeDocument() {
        DocumentLanguage saved = documentLanguageRepository.save(createRandomDocumentLanguage(savedDocument, savedLanguage));
        documentRepository.deleteById(saved.getDocument().getEntityId());

        Optional<DocumentLanguage> found =
                documentLanguageRepository.findByDocumentIdAndLanguageId(saved.getDocument().getEntityId(), saved.getLanguage().getEntityId());

        assertTrue(found.isEmpty());
    }
}
