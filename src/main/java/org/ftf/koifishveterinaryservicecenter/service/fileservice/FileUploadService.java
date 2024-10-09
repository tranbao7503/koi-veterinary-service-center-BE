package org.ftf.koifishveterinaryservicecenter.service.fileservice;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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

        // Lấy phần mở rộng của file
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // Tạo tên file ngẫu nhiên bằng UUID
        String randomFileName = UUID.randomUUID().toString() + fileExtension;

        // Lưu file vào thư mục
        Path filePath = uploadPath.resolve(randomFileName);
        Files.copy(file.getInputStream(), filePath);

        // Trả về đường dẫn file ngẫu nhiên
        return randomFileName;
    }

}
