package csie.bdsa.lms.authservice.controller;

import csie.bdsa.lms.authservice.model.User;
import csie.bdsa.lms.authservice.service.UserService;
import csie.bdsa.lms.shared.controller.BaseController;
import csie.bdsa.lms.shared.dto.UserDetailsDto;
import csie.bdsa.lms.shared.dto.UserDto;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User, UserDetailsDto, Long> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @GetMapping("/{id}/public")
    public ResponseEntity<List<UserDto>> getPublic(@PathVariable Set<Long> ids) {
        return new ResponseEntity<>(userService.findByIdPublic(ids), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDetails> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/username/{username}/id")
    public ResponseEntity<Long> getUserIdByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.findIdByUsername(username), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDetailsDto> patch(@PathVariable Long id, @RequestBody UserDetailsDto userDetailsDto) {
        userDetailsDto.setId(id);
        return new ResponseEntity<>(this.userService.update(userDetailsDto), HttpStatus.OK);
    }
}
