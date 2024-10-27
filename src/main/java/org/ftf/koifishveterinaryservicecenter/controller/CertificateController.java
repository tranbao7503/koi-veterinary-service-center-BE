package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.CertificateDto;
import org.ftf.koifishveterinaryservicecenter.entity.Certificate;
import org.ftf.koifishveterinaryservicecenter.exception.CertificateNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.UserNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.CertificateMapper;
import org.ftf.koifishveterinaryservicecenter.service.certificateservice.CertificateService;
import org.ftf.koifishveterinaryservicecenter.service.fileservice.FileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/certificates")
//@CrossOrigin
public class CertificateController {

    private final CertificateService certificateService;
    private final FileDownloadService fileDownloadService;

    @Autowired
    public CertificateController(
            CertificateService certificateService
            , FileDownloadService fileDownloadService) {
        this.certificateService = certificateService;
        this.fileDownloadService = fileDownloadService;
    }

    /*
     * Add certificate information of a veterinarian into system
     * Actors: Manager
     * */
    @PostMapping("/veterinarians/{veterinarianId}")
    public ResponseEntity<?> addVeterinarianCertificate(
            @PathVariable("veterinarianId") Integer veterinarianId
            , @RequestParam("certificateName") String certificateName
            , @RequestParam("file") MultipartFile file) {
        try {
            String path = certificateService.AddVeterinarianCertificate(veterinarianId, certificateName, file);
            return new ResponseEntity<>(path, HttpStatus.OK);
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * View certificate list of a veterinarian
     * Actors: Manager
     * */
    @GetMapping("/veterinarians/{veterinarianId}")
    public ResponseEntity<?> getCertificate(
            @PathVariable("veterinarianId") Integer veterinarianId) {
        try {
            List<Certificate> certificates = certificateService.getAllCertificatesByVeterinarianId(veterinarianId);
            List<CertificateDto> certificateDtos = certificates.stream()
                    .map(CertificateMapper.INSTANCE::convertToCertificateDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(certificateDtos, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Download a certificate file
     * Actors: Manager
     */
    @GetMapping("/{filename}")
    public ResponseEntity<?> downloadCertificate(@PathVariable("filename") String filename) {
        try {
            Resource resource = fileDownloadService.loadCertificateAsResource(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (FileNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
