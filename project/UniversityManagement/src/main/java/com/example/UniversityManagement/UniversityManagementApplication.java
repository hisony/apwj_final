package com.example.UniversityManagement;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootApplication
@EnableScheduling
public class UniversityManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversityManagementApplication.class, args);

	}

	// JWT Filter
	class JwtAuthenticationFilter extends OncePerRequestFilter {
		@Autowired
		private JwtUtil jwtUtil;
		@Autowired
		private UserDetailsService userDetailsService;

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
			final String authHeader = request.getHeader("Authorization");
			String username = null;
			String jwt = null;
			
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				jwt = authHeader.substring(7);
				
				// Check if token is blacklisted
				if (jwtUtil.isTokenBlacklisted(jwt)) {
					filterChain.doFilter(request, response);
					return;
				}
				
				try {
					username = jwtUtil.extractUsername(jwt);
				} catch (Exception ignored) {
					// JWT is invalid, continue without authentication
				}
			}
			
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				try {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				} catch (Exception e) {
					// User not found or other error, continue without authentication
					// Log the error for debugging
					System.err.println("Error loading user details for username: " + username + " - " + e.getMessage());
				}
			}
			
			filterChain.doFilter(request, response);
		}
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						// ALLOWED
						.requestMatchers(HttpMethod.POST, "/api/login").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/students/*").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/teachers/*").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/courses/*").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/enrollments/*").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/users/logout").permitAll()
						// SECURED
						.requestMatchers(HttpMethod.POST, "/api/**").authenticated()
						.requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
						.requestMatchers(HttpMethod.DELETE, "/api/**").authenticated()
						.anyRequest().permitAll()
				)
				.addFilterBefore(jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
				.httpBasic(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
		
		// Configure custom SQL queries for the 'user' table
		userDetailsManager.setUsersByUsernameQuery(
			"SELECT username, password, enabled FROM user WHERE username = ?"
		);
		userDetailsManager.setAuthoritiesByUsernameQuery(
			"SELECT username, authority FROM authority WHERE username = ?"
		);
		
		System.out.println("JdbcUserDetailsManager configured with custom queries:");
		System.out.println("Users query: " + userDetailsManager.getUsersByUsernameQuery());
//		System.out.println("Authorities query: " + userDetailsManager.getAuthoritiesByUsernameQuery());
		
		return userDetailsManager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		System.out.println("Password 'admin123': "+new BCryptPasswordEncoder().encode("admin123"));
		System.out.println("Password '12345': "+new BCryptPasswordEncoder().encode("12345"));
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private JwtUtil jwtUtil;

	@Scheduled(fixedRate = 3600000) // Run every hour (3600000 ms = 1 hour)
	public void cleanBlacklist() {
		jwtUtil.cleanupExpiredTokens();
	}

}
