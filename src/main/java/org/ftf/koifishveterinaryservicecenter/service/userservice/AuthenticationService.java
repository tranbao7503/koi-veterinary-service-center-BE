package org.ftf.koifishveterinaryservicecenter.service.userservice;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.ftf.koifishveterinaryservicecenter.dto.AuthenticationRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.IntrospectRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.LogoutRequest;
import org.ftf.koifishveterinaryservicecenter.dto.RefreshRequest;
import org.ftf.koifishveterinaryservicecenter.dto.response.AuthenticationResponse;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;
import org.ftf.koifishveterinaryservicecenter.entity.User;

import java.text.ParseException;


public interface AuthenticationService {
    Integer getAuthenticatedUserId();

    IntrospectResponse introspect(IntrospectRequestDTO request) throws ParseException, JOSEException;

    AuthenticationResponse authenticate(AuthenticationRequestDTO request);

    IntrospectResponse getUserInfoFromToken(IntrospectRequestDTO request) throws ParseException;

    String getAuthenticatedUserRoleKey();

    String generateToken(User user);

    boolean isSignatureValid(String token);

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshRequest request) throws JOSEException, ParseException;

    SignedJWT verifyToken(String token) throws JOSEException, ParseException;
}
