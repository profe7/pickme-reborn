package me.pick.metrodata.configs;

import me.pick.metrodata.services.accountdetail.AccountDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.AllArgsConstructor;

import static org.springframework.security.config.Customizer.withDefaults;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig {

    private final AccountDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> {
                    try {
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .and()
                                .authorizeHttpRequests(requests -> requests
                                        .requestMatchers(new AntPathRequestMatcher("/mitra/**")).authenticated()
                                        .requestMatchers(new AntPathRequestMatcher("/vacancies/**")).authenticated()
                                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).authenticated()
                                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/landing-page")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/dist/**")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/fonts/**")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/img/**")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/client/**")).authenticated()
                                        .requestMatchers(new AntPathRequestMatcher("/api/v1/**")).authenticated());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .failureUrl("/login?error=true"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // List<SimpleGrantedAuthority> authorities = Stream.of(
        // "CREATE_HOLIDAY", "READ_HOLIDAY", "UPDATE_HOLIDAY",
        // "DELETE_HOLIDAY").map(SimpleGrantedAuthority::new).toList();
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }
}
