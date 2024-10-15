package org.ftf.koifishveterinaryservicecenter.service.userservice;

import com.nimbusds.jose.JOSEException;
import org.ftf.koifishveterinaryservicecenter.dto.AuthenticationRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.IntrospectRequestDTO;
import org.ftf.koifishveterinaryservicecenter.dto.response.AuthenticationResponse;
import org.ftf.koifishveterinaryservicecenter.dto.response.IntrospectResponse;

import java.text.ParseException;

public interface AuthenticationService {
    Integer getAuthenticatedUserId();

    IntrospectResponse introspect(IntrospectRequestDTO request) throws ParseException, JOSEException;

    AuthenticationResponse authenticate(AuthenticationRequestDTO request);

    IntrospectResponse getUserInfoFromToken(IntrospectRequestDTO request) throws ParseException;
}
