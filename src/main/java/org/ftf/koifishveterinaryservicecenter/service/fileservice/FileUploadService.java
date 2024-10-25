package org.ftf.koifishveterinaryservicecenter.service.fileservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${upload.path}" + "/images")
    private String IMGAGE_DIR;

    @Value("${upload.path}" + "/certificates")
    private String CERTIFICATE_DIR;

    public String uploadFile(MultipartFile file) throws IOException {
        // Create path
        Path uploadPath = Paths.get(IMGAGE_DIR);

        if (!Files.exists(uploadPath)) { // folder not existed -> create
            Files.createDirectories(uploadPath);
        }

        // Get extend of file
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }


        // Create random name for file
        String randomFileName = UUID.randomUUID().toString() + fileExtension;

        // Save
        Path filePath = uploadPath.resolve(randomFileName);
        Files.copy(file.getInputStream(), filePath);

        return randomFileName;
    }

    public String uploadCertificate(MultipartFile file) throws IOException {
        // Create path
        Path uploadPath = Paths.get(CERTIFICATE_DIR);

        if (!Files.exists(uploadPath)) { // folder not existed -> create
            Files.createDirectories(uploadPath);
        }

        // Get extend of file
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // Create random name for file
        String randomFileName = UUID.randomUUID().toString() + fileExtension;

        // Save
        Path filePath = uploadPath.resolve(randomFileName);
        Files.copy(file.getInputStream(), filePath);

        return randomFileName;
    }

}
