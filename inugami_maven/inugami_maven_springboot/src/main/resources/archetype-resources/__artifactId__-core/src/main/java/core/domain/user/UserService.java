package ${package}.core.domain.user;

import ${package}.api.domain.user.IUserDao;
import ${package}.api.domain.user.IUserService;
import ${package}.api.domain.user.dto.UserDTO;
import ${package}.api.domain.user.dto.UserDTOSearchRequestDTO;
import ${package}.api.domain.user.dto.UserFilters;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import io.inugami.framework.interfaces.tools.ListUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ${package}.api.domain.user.exception.UserErrors.*;
import static io.inugami.framework.interfaces.exceptions.Asserts.*;
import static io.inugami.framework.interfaces.exceptions.DefaultErrorCode.fromErrorCode;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    protected static final String FIELD_UID       = "{0}.uid";
    protected static final String FIELD_FIRSTNAME = "{0}.firstName";
    protected static final String FIELD_LASTNAME  = "{0}.lastName";
    protected static final String FIELD_EMAIL     = "{0}.email";
    protected static final int    MAX_SIZE        = 256;

    private final IUserDao userDao;

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @Transactional
    @Override
    public Collection<UserDTO> create(final Collection<UserDTO> values) {
        assertNotEmpty(CREATE_INVALID_DATA, values);

        assertModel(values,
                    (v, index) -> assertNull(fromErrorCode(CREATE_UID_FORBIDDEN)
                                                     .addField(FIELD_UID, index)
                                                     .build(), v.getUid()),

                    (v, index) -> {
                        assertNotEmpty(fromErrorCode(CREATE_FIRSTNAME_INVALID)
                                               .addField(FIELD_FIRSTNAME, index)
                                               .build(), v.getFirstName());
                        v.setFirstName(v.getFirstName().trim());
                        assertLowerOrEquals(fromErrorCode(CREATE_FIRSTNAME_INVALID)
                                                    .addField(FIELD_FIRSTNAME, index)
                                                    .build(), MAX_SIZE, v.getFirstName().length());
                    },

                    (v, index) -> {
                        assertNotEmpty(fromErrorCode(CREATE_LASTNAME_INVALID)
                                               .addField(FIELD_LASTNAME, index)
                                               .build(), v.getLastName());
                        v.setLastName(v.getLastName().trim());
                        assertLowerOrEquals(fromErrorCode(CREATE_LASTNAME_INVALID)
                                                    .addField(FIELD_LASTNAME, index)
                                                    .build(), MAX_SIZE, v.getLastName().length());
                    },

                    (v, index) -> {
                        assertNotEmpty(fromErrorCode(CREATE_EMAIL_INVALID)
                                               .addField(FIELD_EMAIL, index)
                                               .build(), v.getEmail());
                        v.setEmail(v.getEmail().trim());
                        assertRegexMatch(fromErrorCode(CREATE_EMAIL_INVALID)
                                                 .addField(FIELD_EMAIL, index)
                                                 .build(), EMAIL_REGEX, v.getEmail());
                    });

        return userDao.create(values);
    }

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Override
    public SearchResponse<UserDTO> search(final UserDTOSearchRequestDTO searchRequest) {
        return userDao.search(Optional.ofNullable(searchRequest).orElse(UserDTOSearchRequestDTO.builder().build()),
                              UserFilters.FILTERS);
    }

    @Override
    public UserDTO getById(final String id, final Boolean full) {
        assertRegexMatch(READ_USER_UID_REQUIRED, UID_REGEX, id);

        final var result = userDao.getById(id, full);

        assertNotNull(READ_USER_NOT_FOUND, result);
        return result;
    }

    @Override
    public boolean contains(final Collection<String> uids) {
        if (ListUtils.isEmpty(uids)) {
            return false;
        }
        return userDao.contains(uids);
    }

    @Override
    public Collection<UserDTO> getByIds(final Collection<String> uids) {
        if (ListUtils.isEmpty(uids)) {
            return List.of();
        }
        return userDao.getByIds(uids);
    }

    //==================================================================================================================
    // UPDATE
    //==================================================================================================================
    @Transactional
    @Override
    public Collection<UserDTO> update(final Collection<UserDTO> values, final boolean force) {
        assertNotEmpty(UPDATE_INVALID_DATA, values);
        assertModel(values,
                    (v, index) -> {
                        assertNotNull(fromErrorCode(UPDATE_UID_REQUIRED)
                                              .addField(FIELD_UID, index)
                                              .build(), v.getUid());

                        assertRegexMatch(fromErrorCode(UPDATE_UID_REQUIRED)
                                                 .addField(FIELD_UID, index)
                                                 .build(), UID_REGEX, v.getUid());
                    }
                   );

        final var uids = values.stream().map(UserDTO::getUid).toList();
        assertTrue(UPDATE_USERS_NOT_FOUND, contains(uids));
        final var           savedUsers  = getByIds(uids);
        final List<UserDTO> usersToSave = new ArrayList<>();

        for (UserDTO user : values) {
            final UserDTO savedUser = findUser(user.getUid(), savedUsers);
            assertNotNull(UNDEFINED, savedUser);
            usersToSave.add(mergeUser(user, savedUser, force));
        }

        assertModel(usersToSave,
                    (v, index) -> {
                        assertNotEmpty(fromErrorCode(UPDATE_FIRSTNAME_INVALID)
                                               .addField(FIELD_FIRSTNAME, index)
                                               .build(), v.getFirstName());
                        v.setFirstName(v.getFirstName().trim());
                        assertLowerOrEquals(fromErrorCode(UPDATE_FIRSTNAME_INVALID)
                                                    .addField(FIELD_FIRSTNAME, index)
                                                    .build(), MAX_SIZE, v.getFirstName().length());
                    },

                    (v, index) -> {
                        assertNotEmpty(fromErrorCode(UPDATE_LASTNAME_INVALID)
                                               .addField(FIELD_LASTNAME, index)
                                               .build(), v.getLastName());
                        v.setLastName(v.getLastName().trim());
                        assertLowerOrEquals(fromErrorCode(UPDATE_LASTNAME_INVALID)
                                                    .addField(FIELD_LASTNAME, index)
                                                    .build(), MAX_SIZE, v.getLastName().length());
                    },

                    (v, index) -> {
                        assertNotEmpty(fromErrorCode(UPDATE_EMAIL_INVALID)
                                               .addField(FIELD_EMAIL, index)
                                               .build(), v.getEmail());
                        v.setEmail(v.getEmail().trim());
                        assertRegexMatch(fromErrorCode(UPDATE_EMAIL_INVALID)
                                                 .addField(FIELD_EMAIL, index)
                                                 .build(), EMAIL_REGEX, v.getEmail());
                    });

        return userDao.update(usersToSave);
    }

    private UserDTO mergeUser(final UserDTO user,
                              final UserDTO savedUser,
                              final boolean force) {

        return force ? forceMerge(user, savedUser)
                     : softMerge(user, savedUser);
    }


    private UserDTO forceMerge(final UserDTO user, final UserDTO savedUser) {
        savedUser.setEmail(user.getEmail());
        savedUser.setFirstName(user.getFirstName());
        savedUser.setLastName(user.getLastName());
        return savedUser;
    }

    private UserDTO softMerge(final UserDTO user, final UserDTO savedUser) {
        applyIfNotNull(user.getEmail(), savedUser::setEmail);
        applyIfNotNull(user.getFirstName(), savedUser::setFirstName);
        applyIfNotNull(user.getLastName(), savedUser::setLastName);
        return savedUser;
    }

    protected UserDTO findUser(final String uid, final Collection<UserDTO> savedUsers) {
        return savedUsers.stream()
                         .filter(u -> u.getUid().equals(uid))
                         .findFirst()
                         .orElse(null);
    }

    //==================================================================================================================
    // DELETE
    //==================================================================================================================
    @Transactional
    @Override
    public void delete(final Collection<String> ids) {
        assertNotEmpty(DELETE_USER_UID_REQUIRED, ids);
        assertTrue(DELETE_USERS_NOT_FOUND, contains(ids));
        userDao.delete(ids);
    }
}