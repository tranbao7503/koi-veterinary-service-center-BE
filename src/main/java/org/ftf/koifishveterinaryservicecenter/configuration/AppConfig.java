package org.ftf.koifishveterinaryservicecenter.configuration;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class AppConfig {

    private final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/users/token", "/api/v1/users/introspect", "api/v1/users/customers", "api/v1/users/signup"
    };

    @Value("${jwt.signer}")
    private String SIGNER_KEY;
    @Bean
    /*
    * ModelMapper use for mapping Entity to DTO
    * */
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean  // xoa prefix
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity


                .csrf(csrfConfig -> csrfConfig.ignoringRequestMatchers("/api/v1/users/token", "/api/v1/users/introspect", "api/v1/users/customers", "api/v1/users/signup"))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())

                .authorizeHttpRequests(request -> request
                // Cho phép truy cập công khai đối với các endpoint được chỉ định
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                // Chỉ cho phép các vai trò "STA", "VET", "MAN" truy cập /api/v1/users/customers
                        .requestMatchers("/api/v1/users/customers").hasAnyAuthority("MAN")
                // Các quyền khác cho /api/v1/users
                .requestMatchers("/api/v1/users").hasAnyAuthority("MAN", "STA", "VET")
                // Các yêu cầu còn lại phải được xác thực
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter())
                ));

        return httpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] secretKeyBytes = SIGNER_KEY.getBytes();
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA512");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }
}
