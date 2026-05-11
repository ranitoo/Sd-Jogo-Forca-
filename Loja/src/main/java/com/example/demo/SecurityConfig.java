
package com.example.demo;

import javax.sql.DataSource;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;

@Configuration
public class SecurityConfig {
	
	/*
	@Autowired
    private DataSource dataSource;*/
     
    @Bean
    public UserDetailsService userDetailsService() {
        return new ClienteUserDetailsService();
    }
    
    @Autowired
    private ClienteUserDetailsService cUDS;
    
    @Autowired
    private LoginSucessoHandler loginSucessoHandler;

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/registar", "/processar_registo", "/login", "/logout", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }*/
    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
         
    	http.authenticationProvider(authenticationProvider());
    	http
    		 .csrf().disable()
         	.authorizeHttpRequests(auth -> auth
         	.requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/users").authenticated()
            .requestMatchers("/carrinho/**").authenticated()
            .anyRequest().permitAll()
            )
            .formLogin(login ->
                login.usernameParameter("email")
                //.defaultSuccessUrl("/inicio")
                .successHandler(loginSucessoHandler)
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/").permitAll()
        );
         
        return http.build();
    }  

    @PostConstruct
    public void init() {
        System.out.println("SecurityConfig carregada com sucesso!");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
    
    /*
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }*/

}


