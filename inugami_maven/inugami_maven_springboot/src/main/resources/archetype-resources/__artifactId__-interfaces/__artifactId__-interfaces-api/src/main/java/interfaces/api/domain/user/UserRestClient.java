package ${package}.interfaces.api.domain.user;

import ${package}.interfaces.api.domain.user.dto.UserAPI;
import ${package}.interfaces.api.domain.user.dto.UserDTOSearchRequestAPI;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import io.inugami.framework.interfaces.rest.GenericHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping(path = "ws/user")
public interface UserRestClient {
    // =================================================================================================================
    // CREATE
    // =================================================================================================================
    @GenericHeaders
    @UserRestClientDOC.DocCreate
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Collection<UserAPI> create(@RequestBody final Collection<UserAPI> values);

    // =================================================================================================================
    // READ
    // =================================================================================================================
    @GenericHeaders
    @UserRestClientDOC.DocSearch
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    SearchResponse<UserAPI> search(final UserDTOSearchRequestAPI searchRequest);

    @GenericHeaders
    @UserRestClientDOC.DocGetById
    @GetMapping(path = "{uid}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserAPI getById(@PathVariable(required = true) final String uid,
                          @RequestParam(name = "full", defaultValue = "false", required = false) final boolean full);

    // =================================================================================================================
    // UPDATE
    // =================================================================================================================
    @GenericHeaders
    @UserRestClientDOC.DocUpdateForce
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Collection<UserAPI> updateForce(@RequestBody final Collection<UserAPI> values);

    @GenericHeaders
    @UserRestClientDOC.DocUpdate
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Collection<UserAPI> update(@RequestBody final Collection<UserAPI> values);

    // =================================================================================================================
    // DELETE
    // =================================================================================================================
    @GenericHeaders
    @UserRestClientDOC.DocDelete
    @DeleteMapping()
    void delete(@RequestParam(name = "uid") final Collection<String> uid);


}
