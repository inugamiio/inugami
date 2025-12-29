package ${package}.infrastructure.database.mapper;

import ${package}.api.domain.user.dto.UserDTO;
import ${package}.infrastructure.database.entity.UserEntity;
import ${package}.infrastructure.database.entity.UserProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @since 2025-12-28
 */
@Mapper
public interface UserEntityMapper {

    UserEntity convertToEntity(final UserDTO value);

    default Collection<UserEntity> convertToEntity(final Collection<UserDTO> values) {
        return Optional.ofNullable(values)
                       .orElse(List.of())
                       .stream().map(this::convertToEntity)
                       .toList();
    }

    @Mapping(target = "audit", expression = "java(AuditEntityMapperUtils.convertToAuditDTO(value))")
    UserDTO convertProjectionToDto(final UserProjection value);

    @Mapping(target = "audit", expression = "java(AuditEntityMapperUtils.convertToAuditDTO(value))")
    UserDTO convertToDto(final UserEntity value);

    default Collection<UserDTO> convertToDto(final Collection<UserEntity> values) {
        return Optional.ofNullable(values)
                       .orElse(List.of())
                       .stream().map(this::convertToDto)
                       .toList();
    }


}
