package org.ftf.koifishveterinaryservicecenter.service.certificateservice;

import org.ftf.koifishveterinaryservicecenter.entity.Certificate;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.exception.CertificateNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.repository.CertificateRepository;
import org.ftf.koifishveterinaryservicecenter.service.fileservice.FileUploadService;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final FileUploadService fileUploadService;
    private final UserService userService;

    @Autowired
    public CertificateServiceImpl(
            CertificateRepository certificateRepository
            , FileUploadService fileUploadService
            , UserService userService) {
        this.certificateRepository = certificateRepository;
        this.fileUploadService = fileUploadService;
        this.userService = userService;
    }

    @Override
    public String AddVeterinarianCertificate(Integer veterinarianId, String certificateName, MultipartFile certificateFromRequest) throws IOException, UserNotFoundException {
        User veterinarian = userService.getVeterinarianById(veterinarianId);
        String path = fileUploadService.uploadCertificate(certificateFromRequest);

        Certificate certificate = new Certificate();
        certificate.setCertificateName(certificateName);
        certificate.setFilePath(path);
        certificate.setUploadDate(LocalDateTime.now());
        certificate.setVeterinarian(veterinarian);

        certificateRepository.save(certificate);
        return path;
    }

    @Override
    public List<Certificate> getAllCertificatesByVeterinarianId(Integer veterinarianId) throws UserNotFoundException {
        User veterinarian = userService.getVeterinarianById(veterinarianId);
        List<Certificate> certificates = certificateRepository.findByVeterinarianId(veterinarian.getUserId());
        if (certificates.isEmpty()) {
            throw new CertificateNotFoundException("Certificate not found for Veterinarian with Id: " + veterinarianId);
        }
        return certificates;
    }

}
