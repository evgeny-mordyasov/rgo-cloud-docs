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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.docs.boot.EntityGenerator.createRandomClassification;
import static rgo.cloud.docs.boot.EntityGenerator.createRandomDocument;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentRepositoryTest extends CommonTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ClassificationRepository classificationRepository;

    private Classification savedClassification;

    @BeforeEach
    public void setUp() {
        truncateTables();
        savedClassification = classificationRepository.save(createRandomClassification());
    }

    @Test
    public void findById_notFound() {
        long fakeId = generateId();

        Optional<Document> found = documentRepository.findById(fakeId);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findById_found() {
        Document saved = documentRepository.save(createRandomDocument(savedClassification));

        Optional<Document> found = documentRepository.findById(saved.getEntityId());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getFullName(), found.get().getFullName());
        assertEquals(saved.getName(), found.get().getName());
        assertEquals(saved.getExtension(), found.get().getExtension());
        assertEquals(saved.getClassification().getEntityId(), found.get().getClassification().getEntityId());
        assertEquals(saved.getClassification().getName(), found.get().getClassification().getName());
    }

    @Test
    public void save() {
        Document created = createRandomDocument(savedClassification);

        Document saved = documentRepository.save(created);

        assertEquals(created.getFullName(), saved.getFullName());
        assertEquals(created.getName(), saved.getName());
        assertEquals(created.getExtension(), saved.getExtension());
        assertEquals(created.getClassification().getEntityId(), saved.getClassification().getEntityId());
        assertEquals(created.getClassification().getName(), saved.getClassification().getName());
    }

    @Test
    public void save_classificationNotFound() {
        Document created = createRandomDocument(createRandomClassification());

        assertThrows(EntityNotFoundException.class, () -> documentRepository.save(created), "Classification by id not found.");
    }

    @Test
    public void deleteById() {
        Document saved = documentRepository.save(createRandomDocument(savedClassification));
        documentRepository.deleteById(saved.getEntityId());

        Optional<Document> found = documentRepository.findById(saved.getEntityId());

        assertTrue(found.isEmpty());
    }

    @Test
    public void deleteById_cascade() {
        Document saved = documentRepository.save(createRandomDocument(savedClassification));
        classificationRepository.deleteById(savedClassification.getEntityId());

        Optional<Document> foundDocument = documentRepository.findById(saved.getEntityId());
        Optional<Classification> foundClassification = classificationRepository.findById(saved.getEntityId());

        assertTrue(foundDocument.isEmpty());
        assertTrue(foundClassification.isEmpty());
    }
}
