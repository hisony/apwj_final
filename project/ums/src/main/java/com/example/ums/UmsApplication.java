package com.example.ums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@SpringBootApplication
public class UmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UmsApplication.class, args);
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(
//                auth -> auth
//                        //Public
//                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/wishlists/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/carts/**").permitAll()
//
//                        //Temp
//                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
//
//                        //Secured
//                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
//                        .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
//                        .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
//
//                        //Else
//                        .anyRequest().authenticated()
//                        )
//                .httpBasic(Customizer.withDefaults());
//        return http.build();
//    }
    /*

    /*@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.builder().username("user").password(passwordEncoder().encode("123456")).roles("USER").build();
        UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("123456")).roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }*/

//   @Bean
//    public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        System.out.println(new BCryptPasswordEncoder().encode("12345"));
//        return new BCryptPasswordEncoder();
//    }






}
