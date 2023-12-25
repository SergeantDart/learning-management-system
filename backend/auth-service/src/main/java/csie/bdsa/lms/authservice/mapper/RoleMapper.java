package csie.bdsa.lms.authservice.mapper;

import csie.bdsa.lms.authservice.model.Role;
import csie.bdsa.lms.shared.dto.RoleDto;
import csie.bdsa.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends BaseMapper<Role, RoleDto, Long> {
}
