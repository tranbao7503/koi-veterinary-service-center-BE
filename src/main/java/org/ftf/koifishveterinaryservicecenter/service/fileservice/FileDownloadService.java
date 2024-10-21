package org.ftf.koifishveterinaryservicecenter.service.fileservice;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileDownloadService {

    private final String IMAGE_DIR = "src/main/resources/static/files/images";
    private final String CERTIFICATE_DIR = "src/main/resources/static/files/certificates";

    public String getImageUrl(String fileName) {
        return "http://localhost:8080/files/images/" + fileName;
    }

    public String getCertificateUrl(String fileName) {
        return "http://localhost:8080/files/certificates/" + fileName;
    }

    public Resource loadImageAsResource(String fileName) throws FileNotFoundException {
        return loadFileAsResource(IMAGE_DIR, fileName);
    }

    public Resource loadCertificateAsResource(String fileName) throws FileNotFoundException {
        return loadFileAsResource(CERTIFICATE_DIR, fileName);
    }

    private Resource loadFileAsResource(String directory, String fileName) throws FileNotFoundException {
        try {
            Path filePath = Paths.get(directory).resolve(fileName).normalize();
            Resource resource = new FileSystemResource(filePath.toFile());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (Exception e) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }
}
