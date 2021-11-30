package de.fevkav.mooddetection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataMockConfig {

    @Bean
    public DataMock dataMock() {
        return new DataMock();
    }

}
