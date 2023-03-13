package rgo.cloud.docs.facade;

import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.docs.facade.api.FileResource;
import rgo.cloud.docs.service.TranslationService;
import rgo.cloud.docs.service.DocumentService;
import rgo.cloud.docs.service.LanguageService;
import rgo.cloud.docs.service.ReadingDocumentService;
import rgo.cloud.docs.db.api.entity.*;
import rgo.cloud.docs.facade.api.FileDto;

import java.util.*;
import java.util.stream.Collectors;

import static rgo.cloud.common.api.util.ExceptionUtil.unpredictableError;
import static rgo.cloud.docs.facade.FileFacadeMapper.convert;

@Slf4j
public class FileFacade {
    private final DocumentService documentService;
    private final TranslationService translationService;
    private final LanguageService languageService;
    private final ReadingDocumentService readingDocumentService;

    public FileFacade(DocumentService documentService,
                      TranslationService translationService,
                      LanguageService languageService,
                      ReadingDocumentService readingDocumentService) {
        this.documentService = documentService;
        this.translationService = translationService;
        this.languageService = languageService;
        this.readingDocumentService = readingDocumentService;
    }

    public List<FileDto> findAll() {
        List<Translation> translations = translationService.findAll();
        return convert(grouping(translations));
    }

    public List<FileDto> findByClassificationId(Long classificationId) {
        List<Translation> translations = translationService.findByClassificationId(classificationId);
        return convert(grouping(translations));
    }

    public List<FileDto> findByFullName(String name) {
        List<Translation> translations = translationService.findByFullName(name);
        return convert(grouping(translations));
    }

    private Set<Map.Entry<Long, List<Translation>>> grouping(List<Translation> translations) {
        return StreamEx.of(translations)
                .groupingBy(v -> v.getDocument().getEntityId())
                .entrySet();
    }

    public Optional<FileDto> findByDocumentId(Long documentId) {
        Optional<Document> document = documentService.findById(documentId);

        if (document.isEmpty()) {
            return Optional.empty();
        }

        List<Translation> translations = translationService.findByDocumentId(documentId);

        return Optional.of(
                convert(document.get(), translations));
    }

    public List<Language> getFreeLanguages(Long documentId) {
        List<Language> list = translationService.findByDocumentId(documentId)
                .stream()
                .map(Translation::getLanguage)
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            String errorMsg = "The document not found by documentId.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }

        List<Language> allLanguages = languageService.findAll();
        allLanguages.removeAll(list);

        return allLanguages;
    }

    public FileResource load(TranslationKey key) {
        Optional<Translation> opt = translationService.findByKeyWithData(key);

        if (opt.isEmpty()) {
            String errorMsg = "The resource by documentId and languageId not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }

        readingDocumentService.save(ReadingDocument.builder().key(key).build());

        return FileResource.builder()
                .withData(opt.get().getData())
                .withFullFileName(opt.get().getDocument().getFullName())
                .build();
    }

    public FileDto save(Translation tr) {
        Document savedDocument = documentService.save(tr.getDocument());
        Translation savedTranslation = translationService.save(tr.toBuilder().document(savedDocument).build());

        return convert(savedDocument, Collections.singletonList(savedTranslation));
    }

    public FileDto patch(Translation tr) {
        Optional<Document> opt = documentService.findById(tr.getDocument().getEntityId());
        if (opt.isEmpty()) {
            String errorMsg = "The document by documentId not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }

        translationService.save(Translation.builder()
                .document(opt.get())
                .language(tr.getLanguage())
                .data(tr.getData())
                .build());

        List<Translation> translations = translationService.findByDocumentId(tr.getDocument().getEntityId());

        return convert(opt.get(), translations);
    }

    public FileDto patchFileName(Document document) {
        Document updatedDocument = documentService.patchFileName(document);
        List<Translation> translations = translationService.findByDocumentId(document.getEntityId());

        return convert(updatedDocument, translations);
    }

   public void deleteByDocumentId(Long documentId) {
        documentService.deleteById(documentId);
   }

   public void deleteByKey(TranslationKey key) {
        List<Translation> translations = translationService.findByDocumentId(key.getDocumentId());

        if (translations.isEmpty()) {
            String errorMsg = "The translation by documentId not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }

        if (translations.size() == 1) {
            documentService.deleteById(key.getDocumentId());
        } else {
            Optional<Translation> opt = translationService.findByKey(key);
            if (opt.isEmpty()) {
                unpredictableError("Error searching for the translation while deleting the translation.");
            }

            translationService.deleteById(opt.get().getEntityId());
        }
   }
}
