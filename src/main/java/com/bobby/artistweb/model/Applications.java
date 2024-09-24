package com.bobby.artistweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@Component

@Data
public class Applications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;
    private String appName = "users";
    private String appKey = "7dfb4cf67742cb0660305e56ef816c53fcec892cae7f6ee39b75f34e659d672c";

}
