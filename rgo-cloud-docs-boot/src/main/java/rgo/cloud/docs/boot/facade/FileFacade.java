package rgo.cloud.docs.boot.facade;

import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.core.io.ByteArrayResource;
import rgo.cloud.common.api.exception.EntityNotFoundException;
import rgo.cloud.common.api.exception.UnpredictableException;
import rgo.cloud.docs.boot.service.DocumentLanguageService;
import rgo.cloud.docs.boot.service.DocumentService;
import rgo.cloud.docs.internal.api.facade.FileDto;
import rgo.cloud.docs.internal.api.storage.Document;
import rgo.cloud.docs.internal.api.storage.DocumentLanguage;

import java.util.*;

import static rgo.cloud.docs.boot.facade.FileFacadeMapper.convert;

@Slf4j
public class FileFacade {
    private final DocumentService documentService;
    private final DocumentLanguageService dlService;

    public FileFacade(DocumentService documentService, DocumentLanguageService dlService) {
        this.documentService = documentService;
        this.dlService = dlService;
    }

    public List<FileDto> findAll() {
        List<DocumentLanguage> dls = dlService.findAll();
        return convert(grouping(dls));
    }

    public List<FileDto> findByClassificationId(Long classificationId) {
        List<DocumentLanguage> dls = dlService.findByClassificationId(classificationId);
        return convert(grouping(dls));
    }

    private Set<Map.Entry<Long, List<DocumentLanguage>>> grouping(List<DocumentLanguage> dls) {
        return StreamEx.of(dls)
                .groupingBy(v -> v.getDocument().getEntityId())
                .entrySet();
    }

    public Optional<FileDto> findByDocumentId(Long documentId) {
        Optional<Document> document = documentService.findById(documentId);

        if (document.isEmpty()) {
            return Optional.empty();
        }

        List<DocumentLanguage> languages = dlService.findByDocumentId(documentId);

        return Optional.of(
                convert(document.get(), languages));
    }

    public FileResource load(Long documentId, Long languageId) {
        Optional<DocumentLanguage> opt = dlService.findByDocumentIdAndLanguageIdWithData(documentId, languageId);

        if (opt.isEmpty()) {
            String errorMsg = "The resource by documentId and languageId not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }

        return FileResource.builder()
                .withData(new ByteArrayResource(opt.get().getData()))
                .withFullFileName(opt.get().getDocument().getFullName())
                .build();
    }

    public FileDto save(DocumentLanguage dl) {
        Document savedDocument = documentService.save(dl.getDocument());
        DocumentLanguage savedDl = dlService.save(dl.toBuilder().document(savedDocument).build());

        return convert(savedDocument, Collections.singletonList(savedDl));
    }

    public FileDto patch(DocumentLanguage dl) {
        Optional<Document> opt = documentService.findById(dl.getDocument().getEntityId());
        if (opt.isEmpty()) {
            String errorMsg = "The document by documentId not found.";
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }

        dlService.save(DocumentLanguage.builder()
                .document(opt.get())
                .language(dl.getLanguage())
                .data(dl.getData())
                .build());

        List<DocumentLanguage> dls = dlService.findByDocumentId(dl.getDocument().getEntityId());

        return convert(opt.get(), dls);
    }

   public void deleteByDocumentId(Long documentId) {
        documentService.deleteById(documentId);
   }

   public void deleteByDocumentIdAndLanguageId(Long documentId, Long languageId) {
        List<DocumentLanguage> dls = dlService.findByDocumentId(documentId);

        if (dls.size() == 1) {
            documentService.deleteById(documentId);
        } else {
            Optional<DocumentLanguage> opt = dlService.findByDocumentIdAndLanguageId(documentId, languageId);

            if (opt.isEmpty()) {
                String errorMsg = "Error searching for the documentLanguage while deleting the documentLanguage.";
                log.error(errorMsg);
                throw new UnpredictableException(errorMsg);
            }

            dlService.deleteById(opt.get().getEntityId());
        }
   }
}
