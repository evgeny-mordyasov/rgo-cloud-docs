package rgo.cloud.docs.boot.storage.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rgo.cloud.common.spring.test.CommonTest;
import rgo.cloud.docs.internal.api.storage.Classification;
import rgo.cloud.docs.internal.api.storage.Document;
import rgo.cloud.docs.internal.api.storage.Language;
import rgo.cloud.docs.internal.api.storage.ReadingDocument;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static rgo.cloud.docs.boot.EntityGenerator.*;
import static rgo.cloud.docs.boot.EntityGenerator.createRandomDocument;

@SpringBootTest
@ActiveProfiles("test")
public class ReadingDocumentRepositoryTest extends CommonTest {

    @Autowired
    private ReadingDocumentRepository repository;

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
    public void save() {
        ReadingDocument created = createRandomReadingDocument(savedDocument.getEntityId(), savedLanguage.getEntityId());

        ReadingDocument saved = repository.save(created);

        assertEquals(created.getDocumentId(), saved.getDocumentId());
        assertEquals(created.getLanguageId(), saved.getLanguageId());
    }
}