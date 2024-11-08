package com.bobby.artistweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaintWorkEditDTO {

    private int id = 0;
    private String title = "";
    private String description = "";
    private int price = 0;
    private String status = "active";

    private int dimensionWidth = 0;
    private int dimensionHeight = 0;

}
