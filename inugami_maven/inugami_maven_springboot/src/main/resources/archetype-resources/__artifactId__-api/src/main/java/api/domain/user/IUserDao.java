package ${package}.api.domain.user;

import io.inugami.framework.interfaces.models.crud.CrudDao;
import ${package}.api.domain.user.dto.UserDTO;
import ${package}.api.domain.user.dto.UserDTOSearchRequestDTO;

public interface IUserDao extends CrudDao<UserDTO,String, UserDTOSearchRequestDTO> {

}