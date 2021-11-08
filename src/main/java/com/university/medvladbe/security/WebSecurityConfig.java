package com.university.medvladbe.security;

import com.university.medvladbe.repository.UserRepository;
import com.university.medvladbe.security.Filter.CustomAuthenticationFilter;
import com.university.medvladbe.security.Filter.CustomAuthorizationFilter;
import com.university.medvladbe.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(),userRepository);
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/token/refresh/**").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/forgotPassword").permitAll()
                .antMatchers("/verifyToken").permitAll()
                .antMatchers("/resetPassword").permitAll()
//                .antMatchers("/user/**").hasRole("USER")
//                .antMatchers("/doctor/**").hasRole("DOCTOR")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/403");
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);


    }


}
