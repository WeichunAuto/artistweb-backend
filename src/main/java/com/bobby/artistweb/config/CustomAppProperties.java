package com.bobby.artistweb.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "custom.app.email")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomAppProperties {
    private String sender;
    private String password;
    private String recipient;
}
