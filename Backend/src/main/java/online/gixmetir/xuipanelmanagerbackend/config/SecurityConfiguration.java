package online.gixmetir.xuipanelmanagerbackend.config;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import online.gixmetir.xuipanelmanagerbackend.models.Role;
import online.gixmetir.xuipanelmanagerbackend.security.filter.JwtAuthenticationFilter;
import online.gixmetir.xuipanelmanagerbackend.services.app.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter filter;
    private final AuthenticationService authenticationService;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(CorsConfiguration.ALL); // Allow all origins (you can specify specific origins)
        configuration.addAllowedMethod(CorsConfiguration.ALL); // Allow all HTTP methods
//        configuration.addAllowedMethod("POST"); // Allow all HTTP methods
        configuration.addAllowedHeader(CorsConfiguration.ALL); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@Nonnull CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(CorsConfiguration.ALL).allowedMethods(CorsConfiguration.ALL).allowedHeaders(CorsConfiguration.ALL);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/api/v1/users/change-password", "/api/v1/users/self-details").hasAnyAuthority(Role.Admin.name(), Role.Customer.name(), Role.SuperCustomer.name())
                                .requestMatchers("/api/v1/plans/get-all").hasAnyAuthority(Role.Admin.name(), Role.Customer.name(), Role.SuperCustomer.name())
                                .requestMatchers("/api/v1/subscriptions/change-pay-status-for-subscription").hasAnyAuthority(Role.Admin.name())
                                .requestMatchers("/api/v1/subscriptions/change-pay-status-for-subscription-renew-log").hasAnyAuthority(Role.Admin.name())
                                .requestMatchers("/api/v1/subscriptions/*").hasAnyAuthority(Role.Admin.name(), Role.Customer.name(), Role.SuperCustomer.name(), Role.Bot.name())
                                .requestMatchers("/api/v1/users/*").hasAnyAuthority(Role.Admin.name())
                                .requestMatchers("/api/v1/plans/*").hasAnyAuthority(Role.Admin.name())
                                .requestMatchers("/api/v1/inbounds/*").hasAnyAuthority(Role.Admin.name())
                                .requestMatchers("api/v1/servers/*").hasAnyAuthority(Role.Admin.name())
                                .requestMatchers("/api/v1/os-setting/*").hasAnyAuthority(Role.Admin.name())
                                .requestMatchers("/api/v1/global-setting/*").hasAnyAuthority(Role.Admin.name())
                                .requestMatchers("/api/v1/user-payment-logs/*").hasAnyAuthority(Role.Admin.name())
                                .requestMatchers("v3/api-docs/**", "swagger-ui/*", "api/v1/authentication/*", "/api/v1/subscriptions/client/**","/api/v1/subscriptions/frag/**").permitAll()
                                .anyRequest().authenticated()

                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authenticationService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
