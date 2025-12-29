package ${package}.interfaces.core.domain.user;

import ${package}.api.domain.user.IUserService;
import ${package}.interfaces.api.domain.user.UserRestClient;
import ${package}.interfaces.api.domain.user.dto.UserAPI;
import ${package}.interfaces.api.domain.user.dto.UserDTOSearchRequestAPI;
import ${package}.interfaces.core.domain.user.mapper.UserAPIMapper;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import io.inugami.framework.interfaces.models.search.SearchResponseMapperUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @since 2025-12-29
 */
@RequiredArgsConstructor
@Builder
@RestController
public class UserRestController implements UserRestClient {
    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    private final IUserService  userService;
    private final UserAPIMapper userAPIMapper;

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @Override
    public Collection<UserAPI> create(final Collection<UserAPI> values) {
        final var request = userAPIMapper.convertToCore(values);
        return userAPIMapper.convertToRest(userService.create(request));
    }

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Override
    public SearchResponse<UserAPI> search(final UserDTOSearchRequestAPI searchRequest) {
        final var request  = userAPIMapper.convertToCore(searchRequest);
        final var response = userService.search(request);
        return SearchResponseMapperUtils.convert(response, userAPIMapper::convertToRest);
    }

    @Override
    public UserAPI getById(final String uid, final boolean full) {
        return userAPIMapper.convertToRest(userService.getById(uid, full));
    }

    //==================================================================================================================
    // UPDATE
    //==================================================================================================================
    @Override
    public Collection<UserAPI> updateForce(final Collection<UserAPI> values) {
        final var request = userAPIMapper.convertToCore(values);
        return userAPIMapper.convertToRest(userService.update(request, true));
    }

    @Override
    public Collection<UserAPI> update(final Collection<UserAPI> values) {
        final var request = userAPIMapper.convertToCore(values);
        return userAPIMapper.convertToRest(userService.update(request, false));
    }

    //==================================================================================================================
    // DELETE
    //==================================================================================================================
    @Override
    public void delete(final Collection<String> uid) {
        userService.delete(uid);
    }
}
