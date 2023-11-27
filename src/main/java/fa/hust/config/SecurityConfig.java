package fa.hust.config;


import fa.hust.controllerAd.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").authenticated()
                        .anyRequest().permitAll())
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/signIn")
                        .successForwardUrl("/")
                        .failureHandler(authenticationFailureHandler())
//                        .failureForwardUrl("/login")
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
                .exceptionHandling(err -> err.accessDeniedPage("/index"))
        ;
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }
}
