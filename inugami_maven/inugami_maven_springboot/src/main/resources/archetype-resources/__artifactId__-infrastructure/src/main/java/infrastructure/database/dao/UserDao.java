package ${package}.infrastructure.database.dao;

import ${package}.api.domain.user.IUserDao;
import ${package}.api.domain.user.dto.UserDTO;
import ${package}.api.domain.user.dto.UserDTOSearchRequestDTO;
import ${package}.infrastructure.database.entity.QUserEntity;
import ${package}.infrastructure.database.entity.UserEntity;
import ${package}.infrastructure.database.listener.AuditingEntityListener;
import ${package}.infrastructure.database.mapper.UserEntityMapper;
import ${package}.infrastructure.database.repository.UserEntityRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.inugami.framework.commons.spring.data.utils.SearchResponseUtils;
import io.inugami.framework.interfaces.models.search.QueryFilterDTO;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import io.inugami.framework.interfaces.tools.ListUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static io.inugami.framework.commons.spring.data.utils.PredicateUtils.*;
import static io.inugami.framework.interfaces.database.NodeUtils.processIfNotEmpty;

@RequiredArgsConstructor
@Builder
@Service
public class UserDao implements IUserDao {
    //==================================================================================================================
    // ATTRIBUTES
    //==================================================================================================================
    private final UserEntityRepository userEntityRepository;
    private final UserEntityMapper     userEntityMapper;

    //==================================================================================================================
    // CREATE
    //==================================================================================================================
    @Transactional
    @Override
    public Collection<UserDTO> create(final Collection<UserDTO> values) {
        final var result = userEntityRepository.saveAll(userEntityMapper.convertToEntity(values));
        return userEntityMapper.convertToDto(result);
    }

    //==================================================================================================================
    // READ
    //==================================================================================================================
    @Override
    public SearchResponse<UserDTO> search(final UserDTOSearchRequestDTO searchRequest,
                                          final Collection<QueryFilterDTO<?>> requestFilters) {
        final PageRequest page      = SearchResponseUtils.buildPageRequest(searchRequest, "email");
        final Predicate   predicate = buildPredicate(searchRequest);
        final Page<UserEntity> resultSet = predicate == null ? userEntityRepository.findAll(page)
                                                             : userEntityRepository.findAll(buildPredicate(searchRequest), page);

        return SearchResponseUtils.buildSearchResponse(resultSet,
                                                       userEntityMapper::convertToDto);
    }

    private Predicate buildPredicate(final UserDTOSearchRequestDTO request) {
        final List<BooleanExpression> predicates = new ArrayList<>();

        produceStringContains(request.getFirstName(), QUserEntity.userEntity.firstName, predicates::add);
        produceStringContains(request.getLastName(), QUserEntity.userEntity.lastName, predicates::add);
        produceStringIn(request.getEmail(), QUserEntity.userEntity.email, predicates::add);
        return mergePredicateAnd(predicates);
    }

    @Override
    public UserDTO getById(final String uid, final Boolean full) {
        return Optional.ofNullable(full).orElse(false).booleanValue()
               ? userEntityRepository.findById(uid).map(userEntityMapper::convertToDto).orElse(null)
               : userEntityRepository.findByUidProjected(uid)
                                     .map(userEntityMapper::convertProjectionToDto)
                                     .orElse(null);
    }

    @Override
    public boolean contains(final Collection<String> uids) {
        return !ListUtils.isEmpty(uids) && userEntityRepository.findUids(uids).size() == uids.size();
    }

    @Override
    public Collection<UserDTO> getByIds(final Collection<String> ids) {
        return userEntityRepository.findByUidsProjected(ids)
                                   .stream()
                                   .map(userEntityMapper::convertProjectionToDto)
                                   .toList();
    }

    //==================================================================================================================
    // UPDATE
    //==================================================================================================================
    @Transactional
    @Override
    public Collection<UserDTO> update(final Collection<UserDTO> values) {
        if (ListUtils.isEmpty(values)) {
            return List.of();
        }
        final var updateAudit = AuditingEntityListener.resolveUpdateAudit();
        values.forEach(value -> userEntityRepository.update(value.getUid(),
                                                            value.getFirstName(),
                                                            value.getLastName(),
                                                            value.getEmail(),
                                                            updateAudit.getLastModifiedBy(),
                                                            updateAudit.getLastModifiedDate()));

        return values;
    }


    //==================================================================================================================
    // DELETE
    //==================================================================================================================
    @Transactional
    @Override
    public void delete(final Collection<String> ids) {
        processIfNotEmpty(ids, userEntityRepository::deleteAllById);
    }
}