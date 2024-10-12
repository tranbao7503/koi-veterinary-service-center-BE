package org.ftf.koifishveterinaryservicecenter.service.userservice;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.ftf.koifishveterinaryservicecenter.dto.AuthenticationRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.IntrospectRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.LogoutRequest;
import org.ftf.koifishveterinaryservicecenter.dto.RefreshRequest;
import org.ftf.koifishveterinaryservicecenter.dto.response.AuthenticationResponse;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;
import org.ftf.koifishveterinaryservicecenter.entity.InvalidatedToken;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AppException;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.exception.ErrorCode;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.InvalidatedTokenRepository;
import org.ftf.koifishveterinaryservicecenter.repository.RoleRepository;
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
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    InvalidatedTokenRepository invalidatedTokenRepository;
    @Value("${jwt.signer}")
    private String SIGNER_KEY;

    /*public IntrospectResponse introspect(IntrospectRequestDTO request) throws ParseException {
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
    }*/
    public IntrospectResponse introspect(IntrospectRequestDTO request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
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
                .subject(user.getUsername())
                .issuer("KoiFish.com")
                .issueTime(Date.from(Instant.now()))
                .claim("userId", user.getUserId())
                .claim("scope", user.getRole().getRoleKey())
                .expirationTime(Date.from(Instant.now().plus(3, ChronoUnit.HOURS)))
                .jwtID(UUID.randomUUID().toString())
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

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken());

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {

        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws JOSEException, ParseException {
        //Kiem tra token con hieu luc hay k
        var signJWT = verifyToken(request.getToken());

        var jit = signJWT.getJWTClaimsSet().getJWTID();

        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username).orElseThrow(()
                -> new AppException(ErrorCode.UNAUTHENTICATED));

        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();


    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
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
    /*private String buildScope(User user){
        StringJoiner stringJoiner =new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRole()))
           user.getRole().getRoleKey().forEach(stringJoiner::add);
        return stringJoiner.toString();
}*/
}
