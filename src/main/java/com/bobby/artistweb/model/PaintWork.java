package com.bobby.artistweb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaintWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;

    private String title = "";

    @Column(columnDefinition = "TEXT")
    private String description = "";

    private int price = 0;
    private String status = "active";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private Date date;

    @Lob
    private byte[] imageData;
    private String imageName;
    private String imageType;

    public PaintWork(int id) {
        this.id = id;
    }

}
