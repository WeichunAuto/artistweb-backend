package com.bobby.artistweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaintWorkDTO {

    private int id = 0;
    private String title = "";
    private String description = "";
    private int price = 0;
    private String status = "active";

    private String date;

    private String year ="";

    private int dimensionWidth = 0;
    private int dimensionHeight = 0;

    private long decorationCount;

}
