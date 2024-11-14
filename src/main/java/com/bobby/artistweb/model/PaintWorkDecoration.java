package com.bobby.artistweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaintWorkDecoration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    @ManyToOne
    @JoinColumn(name = "paintWork_id", nullable = false)
    private PaintWork paintWork;

    @Lob
    private byte[] imageData;
    private String imageName;
    private String imageType;

    @Lob
    private byte[] optimizedImageData;

    private boolean cover;


}
