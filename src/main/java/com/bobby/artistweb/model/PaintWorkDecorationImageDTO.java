package com.bobby.artistweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaintWorkDecorationImageDTO {
    private long id = 0;

    private byte[] imageData;

}
