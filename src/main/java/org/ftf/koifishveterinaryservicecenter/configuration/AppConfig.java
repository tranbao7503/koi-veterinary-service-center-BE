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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class AppConfig {

    private final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/users/token", "/api/v1/users/introspect", "api/v1/users/customers"
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

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfig -> csrfConfig.ignoringRequestMatchers("/api/v1/users/token", "/api/v1/users/introspect", "api/v1/users/customers"))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                // Cho phép truy cập công khai đối với các endpoint được chỉ định
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                // Chỉ cho phép các vai trò "STA", "VET", "MAN" truy cập /api/v1/users/customers
                .requestMatchers("/api/v1/users/customers").hasAnyAuthority("STA", "VET", "MAN")
                // Các quyền khác cho /api/v1/users
                .requestMatchers("/api/v1/users").hasAnyAuthority("MAN", "STA", "VET")
                // Các yêu cầu còn lại phải được xác thực
                .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));

        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        // Định nghĩa secret key (nên được lưu trữ an toàn và bảo mật)
        String secretKey = SIGNER_KEY; // Thay "your-secret-key" bằng khoá bí mật thực tế

        // Chuyển secret key thành dạng SecretKeySpec
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS512");

        // Sử dụng NimbusJwtDecoder để giải mã JWT với khoá bí mật
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();


    }




}
