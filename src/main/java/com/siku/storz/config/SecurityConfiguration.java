package com.siku.storz.config;

import com.siku.storz.security.auth.RestAuthenticationEntryPoint;
import com.siku.storz.security.filter.JwtTokenFilterConfigurer;
import com.siku.storz.security.handler.SuccessfulAuthHandler;
import com.siku.storz.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true
)
@SuppressWarnings("unused")
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] IGNORED_ENDPOINTS = {"/lang"};
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/signup").permitAll()
                .anyRequest().authenticated();

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // If a user try to access a resource without having enough permissions
        http.exceptionHandling().authenticationEntryPoint(customAuthEntryPoint());

        // adding a jwt filter
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers(IGNORED_ENDPOINTS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationEntryPoint customAuthEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public AuthenticationSuccessHandler successfulAuthHandler() {
        log.info("Created successful auth handler");
        return new SuccessfulAuthHandler();
    }

    private void obtainAuthContext() {
        // obtaining the security context
        SecurityContext existingContext = SecurityContextHolder.getContext();
        final Authentication authentication = existingContext.getAuthentication();
        authentication.getDetails();
    }
}
