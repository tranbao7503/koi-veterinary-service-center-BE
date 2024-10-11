package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.ftf.koifishveterinaryservicecenter.entity.User;

import java.time.LocalDateTime;

@Data
public class CertificateDto {

    @JsonProperty("certificate_id")
    private Integer id;

    @JsonProperty("certificate_name")
    private String certificateName;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("upload_date")
    private LocalDateTime uploadDate;

}
