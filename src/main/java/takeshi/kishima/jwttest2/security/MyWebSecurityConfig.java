package takeshi.kishima.jwttest2.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import takeshi.kishima.jwttest2.user.ApplicationUserRepository;

@Configuration
@RequiredArgsConstructor
public class MyWebSecurityConfig {
    
    private final ApplicationUserRepository applicationUserRepository;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        final AuthenticationProvider provider = new ApplicationUserAuthenticationProvider(passwordEncoder, applicationUserRepository);
        final AuthenticationManager manager = new ProviderManager(Arrays.asList(provider));
        
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.POST, "/users/sign-up").permitAll() // ログインなくてもOK
                .anyRequest().authenticated() // その他はログイン後
            )
            .addFilter(new JWTAuthenticationFilter(manager)) // 使用するフィルタを登録する
            .addFilterAfter(new JWTAuthorizationFilter(), JWTAuthenticationFilter.class); // 認可フィルタの利用設定
        ;
        return http.build();
    }
}
