package org.ftf.koifishveterinaryservicecenter.service.userservice;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.ftf.koifishveterinaryservicecenter.dto.AuthenticationRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.IntrospectRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.response.AuthenticationResponse;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.RoleRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Value("${jwt.signer}")
    private String SIGNER_KEY;

    @Override
    public Integer getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        Map<String, Object> claims = jwt.getClaims();
        Long longValue = (Long) claims.get("userId");
        return longValue.intValue();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequestDTO request) throws ParseException {
        var token = request.getToken();

        if (!isSignatureValid(token)) {
            return null;
        }

        SignedJWT signedJWT = SignedJWT.parse(token);
        var claimsSet = signedJWT.getJWTClaimsSet();

        // Lấy timeout từ claims (Giả sử trường timeout có trong JWT claims)
        Integer timeout = claimsSet.getIntegerClaim("timeout"); // Hoặc trường hợp khác, điều chỉnh theo cách bạn lưu trữ timeout trong JWT

        return IntrospectResponse.builder()
                .userId(((Long) claimsSet.getClaim("userId")).intValue())
                .roleId((String) claimsSet.getClaim("role"))
                .timeout(timeout) // Gán giá trị timeout
                .build();
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequestDTO request) {
        Logger.getAnonymousLogger().info(SIGNER_KEY);
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AuthenticationException("Unauthenication");
        }
        String token = generateToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }


    public IntrospectResponse getUserInfoFromToken(IntrospectRequestDTO request) throws AuthenticationException, ParseException {
        var token = request.getToken();
        if (!isSignatureValid(token)) {
            return null;
        }
        SignedJWT signedJWT = SignedJWT.parse(token);
        var claimsSet = signedJWT.getJWTClaimsSet();
        return IntrospectResponse.builder()
                .userId(((Long) claimsSet.getClaim("userId")).intValue())
                .roleId((String) claimsSet.getClaim("scope"))
                .build();
    }


    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Định nghĩa timeout (3 giờ)
        int timeoutInSeconds = 3 * 60 * 60; // 3 giờ

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject("KoiFish")
                .issuer("KoiFish.com")
                .issueTime(Date.from(Instant.now()))
                .claim("userId", user.getUserId())
                .claim("role", user.getRole().getRoleKey())
                .claim("timeout", timeoutInSeconds) // Thêm trường timeout
                .expirationTime(Date.from(Instant.now().plus(3, ChronoUnit.HOURS)))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            Logger.getAnonymousLogger().info(SIGNER_KEY);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            // Log lỗi và ném ngoại lệ tùy chỉnh
            throw new AuthenticationException();
        }
    }

    private boolean isSignatureValid(String token) {
        // Parse the JWS and verify its RSA signature
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            return signedJWT.verify(verifier);
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }


}