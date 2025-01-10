package com.example.studentmanagement;


import com.example.studentmanagement.service.UserService;
import com.example.studentmanagement.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CustomAuthenticationHandler successHandler;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**", "/login", "/index", "/css/**", "/js/**").permitAll()
                                .requestMatchers("/courses/new", "/upload", "/courses/edit/**", "/courses/{id}", "/delete-pdf/{id}").hasRole("ADMIN")
                                .requestMatchers("/students/new", "/students/edit/**", "/students/{id}").hasRole("ADMIN")
                                .requestMatchers("/teachers/new", "/teachers/edit/**", "/teachers/{id}").hasRole("ADMIN")
                                .requestMatchers("/assignments/new", "/assignments/{id}/edit", "/assignments/{id}/delete", "/assignments/{id}/delete-file").hasRole("ADMIN")
                                .requestMatchers("/download-file/**").authenticated()
                                .anyRequest().authenticated()

                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/", true)
                                .usernameParameter("email")
                                .successHandler(successHandler)
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                )
                .exceptionHandling()
                    .accessDeniedPage("/403");
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
