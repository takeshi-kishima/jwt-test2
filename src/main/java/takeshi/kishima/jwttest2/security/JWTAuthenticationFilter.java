package takeshi.kishima.jwttest2.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String SECRET = "SecretKeyToGenJWTs";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    private final ObjectMapper objectMpper = new ObjectMapper();

    public JWTAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super();
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest req,
        final HttpServletResponse res) throws AuthenticationException {
        try {
            final LoginForm form = objectMpper.readValue(req.getInputStream(), LoginForm.class);

            final UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(
                form.getUsername(),
                form.getPassword());

            return getAuthenticationManager().authenticate(creds); //★親クラスがやっているのは /login に対する POST
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest req,
        final HttpServletResponse res,
        final FilterChain chain,
        final Authentication auth) throws IOException, ServletException {

        final String token = JWT.create()
            .withSubject(auth
                .getName())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
