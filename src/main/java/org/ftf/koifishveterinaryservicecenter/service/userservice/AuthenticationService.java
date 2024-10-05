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
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Logger;

@Service
public class AuthenticationService {


    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${jwt.signer}")
    private String SIGNER_KEY;


    public IntrospectResponse introspect(IntrospectRequestDTO request) throws ParseException {
        var token = request.getToken();

        if (!isSignatureValid(token)) {
            return null;
        }

        SignedJWT signedJWT = SignedJWT.parse(token);
        var claimsSet = signedJWT.getJWTClaimsSet();

        return IntrospectResponse.builder()
                .userId(((Long) claimsSet.getClaim("userId")).intValue())
                .roleId((String) claimsSet.getClaim("role"))
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequestDTO request) {
        Logger.getAnonymousLogger().info(SIGNER_KEY);
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AuthenticationException("Unauthenication");
        }
        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }


    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject("KoiFish")
                .issuer("KoiFish.com")
                .issueTime(Date.from(Instant.now()))
                .claim("userId", user.getUserId())
                .claim("role", user.getRole().getRoleKey())
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
