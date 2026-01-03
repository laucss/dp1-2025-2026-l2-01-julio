package es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration;

import static org.springframework.security.config.Customizer.withDefaults;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration.jwt.AuthEntryPointJwt;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration.jwt.AuthTokenFilter;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.configuration.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	DataSource dataSource;

	private static final String ADMIN = "ADMIN";
	private static final String PLAYER = "PLAYER";


	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http
            .cors(withDefaults())
            // Si usas H2, conviene ignorar su CSRF:
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))

            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos comunes (css, js, images, webjars…) públicos
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // H2 Console accesible
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // Raíz / páginas públicas
                .requestMatchers("/", "/oups").permitAll()

                // Swagger / OpenAPI accesible
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/swagger-resources/**"
                ).permitAll()

                // Permitir el handshake SockJS (info/xhr) sin autenticación
                .requestMatchers("/ws/**").permitAll()

                 .requestMatchers("/resources/images/**").permitAll()

                // API pública
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/developers").permitAll()
                .requestMatchers("/api/v1/plan").permitAll()

                // API restringida para usuarios autenticados
                .requestMatchers(HttpMethod.GET,"/api/v1/achievements").authenticated()
                .requestMatchers(HttpMethod.GET,"/api/v1/achievements/**").authenticated()
                .requestMatchers(HttpMethod.PUT,"/api/v1/users/**").authenticated()
                .requestMatchers(HttpMethod.GET,"/api/v1/users/**").authenticated()
                .requestMatchers(HttpMethod.GET,"/api/v1/rooms/**").authenticated()
                
                // API restringida para jugadores
                .requestMatchers(HttpMethod.POST, "/api/v1/matches/lobbies").hasAnyAuthority(PLAYER)
                .requestMatchers(HttpMethod.PUT, "/api/v1/matches/{id}/discardConfirmed").hasAnyAuthority(PLAYER)
                .requestMatchers(HttpMethod.GET,"/api/v1/matches/{matchId}/{playerId}/drawCardFromDeck").hasAnyAuthority(PLAYER, ADMIN)
                .requestMatchers(HttpMethod.PUT,"/api/v1/matches/{matchId}/move").hasAnyAuthority(PLAYER, ADMIN)

                .requestMatchers(HttpMethod.GET, "/api/v1/deck/**").hasAnyAuthority(PLAYER, ADMIN)


                // API restringida para administradores
                .requestMatchers(HttpMethod.POST,"/api/v1/achievements/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.PUT,"/api/v1/achievements/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.DELETE,"/api/v1/achievements/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.POST,"/api/v1/users/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.DELETE,"/api/v1/users/**").hasAuthority(ADMIN)

                // API restringida para jugadores o administradores
                .requestMatchers("/api/v1/matches/**").hasAnyAuthority(PLAYER, ADMIN)
                .requestMatchers(HttpMethod.POST,"/api/v1/bag/validate").permitAll()
                
                .requestMatchers("/api/v1/friendRequests/**").authenticated()

                
                

                .requestMatchers("/api/v1/players/**").authenticated()

                .requestMatchers("/api/v1/statistics/**").authenticated()

                .requestMatchers("/api/v1/players/**").authenticated()

                .requestMatchers("/api/v1/statistics/**").authenticated()

                .requestMatchers("/api/v1/match/{matchId}/chat/**").hasAnyAuthority(PLAYER, ADMIN)
                
                // El resto denegado
                .anyRequest().denyAll()
            )


			.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Permite enviar JWT en headers/cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
