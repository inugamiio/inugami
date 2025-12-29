package ${package}.api.domain.user;

import io.inugami.framework.interfaces.models.crud.Crud;
import ${package}.api.domain.user.dto.UserDTO;
import ${package}.api.domain.user.dto.UserDTOSearchRequestDTO;

public interface IUserService extends Crud<UserDTO,String, UserDTOSearchRequestDTO> {

}