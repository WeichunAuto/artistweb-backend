package com.bobby.artistweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicImageDTO {
    private String optimizedImageName;
    private String optimizedImageType;
    @Lob
    private byte[] optimizedImageData;

}
