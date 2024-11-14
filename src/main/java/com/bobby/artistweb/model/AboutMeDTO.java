package com.bobby.artistweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AboutMeDTO {
    private long id = 0;
    private String name ="";
    private String description ="";
}
