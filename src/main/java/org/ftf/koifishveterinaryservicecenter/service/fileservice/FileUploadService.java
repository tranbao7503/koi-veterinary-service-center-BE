package org.ftf.koifishveterinaryservicecenter.service.fileservice;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    // Đường dẫn thư mục lưu trữ ảnh
    private final String IMGAGE_DIR = "images/";

    public String uploadFile(MultipartFile file) throws IOException {
        // Tạo đường dẫn lưu file
        Path uploadPath = Paths.get(IMGAGE_DIR);

        // Kiểm tra thư mục nếu chưa tồn tại thì tạo
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Lưu file vào thư mục
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        // Trả về đường dẫn file
        return fileName;
    }

}
