package com.example.UniversityManagement;

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
public class UniversityManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversityManagementApplication.class, args);

	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth

								// ALLOWED
//                 .requestMatchers(HttpMethod.GET, "/api/students/*").permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/teachers/*").permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/courses/*").permitAll()
//                 .requestMatchers(HttpMethod.GET, "/api/enrollments/*").permitAll()

								// TEMP
								.requestMatchers(HttpMethod.GET, "/api/**").permitAll()


								// SECURED
								.requestMatchers(HttpMethod.POST, "/api/**").permitAll()
								.requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
								.requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()

						// OTHER
//                 .anyRequest().authenticated()
				)
				.httpBasic(Customizer.withDefaults());
		return http.build();
	}

	//  @Bean
//  public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
//     return new JdbcUserDetailsManager(dataSource);
//  }
	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		UserDetails user = User.withUsername("admin")
				.password(passwordEncoder().encode("1234"))
				.roles("ADMIN")
				.build();
		return new InMemoryUserDetailsManager(user);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}



}
