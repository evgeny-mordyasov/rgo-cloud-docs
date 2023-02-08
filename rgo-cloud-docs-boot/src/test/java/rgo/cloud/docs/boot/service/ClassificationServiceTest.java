package rgo.cloud.docs.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.api.exception.ViolatesConstraintException;
import rgo.cloud.common.spring.test.CommonTest;
import rgo.cloud.docs.db.api.repository.ClassificationRepository;
import rgo.cloud.docs.db.api.entity.Classification;
import rgo.cloud.docs.service.ClassificationService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static rgo.cloud.common.spring.util.TestCommonUtil.*;
import static rgo.cloud.docs.boot.EntityGenerator.createRandomClassification;

@SpringBootTest
@ActiveProfiles("test")
public class ClassificationServiceTest extends CommonTest {

    @Autowired
    private ClassificationService service;

    @Autowired
    private ClassificationRepository classificationRepository;

    @BeforeEach
    public void setUp() {
        truncateTables();
    }

    @Test
    public void findAll_noOneHasBeenFound() {
        int noOneHasBeenFound = 0;

        List<Classification> found = service.findAll();

        assertEquals(noOneHasBeenFound, found.size());
    }

    @Test
    public void findAll_foundOne() {
        int foundOne = 1;
        classificationRepository.save(createRandomClassification());

        List<Classification> found = service.findAll();

        assertEquals(foundOne, found.size());
    }

    @Test
    public void findAll_foundALot() {
        int foundALot = 2;
        classificationRepository.save(createRandomClassification());
        classificationRepository.save(createRandomClassification());

        List<Classification> found = service.findAll();

        assertEquals(foundALot, found.size());
    }

    @Test
    public void findById_notFound() {
        long fakeId = generateId();

        Optional<Classification> found = service.findById(fakeId);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findById_found() {
        Classification saved = classificationRepository.save(createRandomClassification());

        Optional<Classification> found = service.findById(saved.getEntityId());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void findByName_notFound() {
        String fakeName = randomString();

        Optional<Classification> found = service.findByName(fakeName);

        assertTrue(found.isEmpty());
    }

    @Test
    public void findByName_found() {
        Classification saved = classificationRepository.save(createRandomClassification());

        Optional<Classification> found = service.findByName(saved.getName());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void save() {
        Classification saved = service.save(createRandomClassification());

        Optional<Classification> found = classificationRepository.findById(saved.getEntityId());

        assertTrue(found.isPresent());
        assertEquals(saved.getEntityId(), found.get().getEntityId());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    public void save_nameAlreadyExists() {
        Classification created = createRandomClassification();
        classificationRepository.save(created);

        assertThrows(ViolatesConstraintException.class, () -> service.save(created), "Classification by name already exist.");
    }

    @Test
    public void update() {
        Classification saved = classificationRepository.save(createRandomClassification());
        Classification newObj = Classification.builder()
                .entityId(saved.getEntityId())
                .name(randomString())
                .build();

        Classification updatedClassification = service.update(newObj);

        assertEquals(newObj.getEntityId(), updatedClassification.getEntityId());
        assertEquals(newObj.getName(), updatedClassification.getName());
    }

    @Test
    public void deleteById_notFound() {
        long fakeId = generateId();

        assertThrows(EntityNotFoundException.class, () -> service.deleteById(fakeId), "The classification by id not found.");
    }

    @Test
    public void deleteById() {
        Classification saved = classificationRepository.save(createRandomClassification());
        service.deleteById(saved.getEntityId());

        Optional<Classification> found = classificationRepository.findById(saved.getEntityId());

        assertTrue(found.isEmpty());
    }
}
