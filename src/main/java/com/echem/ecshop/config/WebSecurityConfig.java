package com.echem.ecshop.config;

import com.echem.ecshop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity()
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    private final UserService userService;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/orders/*").authenticated()
                        .requestMatchers("/buckets/*").authenticated()
                        .requestMatchers("/users/*").authenticated()
                        .anyRequest().permitAll()
                        )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .failureUrl("/login-error")
                        .loginProcessingUrl("/auth")
                        .defaultSuccessUrl("/",true)
                )
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults())
                .logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/").deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                )
                .csrf(AbstractHttpConfigurer::disable)
        .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider (){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }
//    @Bean
//    public AuthenticationManager authenticationManager(){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(bCryptPasswordEncoder());
//        provider.setUserDetailsService(userService);
//        return new ProviderManager(provider);
//    }
    @Autowired
    public void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder.eraseCredentials(true);
    }


    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
        }
}
