package rgo.cloud.docs.db.api.repository;

import rgo.cloud.docs.internal.api.storage.Classification;

import java.util.List;
import java.util.Optional;

public interface ClassificationRepository {
    List<Classification> findAll();

    Optional<Classification> findById(Long entityId);

    Optional<Classification> findByName(String name);

    boolean exists(Long entityId);

    Classification save(Classification classification);

    Classification update(Classification classification);

    void deleteById(Long entityId);
}
