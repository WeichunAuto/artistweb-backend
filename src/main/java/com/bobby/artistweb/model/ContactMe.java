package com.bobby.artistweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactMe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String phoneNumber = "";
    @Column(columnDefinition = "TEXT")
    private String message = "";
    private boolean subscribe;

}
