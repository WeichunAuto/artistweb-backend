package com.bobby.artistweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AboutMeDTO {
    private int id = 0;
    private String name ="";
    private String description ="";
}
