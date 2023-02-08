package rgo.cloud.docs.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.spring.test.CommonTest;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;
import rgo.cloud.docs.db.api.repository.DocumentRepository;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.db.api.entity.Document;
import rgo.cloud.docs.service.DocumentService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.docs.boot.EntityGenerator.createRandomClassification;
import static rgo.cloud.docs.boot.EntityGenerator.createRandomDocument;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentServiceTest extends CommonTest {

    @Autowired
    private DocumentService service;

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

        Optional<Document> found = service.findById(fakeId);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findById_found() {
        Document saved = documentRepository.save(createRandomDocument(savedClassification));

        Optional<Document> found = service.findById(saved.getEntityId());

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

        Document saved = service.save(created);

        assertEquals(created.getFullName(), saved.getFullName());
        assertEquals(created.getName(), saved.getName());
        assertEquals(created.getExtension(), saved.getExtension());
        assertEquals(created.getClassification().getEntityId(), saved.getClassification().getEntityId());
        assertEquals(created.getClassification().getName(), saved.getClassification().getName());
    }

    @Test
    public void deleteById_notFound() {
        long fakeId = generateId();

        assertThrows(EntityNotFoundException.class, () -> service.deleteById(fakeId), "The document by id not found.");
    }

    @Test
    public void deleteById() {
        Document saved = documentRepository.save(createRandomDocument(savedClassification));
        service.deleteById(saved.getEntityId());

        Optional<Document> found = documentRepository.findById(saved.getEntityId());

        assertTrue(found.isEmpty());
    }
}
