package rgo.cloud.docs.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rgo.cloud.common.api.exception.ViolatesConstraintException;
import rgo.cloud.common.spring.test.CommonTest;
import rgo.cloud.docs.db.api.repository.LanguageRepository;
import rgo.cloud.docs.db.api.entity.Language;
import rgo.cloud.docs.service.LanguageService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static rgo.cloud.common.spring.util.TestCommonUtil.*;
import static rgo.cloud.docs.boot.EntityGenerator.createRandomLanguage;

@SpringBootTest
@ActiveProfiles("test")
public class LanguageServiceTest extends CommonTest {

    @Autowired
    private LanguageService service;

    @Autowired
    private LanguageRepository languageRepository;

    @BeforeEach
    public void setUp() {
        truncateTables();
    }

    @Test
    public void findAll_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;

        List<Language> found = service.findAll();

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findAll_foundOne() {
        int foundOne = 1;
        languageRepository.save(createRandomLanguage());

        List<Language> found = service.findAll();

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findAll_foundALot() {
        int foundALot = 2;
        languageRepository.save(createRandomLanguage());
        languageRepository.save(createRandomLanguage());

        List<Language> found = service.findAll();

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findById_notFound() {
        long fakeId = generateId();

        Optional<Language> found = service.findById(fakeId);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findById_found() {
        Language saved = languageRepository.save(createRandomLanguage());

        Optional<Language> found = service.findById(saved.getEntityId());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void findByName_notFound() {
        String fakeName = randomString();

        Optional<Language> found = service.findByName(fakeName);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findByName_found() {
        Language saved = languageRepository.save(createRandomLanguage());

        Optional<Language> found = service.findByName(saved.getName());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void save() {
        Language saved = service.save(createRandomLanguage());

        Optional<Language> found = languageRepository.findById(saved.getEntityId());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void save_nameAlreadyExists() {
        Language created = createRandomLanguage();
        languageRepository.save(created);

        assertThrows(ViolatesConstraintException.class, () -> service.save(created), "Language by name already exist.");
    }

    @Test
    public void update() {
        Language saved = languageRepository.save(createRandomLanguage());
        Language newObj = Language.builder()
                .entityId(saved.getEntityId())
                .name(randomString())
                .build();

        Language updatedLanguage = service.update(newObj);

        assertEquals(newObj.getEntityId(), updatedLanguage.getEntityId());
        assertEquals(newObj.getName(), updatedLanguage.getName());
    }
}
