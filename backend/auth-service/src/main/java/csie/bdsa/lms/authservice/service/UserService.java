package csie.bdsa.lms.authservice.service;

import csie.bdsa.lms.authservice.dto.TokenDto;
import csie.bdsa.lms.authservice.mapper.UserMapper;
import csie.bdsa.lms.authservice.model.User;
import csie.bdsa.lms.authservice.repository.UserRepository;
import csie.bdsa.lms.authservice.security.TokenGenerator;
import csie.bdsa.lms.shared.dto.UserDetailsDto;
import csie.bdsa.lms.shared.dto.UserDto;
import csie.bdsa.lms.shared.exception.ForbiddenException;
import csie.bdsa.lms.shared.exception.NotFoundException;
import csie.bdsa.lms.shared.security.SecurityUtils;
import csie.bdsa.lms.shared.service.BaseService;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static csie.bdsa.lms.shared.security.SecurityUtils.BEARER_PREFIX;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_ADMIN;

@Service
public class UserService extends BaseService<User, UserDetailsDto, Long> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserDetailsService userDetailsService;
    private final TokenGenerator tokenGenerator;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, UserDetailsService userDetailsService,
                       TokenGenerator tokenGenerator, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userDetailsService = userDetailsService;
        this.tokenGenerator = tokenGenerator;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetailsDto save(UserDetailsDto userDetailsDto) {
        userDetailsDto.setPassword(passwordEncoder.encode(userDetailsDto.getPassword()));
        userDetailsDto.setAccountNonExpired(true);
        userDetailsDto.setAccountNonLocked(true);
        userDetailsDto.setCredentialsNonExpired(true);
        userDetailsDto.setEnabled(true);
        return super.save(userDetailsDto);
    }

    @Transactional
    public UserDetailsDto update(UserDetailsDto userDetailsDto) {
        User existingUser = userRepository.findById(userDetailsDto.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (userDetailsDto.getUsername() != null) {
            existingUser.setUsername(userDetailsDto.getUsername());
        }
        if (userDetailsDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDetailsDto.getPassword()));
        }
        return this.mapper.toDto(this.repository.save(existingUser));
    }

    public List<UserDto> findByIdPublic(Set<Long> ids) {
        List<User> users = (List<User>) this.userRepository.findAllById(ids);
        if (users.isEmpty()) {
            throw new NotFoundException("User id not found");
        }
        return this.userMapper.userToUserDtoList(users);
    }
    public UserDetailsDto findByUsername(String username) {
        if (!SecurityUtils.getUsername().equals(username) && !SecurityUtils.hasAuthority(ROLE_ADMIN)) {
            throw new ForbiddenException("You are not allowed to view this user's details");
        }
        return (UserDetailsDto) userDetailsService.loadUserByUsername(username);
    }

    public Long findIdByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"))
                .getId();
    }

    public TokenDto login(UserDto userDto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String username = userDto.getUsername();
        return new TokenDto(tokenGenerator.generateAccessToken(username), tokenGenerator.generateRefreshToken(username));
    }

    public TokenDto refresh(String refreshToken) {
        refreshToken = refreshToken.substring(BEARER_PREFIX.length());
        return new TokenDto(tokenGenerator.refreshAccessToken(refreshToken), refreshToken);
    }
}
