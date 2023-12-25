package csie.bdsa.lms.authservice.controller;

import csie.bdsa.lms.authservice.model.Role;
import csie.bdsa.lms.authservice.service.RoleService;
import csie.bdsa.lms.shared.controller.BaseController;
import csie.bdsa.lms.shared.dto.RoleDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController extends BaseController<Role, RoleDto, Long> {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        super(roleService);
        this.roleService = roleService;
    }
}
