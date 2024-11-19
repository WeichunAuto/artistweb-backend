package com.bobby.artistweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaintWorkDTO {

    private long id = 0;
    private String title = "";
    private String description = "";
    private int price = 0;
    private String status = "active";

    private String date;

    private String year ="";

    private int dimensionWidth = 0;
    private int dimensionHeight = 0;

    private long decorationCount;

    private String coverURL = "";

}
