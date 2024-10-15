package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    @JsonProperty("image_id")
    private Integer imageId;
    @JsonProperty("source_path")
    private String sourcePath;
    @JsonProperty("fish_id")
    private Integer fishId;
    // Thêm trường enable
    @JsonProperty("enable")
    private boolean enabled;
}
