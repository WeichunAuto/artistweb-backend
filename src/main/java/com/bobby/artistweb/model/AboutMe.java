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

    private String title ="";
    @Column(columnDefinition = "TEXT")
    private String description ="";
}
