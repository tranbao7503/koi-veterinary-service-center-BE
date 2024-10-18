package org.ftf.koifishveterinaryservicecenter.service.certificateservice;

import org.ftf.koifishveterinaryservicecenter.entity.Certificate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CertificateService {

    String AddVeterinarianCertificate(Integer veterinarianId, String certificateName, MultipartFile certificate) throws IOException;

    List<Certificate> getAllCertificatesByVeterinarianId(Integer veterinarianId);
}
