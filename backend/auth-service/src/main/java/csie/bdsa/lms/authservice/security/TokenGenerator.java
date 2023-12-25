package csie.bdsa.lms.authservice.security;

import csie.bdsa.lms.authservice.client.FacultyFeignClient;
import csie.bdsa.lms.authservice.model.User;
import csie.bdsa.lms.authservice.repository.UserRepository;
import csie.bdsa.lms.shared.exception.BadRequestException;
import csie.bdsa.lms.shared.exception.NotFoundException;
import csie.bdsa.lms.shared.security.TokenUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_ADMIN;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_STUDENT;
import static csie.bdsa.lms.shared.security.SecurityUtils.ROLE_TEACHER;

@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final UserRepository userRepository;
    private final FacultyFeignClient facultyFeignClient;
    private final TokenUtils tokenUtils;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.accessExpiration}")
    private Long accessExpiration;

    @Value("${token.refreshExpiration}")
    private Long refreshExpiration;

    public String refreshAccessToken(String refreshToken) {
        String username = tokenUtils.getUsername(refreshToken);
        if (username == null) {
           throw new AuthenticationException("Refresh token is not valid") {};
        }
        return generateAccessToken(username);
    }

    public String generateAccessToken(String username) {
        Map<String, Object> claims = generateClaims(username);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateRefreshToken(String username) {
        validateUser(username);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Map<String, Object> generateClaims(String username) {
        Map<String, Object> claims = new HashMap<>();
        User user = validateUser(username);
        Long userId = user.getId();

        claims.put("userId", userId);

        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (authorities.contains(ROLE_ADMIN)) {
            claims.put("adminId", facultyFeignClient.getAdministratorByUserId(userId));
        } else if (authorities.contains(ROLE_TEACHER)) {
            claims.put("teacherId", facultyFeignClient.getTeacherByUserId(userId));
        } else if (authorities.contains(ROLE_STUDENT)) {
            claims.put("studentId", facultyFeignClient.getStudentIdByUserId(userId));
        }
        return claims;
    }

    private User validateUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        if (user.isDeleted()) {
            throw new BadRequestException("User is deleted");
        }
        return user;
    }
}
