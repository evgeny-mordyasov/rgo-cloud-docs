package rgo.cloud.docs.boot.api;

import org.springframework.web.bind.annotation.*;
import rgo.cloud.common.api.rest.Response;
import rgo.cloud.docs.rest.api.classification.request.*;
import rgo.cloud.security.config.util.Endpoint;
import rgo.cloud.docs.boot.api.decorator.ClassificationServiceDecorator;

import static rgo.cloud.common.api.util.RequestUtil.JSON;
import static rgo.cloud.common.api.util.RequestUtil.execute;

@RestController
@RequestMapping(Endpoint.Classification.BASE_URL)
public class ClassificationRestController {
    private final ClassificationServiceDecorator service;

    public ClassificationRestController(ClassificationServiceDecorator service) {
        this.service = service;
    }

    @GetMapping(produces = JSON)
    public Response findAll() {
        return execute(service::findAll);
    }

    @GetMapping(value = Endpoint.ENTITY_ID_VARIABLE, produces = JSON)
    public Response findById(@PathVariable Long entityId) {
        return execute(() ->
                service.findById(new ClassificationGetByIdRequest(entityId)));
    }

    @GetMapping(params = "name", produces = JSON)
    public Response findByName(@RequestParam("name") String name) {
        return execute(() ->
                service.findByName(new ClassificationGetByNameRequest(name)));
    }

    @PostMapping(consumes = JSON, produces = JSON)
    public Response save(@RequestBody ClassificationSaveRequest rq) {
        return execute(() -> service.save(rq));
    }

    @PutMapping(consumes = JSON, produces = JSON)
    public Response update(@RequestBody ClassificationUpdateRequest rq) {
        return execute(() -> service.update(rq));
    }

    @DeleteMapping(value = Endpoint.ENTITY_ID_VARIABLE, produces = JSON)
    public Response deleteById(@PathVariable Long entityId) {
        return execute(() ->
                service.deleteById(new ClassificationDeleteByIdRequest(entityId)));
    }
}
