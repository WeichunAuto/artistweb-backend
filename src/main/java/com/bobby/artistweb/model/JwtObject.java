package com.bobby.artistweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class JwtObject {
    private String token = "";
    private long expireTimeMillis = 0;
}
