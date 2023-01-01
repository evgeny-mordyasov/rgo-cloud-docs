package rgo.cloud.docs.boot.api;

import org.springframework.web.bind.annotation.*;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.security.config.util.Endpoint;
import rgo.cloud.docs.boot.api.decorator.LanguageServiceDecorator;
import rgo.cloud.docs.internal.api.rest.language.request.LanguageGetByIdRequest;
import rgo.cloud.docs.internal.api.rest.language.request.LanguageGetByNameRequest;
import rgo.cloud.docs.internal.api.rest.language.request.LanguageSaveRequest;
import rgo.cloud.docs.internal.api.rest.language.request.LanguageUpdateRequest;

import static rgo.cloud.common.api.util.RequestUtil.JSON;
import static rgo.cloud.common.api.util.RequestUtil.execute;

@RestController
@RequestMapping(Endpoint.Language.BASE_URL)
public class LanguageRestController {
    private final LanguageServiceDecorator service;

    public LanguageRestController(LanguageServiceDecorator service) {
        this.service = service;
    }

    @GetMapping(produces = JSON)
    public Response findAll() {
        return execute(service::findAll);
    }

    @GetMapping(value = Endpoint.ENTITY_ID_VARIABLE, produces = JSON)
    public Response findById(@PathVariable Long entityId) {
        return execute(() -> service.findById(new LanguageGetByIdRequest(entityId)));
    }

    @GetMapping(params = "name", produces = JSON)
    public Response findByName(@RequestParam("name") String name) {
        return execute(() -> service.findByName(new LanguageGetByNameRequest(name)));
    }

    @PostMapping(consumes = JSON, produces = JSON)
    public Response save(@RequestBody LanguageSaveRequest rq) {
        return execute(() -> service.save(rq));
    }

    @PutMapping(consumes = JSON, produces = JSON)
    public Response update(@RequestBody LanguageUpdateRequest rq) {
        return execute(() -> service.update(rq));
    }
}
