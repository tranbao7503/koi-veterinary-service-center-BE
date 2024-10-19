package org.ftf.koifishveterinaryservicecenter.service.fileservice;

import org.springframework.stereotype.Service;

@Service
public class FileDownloadService {

    private final String IMAGE_URL = "/files/images/";
    private final String CERTIFICATE_URL = "/files/certificates/";

    public String getImageUrl(String fileName) {
        return IMAGE_URL + fileName;
    }

    public String getCertificateUrl(String fileName) {
        return CERTIFICATE_URL + fileName;
    }
}
