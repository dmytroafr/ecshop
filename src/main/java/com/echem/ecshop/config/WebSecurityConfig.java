package com.echem.ecshop.config;

import com.echem.ecshop.domain.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity()
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> {
                            requests
                                    .requestMatchers("/users").hasAnyAuthority(Role.ADMIN.name(), Role.MANAGER.name())
                                    .anyRequest().permitAll();
                        }
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .failureUrl("/login-error")
                        .loginProcessingUrl("/auth")
                        .permitAll()
                )
                .logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/").deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                )
                .csrf((csrf) -> csrf.disable());
        return http.build();
    }
}
