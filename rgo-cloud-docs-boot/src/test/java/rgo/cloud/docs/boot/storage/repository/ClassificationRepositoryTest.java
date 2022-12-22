package rgo.cloud.docs.boot.storage.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rgo.cloud.docs.boot.CommonTest;
import rgo.cloud.docs.internal.api.storage.Classification;

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
    private ClassificationRepository repository;

    @BeforeEach
    public void setUp() {
        truncateTables();
    }

    @Test
    public void findAll_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;

        List<Classification> found = repository.findAll();

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findAll_foundOne() {
        int foundOne = 1;
        repository.save(createRandomClassification());

        List<Classification> found = repository.findAll();

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findAll_foundALot() {
        int foundALot = 2;
        repository.save(createRandomClassification());
        repository.save(createRandomClassification());

        List<Classification> found = repository.findAll();

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findById_notFound() {
        long fakeId = generateId();

        Optional<Classification> found = repository.findById(fakeId);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findById_found() {
        Classification saved = repository.save(createRandomClassification());

        Optional<Classification> found = repository.findById(saved.getEntityId());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void findByName_notFound() {
        String fakeName = randomString();

        Optional<Classification> found = repository.findByName(fakeName);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findByName_found() {
        Classification saved = repository.save(createRandomClassification());

        Optional<Classification> found = repository.findByName(saved.getName());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void save() {
        Classification created = createRandomClassification();

        Classification saved = repository.save(created);

        assertEquals(created.getName(), saved.getName());
    }

    @Test
    public void update() {
        Classification saved = repository.save(createRandomClassification());
        Classification newObj = Classification.builder()
                .entityId(saved.getEntityId())
                .name(randomString())
                .build();

        Classification updated = repository.update(newObj);

        assertEquals(newObj.getEntityId(), updated.getEntityId());
        assertEquals(newObj.getName(), updated.getName());
    }

    @Test
    public void deleteById() {
        Classification saved = repository.save(createRandomClassification());
        repository.deleteById(saved.getEntityId());

        Optional<Classification> found = repository.findById(saved.getEntityId());

        assertTrue(found.isEmpty());
    }
}
