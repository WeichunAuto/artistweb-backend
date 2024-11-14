package com.bobby.artistweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaintWorkDecorationDTO {

    private long id = 0;

    private String imageName;
    private String imageType;

    private boolean cover;


}
