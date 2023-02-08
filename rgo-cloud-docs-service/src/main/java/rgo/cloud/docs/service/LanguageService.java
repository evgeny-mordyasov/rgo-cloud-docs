package rgo.cloud.docs.service;

import lombok.extern.slf4j.Slf4j;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.api.exception.ViolatesConstraintException;
import rgo.cloud.docs.db.api.repository.LanguageRepository;
import rgo.cloud.docs.db.api.entity.Language;

import java.util.List;
import java.util.Optional;

@Slf4j
public class LanguageService {
    private final LanguageRepository repository;

    public LanguageService(LanguageRepository repository) {
        this.repository = repository;
    }

    public List<Language> findAll() {
        return repository.findAll();
    }

    public Optional<Language> findById(Long entityId) {
        return repository.findById(entityId);
    }

    public Optional<Language> findByName(String name) {
        return repository.findByName(name);
    }

    public Language save(Language language) {
        checkNameForDuplicate(language.getName());
        return repository.save(language);
    }

    public Language update(Language language) {
        validateLanguage(language.getEntityId());
        return repository.update(language);
    }

    private void checkNameForDuplicate(String name) {
        repository.findByName(name).ifPresent(ignored -> {
            String errorMsg = "Language by name already exist.";
            log.error(errorMsg);
            throw new ViolatesConstraintException(errorMsg);
        });
    }

    private void validateLanguage(Long entityId) {
        if (!repository.exists(entityId)) {
            String errorMsg = "The language by id not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }
    }
}
