package org.ftf.koifishveterinaryservicecenter.service.userservice;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.ftf.koifishveterinaryservicecenter.dto.*;
import org.ftf.koifishveterinaryservicecenter.dto.response.AuthenticationResponse;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;
import org.ftf.koifishveterinaryservicecenter.entity.InvalidatedToken;
import org.ftf.koifishveterinaryservicecenter.entity.Role;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.AppException;
import org.ftf.koifishveterinaryservicecenter.exception.AuthenticationException;
import org.ftf.koifishveterinaryservicecenter.exception.ErrorCode;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.InvalidatedTokenRepository;
import org.ftf.koifishveterinaryservicecenter.repository.RoleRepository;
import org.ftf.koifishveterinaryservicecenter.repository.UserRepository;
import org.ftf.koifishveterinaryservicecenter.repository.httpclient.OutboundIdentityClient;
import org.ftf.koifishveterinaryservicecenter.repository.httpclient.OutboundUserClient;
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
import java.util.UUID;
import java.util.logging.Logger;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    private final OutboundIdentityClient outboundIdentityClient;

    private final OutboundUserClient outboundUserClient;

    @Value("${frontend.domain}")
    private String frontendDomain;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    protected String CLIENT_ID;


    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal

    protected String REDIRECT_URI = frontendDomain + "/authenticate";

    @NonFinal
    protected String GRANT_TYPE = "authorization_code";

    @Value("${jwt.signer}")
    private String SIGNER_KEY;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     InvalidatedTokenRepository invalidatedTokenRepository,
                                     OutboundIdentityClient outboundIdentityClient,
                                     OutboundUserClient outboundUserClient) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
        this.outboundIdentityClient = outboundIdentityClient;
        this.outboundUserClient = outboundUserClient;

    }

    @Override
    public Integer getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        Map<String, Object> claims = jwt.getClaims();
        Long longValue = (Long) claims.get("userId");
        return longValue.intValue();
    }

    @Override
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

    @Override
    public String getAuthenticationRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        Map<String, Object> claims = jwt.getClaims();
        return (String) claims.get("role");
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

    @Override
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

    @Override
    public String getAuthenticatedUserRoleKey() {
        Integer userId = getAuthenticatedUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getRole().getRoleKey();
    }

    @Override
    public String generateToken(User user) {
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

    @Override
    public boolean isSignatureValid(String token) {
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

    @Override
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

    @Override
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

    @Override
    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    public AuthenticationResponse outboundAuthenticate(String code) {
        var response = outboundIdentityClient
                .exchangeToken(ExchangeTokenRequest.builder()
                        .code(code)
                        .clientID(CLIENT_ID)
                        .clientSecret(CLIENT_SECRET)
                        .redirectUri(REDIRECT_URI)
                        .grantType(GRANT_TYPE)
                        .build());

        log.info("TOKEN RESPONSE{}", response);

        // Get user info
        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        log.info("User info{}:", userInfo);

        //On board user
        Role role = roleRepository.findByRoleKey("CUS");
        String password = "123456789";

        var user = userRepository.findUserByEmail(userInfo.getEmail()).orElseGet(
                () -> userRepository.save(User.builder()
                        .email(userInfo.getEmail())
                        .username(userInfo.getEmail())
                        .firstName(userInfo.getGivenName()) // Bổ sung first_name
                        .lastName(userInfo.getFamilyName())   // Có thể bổ sung last_name nếu cần
                        .role(role)
                        .password("") // Lúc này có thể đtrống nếu không cần
                        .build()));

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }


}