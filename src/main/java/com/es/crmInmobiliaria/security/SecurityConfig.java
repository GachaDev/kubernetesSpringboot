package com.es.crmInmobiliaria.security;

import com.es.crmInmobiliaria.error.exception.DataBaseException;
import com.es.crmInmobiliaria.error.exception.NotFoundException;
import com.es.crmInmobiliaria.model.Propiedad;
import com.es.crmInmobiliaria.model.Propietario;
import com.es.crmInmobiliaria.model.Usuario;
import com.es.crmInmobiliaria.repository.PropiedadRepository;
import com.es.crmInmobiliaria.repository.PropietarioRepository;
import com.es.crmInmobiliaria.repository.UsuarioRepository;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private RsaKeyProperties rsaKeys;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    private AuthorizationManager<RequestAuthorizationContext> getUsuarioByIdManager() {
        return (authentication, object) -> {
            Authentication auth = authentication.get();

            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return new AuthorizationDecision(true);
            }

            String path = object.getRequest().getRequestURI();
            String id = path.replaceAll("/usuarios/", "");
            long idS = 0L;

            try {
                idS = Long.parseLong(id);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("La id debe de ser un número correcto");
            }

            Usuario usuario = null;

            try {
                usuario = usuarioRepository.findById(idS).orElse(null);
            } catch (Exception e) {
                throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
            }

            if (usuario == null) {
                return new AuthorizationDecision(false);
            }

            if (usuario.getUsername().equals(auth.getName())) {
                return new AuthorizationDecision(true);
            }

            return new AuthorizationDecision(false);
        };
    }

    private AuthorizationManager<RequestAuthorizationContext> getUsuarioByUsernameManager() {
        return (authentication, object) -> {
            Authentication auth = authentication.get();

            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return new AuthorizationDecision(true);
            }

            String path = object.getRequest().getRequestURI();
            String id = path.replaceAll("/usuarios/", "");

            Usuario usuario = null;

            try {
                usuario = usuarioRepository.findByUsername(id).orElse(null);
            } catch (Exception e) {
                throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
            }

            if (usuario == null) {
                return new AuthorizationDecision(false);
            }

            if (usuario.getUsername().equals(auth.getName())) {
                return new AuthorizationDecision(true);
            }

            return new AuthorizationDecision(false);
        };
    }

    private AuthorizationManager<RequestAuthorizationContext> getAccesByIdPropiedad() {
        return (authentication, object) -> {
            Authentication auth = authentication.get();

            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return new AuthorizationDecision(true);
            }

            String path = object.getRequest().getRequestURI();
            String id = path.replaceAll("/propiedades/", "");

            Propiedad propiedad = null;

            long idS = 0L;

            try {
                idS = Long.parseLong(id);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("La id debe de ser un número correcto");
            }

            try {
                propiedad = propiedadRepository.findById(idS).orElse(null);
            } catch (Exception e) {
                throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
            }

            if (propiedad == null) {
                return new AuthorizationDecision(false);
            }

            //Aqui se obtiene el id del propietario para luego saber si el usuario que está autenticado es el propietario
            // La id del usuario que esta asociada a la propiedad es la id del vendedor asociada a la propiedad
            long id_propietario = propiedad.getPropietario().getId();
            Propietario propietario = propietarioRepository.findById(id_propietario).orElseThrow(() -> new NotFoundException("No se ha encontrado al propietario"));

            if (propietario.getUsuario().getUsername().equals(auth.getName())) {
                return new AuthorizationDecision(true);
            }

            return new AuthorizationDecision(false);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/usuarios/login", "/usuarios/register", "/swagger-ui/**", "/v3/api-docs/**", "/").permitAll()
                        .requestMatchers("/usuarios/").hasRole("ADMIN")
                        .requestMatchers("/usuarios/internal/{username}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/usuarios/{username}").access(getUsuarioByUsernameManager())
                        .requestMatchers(HttpMethod.DELETE,"/usuarios/{username}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{id}").access(getUsuarioByIdManager())
                        .requestMatchers("/propiedades/").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/propiedades/{id}").access(getAccesByIdPropiedad())
                        .requestMatchers(HttpMethod.PUT, "/propiedades/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/propiedades/{id}").hasRole("ADMIN")
                        .requestMatchers("/propietarios/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }
}