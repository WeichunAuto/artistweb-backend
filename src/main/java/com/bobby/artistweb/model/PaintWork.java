package com.bobby.artistweb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaintWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    private String title = "";

    @Column(columnDefinition = "TEXT")
    private String description = "";

    private int price = 0;
    private String status = "active";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private Date date;

    private String year ="";

    @Column(nullable = false)
    private int dimensionWidth = 0;
    @Column(nullable = false)
    private int dimensionHeight = 0;

//    @OneToMany(mappedBy = "paintWork", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<PaintWorkDecoration> decorations;

    public PaintWork(int id) {
        this.id = id;
    }

}
