package com.mehdilagdimi.myrh.security;

import com.mehdilagdimi.myrh.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig {
    private final UserService userService;
    private final JwtAuthFilter jwtAuthFilter;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(@Lazy UserService userService, @Lazy JwtAuthFilter jwtAuthFilter, @Lazy PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtAuthFilter = jwtAuthFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(
                                        "/*/oauth/*",
                                                "/*/login/*",
                                                "/*/signup",
                                                "/*/auth",
                                                "/*/offers",
                                                "/*/payment/*",
                                                "/*/offers/fields-options-list"
                                                )
                                 .permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
       final DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://myhr.herokuapp.com","http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("POST","GET", "PUT", "HEAD"));
//        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//        configuration.setAllowedHeaders(List.of(
//                "Authorization", "Content-Type", "Origin", "X-Requested-With", "Accept", "Key", "credential", "X-XSRF-TOKEN"
//                )
//        );
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


}
