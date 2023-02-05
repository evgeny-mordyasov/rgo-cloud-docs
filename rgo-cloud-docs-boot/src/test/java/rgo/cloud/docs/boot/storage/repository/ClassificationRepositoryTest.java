package rgo.cloud.docs.boot.storage.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rgo.cloud.common.spring.test.CommonTest;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rgo.cloud.common.spring.util.TestCommonUtil.generateId;
import static rgo.cloud.common.spring.util.TestCommonUtil.randomString;
import static rgo.cloud.docs.boot.EntityGenerator.createRandomClassification;

@SpringBootTest
@ActiveProfiles("test")
public class ClassificationRepositoryTest extends CommonTest {

    @Autowired
    private ClassificationRepository classificationRepository;

    @BeforeEach
    public void setUp() {
        truncateTables();
    }

    @Test
    public void findAll_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;

        List<Classification> found = classificationRepository.findAll();

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findAll_foundOne() {
        int foundOne = 1;
        classificationRepository.save(createRandomClassification());

        List<Classification> found = classificationRepository.findAll();

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findAll_foundALot() {
        int foundALot = 2;
        classificationRepository.save(createRandomClassification());
        classificationRepository.save(createRandomClassification());

        List<Classification> found = classificationRepository.findAll();

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findById_notFound() {
        long fakeId = generateId();

        Optional<Classification> found = classificationRepository.findById(fakeId);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findById_found() {
        Classification saved = classificationRepository.save(createRandomClassification());

        Optional<Classification> found = classificationRepository.findById(saved.getEntityId());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void findByName_notFound() {
        String fakeName = randomString();

        Optional<Classification> found = classificationRepository.findByName(fakeName);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findByName_found() {
        Classification saved = classificationRepository.save(createRandomClassification());

        Optional<Classification> found = classificationRepository.findByName(saved.getName());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void save() {
        Classification created = createRandomClassification();

        Classification saved = classificationRepository.save(created);

        assertEquals(created.getName(), saved.getName());
    }

    @Test
    public void update() {
        Classification saved = classificationRepository.save(createRandomClassification());
        Classification newObj = Classification.builder()
                .entityId(saved.getEntityId())
                .name(randomString())
                .build();

        Classification updated = classificationRepository.update(newObj);

        assertEquals(newObj.getEntityId(), updated.getEntityId());
        assertEquals(newObj.getName(), updated.getName());
    }

    @Test
    public void deleteById() {
        Classification saved = classificationRepository.save(createRandomClassification());
        classificationRepository.deleteById(saved.getEntityId());

        Optional<Classification> found = classificationRepository.findById(saved.getEntityId());

        assertTrue(found.isEmpty());
    }
}
