package com.trialTask.deliveryapp.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration to give POST and GET request permissions to users
 */
@Configuration
@EnableWebSecurity
public class SecurityFilter {

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST).hasAnyRole(new String[]{"USER","ADMIN"})
                .requestMatchers(HttpMethod.GET).hasAnyRole(new String[]{"USER","ADMIN"})
		).formLogin(withDefaults());
        return http.build();
    }
}
