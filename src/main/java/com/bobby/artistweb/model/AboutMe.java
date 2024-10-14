package com.bobby.artistweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Entity
@Component
@Data
@NoArgsConstructor
public class AboutMe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;

    private String name ="";
    @Column(columnDefinition = "TEXT")
    private String description ="";

    private String imageName = "";
    private String imageType = "";
    @Lob
    private byte[] imageData =  new byte[0];

    private String optimizedImageName;
    private String optimizedImageType;
    @Lob
    private byte[] optimizedImageData;

}
