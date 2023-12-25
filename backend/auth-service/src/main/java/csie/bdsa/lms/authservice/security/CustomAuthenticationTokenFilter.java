package csie.bdsa.lms.authservice.security;

import csie.bdsa.lms.shared.security.AuthenticationTokenFilter;
import csie.bdsa.lms.shared.security.TokenUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationTokenFilter extends AuthenticationTokenFilter {

    private final UserDetailsService userDetailsService;

    public CustomAuthenticationTokenFilter(TokenUtils tokenUtils, UserDetailsService userDetailsService) {
        super(null, tokenUtils);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected UserDetails getUserDetails(String username) {
        return userDetailsService.loadUserByUsername(username);
    }
}
