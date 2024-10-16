package com.bobby.artistweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicImageDTO {
    private String imageName = "";
    private String imageType = "";
    @Lob
    private byte[] imageData =  new byte[0];

}
