package com.demo.image.login.config;

import com.megvii.cloud.http.CommonOperate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ejaiwng on 3/2/2018.
 */
@Configuration
public class Config {

    @Value("${image.compare.api.key}")
    private String imageApiKey;

    @Value("${image.compare.api.secret}")
    private String imageApiSecret;

    @Bean
    public CommonOperate commonOperate() {
        return new CommonOperate(imageApiKey, imageApiSecret, true);
    }
}
