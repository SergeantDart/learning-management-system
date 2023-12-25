package csie.bdsa.lms.authservice.service;

import csie.bdsa.lms.authservice.mapper.RoleMapper;
import csie.bdsa.lms.authservice.model.Role;
import csie.bdsa.lms.authservice.repository.RoleRepository;
import csie.bdsa.lms.shared.dto.RoleDto;
import csie.bdsa.lms.shared.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, RoleDto, Long> {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        super(roleRepository, roleMapper);
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }
}
