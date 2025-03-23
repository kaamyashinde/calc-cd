package ntnu.edu.stud.calculator.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
            // Deaktiver CSRF (ikke nødvendig for API-er)
            .csrf(csrf -> csrf.disable())
            
            // Konfigurer CORS (tillat frontend med offentlig IP)
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.asList(
                    "http://localhost:5173",  // Lokalt utviklingsmiljø
                    "http://localhost:5170",  
                    "http://128.251.48.227:8081"  // Offentlig frontend-IP
                ));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(Arrays.asList("*"));
                config.setAllowCredentials(true); // Tillat autentiseringstokens og cookies
                return config;
            }))
            
            // Sett session-håndtering til stateless (siden vi bruker JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Definer tilgangsregler
            .authorizeHttpRequests(auth -> auth
                // Tillat tilgang til Swagger UI og API-dokumentasjon
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "/swagger-ui.html", "/").permitAll()
                
                // Åpne endpoints for autentisering
                .requestMatchers("/auth/**").permitAll()
                
                // Tillat alle andre forespørsler (kan strammes inn senere)
                .anyRequest().permitAll()
            );

        // Legg til vår tilpassede JWT-filter før Spring Securitys standardfilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
