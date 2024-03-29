package rgo.cloud.docs.db.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rgo.cloud.common.spring.test.PersistenceTest;
import rgo.cloud.docs.db.api.entity.Language;
import rgo.cloud.docs.db.config.PersistenceConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;
import static rgo.cloud.docs.db.utils.EntityGenerator.createRandomLanguage;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class LanguageRepositoryTest extends PersistenceTest {

    @Autowired
    private LanguageRepository languageRepository;

    @BeforeEach
    public void setUp() {
        truncateTables();
    }

    @Test
    public void findAll_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;

        List<Language> found = languageRepository.findAll();

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findAll_foundOne() {
        int foundOne = 1;
        languageRepository.save(createRandomLanguage());

        List<Language> found = languageRepository.findAll();

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findAll_foundALot() {
        int foundALot = 2;
        languageRepository.save(createRandomLanguage());
        languageRepository.save(createRandomLanguage());

        List<Language> found = languageRepository.findAll();

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findById_notFound() {
        long fakeId = generateId();

        Optional<Language> found = languageRepository.findById(fakeId);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findById_found() {
        Language saved = languageRepository.save(createRandomLanguage());

        Optional<Language> found = languageRepository.findById(saved.getEntityId());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void findByName_notFound() {
        String fakeName = randomString();

        Optional<Language> found = languageRepository.findByName(fakeName);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findByName_found() {
        Language saved = languageRepository.save(createRandomLanguage());

        Optional<Language> found = languageRepository.findByName(saved.getName());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void save() {
        Language created = createRandomLanguage();

        Language saved = languageRepository.save(created);

        assertEquals(created.getName(), saved.getName());
    }

    @Test
    public void update() {
        Language saved = languageRepository.save(createRandomLanguage());
        Language newObj = Language.builder()
                .entityId(saved.getEntityId())
                .name(randomString())
                .build();

        Language updated = languageRepository.update(newObj);

        assertEquals(newObj.getEntityId(), updated.getEntityId());
        assertEquals(newObj.getName(), updated.getName());
    }
}
