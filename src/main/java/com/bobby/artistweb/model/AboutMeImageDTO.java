package com.bobby.artistweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AboutMeImageDTO {
    private String optimizedImageName;
    private String optimizedImageType;
    private byte[] optimizedImageData;
}
