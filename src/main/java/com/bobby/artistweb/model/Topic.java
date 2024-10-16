package com.bobby.artistweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Component
@Data
@NoArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;
    private String title = "";

    @Column(columnDefinition = "TEXT")
    private String description = "";

    private String imageName = "";
    private String imageType = "";
    @Lob
    private byte[] imageData =  new byte[0];

}
