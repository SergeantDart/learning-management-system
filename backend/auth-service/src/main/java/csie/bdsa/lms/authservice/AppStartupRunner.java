package csie.bdsa.lms.authservice;

import csie.bdsa.lms.authservice.service.RoleService;
import csie.bdsa.lms.authservice.service.UserService;
import csie.bdsa.lms.shared.dto.RoleDto;
import csie.bdsa.lms.shared.dto.UserDetailsDto;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_ADMIN;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_ADMIN_ID;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_ROOT;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_ROOT_ID;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_STUDENT;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_STUDENT_ID;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_TEACHER;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_TEACHER_ID;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROOT_USER_ID;

@Component
@RequiredArgsConstructor
public class AppStartupRunner implements ApplicationRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        RoleDto root = RoleDto.builder().id(ROLE_ROOT_ID).authority(ROLE_ROOT).build();
        RoleDto admin = RoleDto.builder().id(ROLE_ADMIN_ID).authority(ROLE_ADMIN).build();
        RoleDto teacher = RoleDto.builder().id(ROLE_TEACHER_ID).authority(ROLE_TEACHER).build();
        RoleDto student = RoleDto.builder().id(ROLE_STUDENT_ID).authority(ROLE_STUDENT).build();

        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .id(ROOT_USER_ID)
                .username("admin@csie.ase.ro")
                .password(passwordEncoder.encode("password"))
                .authorities(Set.of(root, admin))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        if (roleService.findAll().isEmpty()) {
            roleService.forceSave(root);
            roleService.forceSave(admin);
            roleService.forceSave(teacher);
            roleService.forceSave(student);
        }

        if (userService.findAll().isEmpty()) {
           userService.forceSave(userDetailsDto);
        }

    }
}
