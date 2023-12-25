package csie.bdsa.lms.authservice.controller;

import csie.bdsa.lms.authservice.dto.TokenDto;
import csie.bdsa.lms.authservice.service.UserService;
import csie.bdsa.lms.shared.dto.UserDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.login(userDto), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        return new ResponseEntity<>(userService.refresh(refreshToken), HttpStatus.OK);
    }
}
