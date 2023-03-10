package rgo.cloud.docs.boot.api;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.security.config.util.Endpoint;
import rgo.cloud.docs.boot.api.decorator.LanguageServiceDecorator;
import rgo.cloud.docs.rest.api.language.request.LanguageGetByIdRequest;
import rgo.cloud.docs.rest.api.language.request.LanguageGetByNameRequest;
import rgo.cloud.docs.rest.api.language.request.LanguageSaveRequest;
import rgo.cloud.docs.rest.api.language.request.LanguageUpdateRequest;

import static rgo.cloud.common.spring.util.RequestUtil.JSON;
import static rgo.cloud.common.spring.util.RequestUtil.execute;

@Hidden
@RestController
@RequestMapping(Endpoint.Language.BASE_URL)
public class LanguageRestController {
    private final LanguageServiceDecorator service;

    public LanguageRestController(LanguageServiceDecorator service) {
        this.service = service;
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<Response> findAll() {
        return execute(service::findAll);
    }

    @GetMapping(value = Endpoint.ENTITY_ID_VARIABLE, produces = JSON)
    public ResponseEntity<Response> findById(@PathVariable Long entityId) {
        return execute(() -> service.findById(new LanguageGetByIdRequest(entityId)));
    }

    @GetMapping(params = "name", produces = JSON)
    public ResponseEntity<Response> findByName(@RequestParam("name") String name) {
        return execute(() -> service.findByName(new LanguageGetByNameRequest(name)));
    }

    @PostMapping(consumes = JSON, produces = JSON)
    public ResponseEntity<Response> save(@RequestBody LanguageSaveRequest rq) {
        return execute(() -> service.save(rq));
    }

    @PutMapping(consumes = JSON, produces = JSON)
    public ResponseEntity<Response> update(@RequestBody LanguageUpdateRequest rq) {
        return execute(() -> service.update(rq));
    }
}
