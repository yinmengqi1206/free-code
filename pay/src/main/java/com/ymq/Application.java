package com.ymq;

import com.stripe.Stripe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yinmengqi
 * @version 1.0
 * @date 2022/11/18 18:57
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class Application {

    public static void main(String[] args) {
        Stripe.apiKey = "sk_test_51M79K5DYvKVTauw2tvkdhTbQzGTVafTtBkoDxarGbGwH0zTxK4PuusMLNoc7l9OvcL1n0Py5Bn28jjAPMRfknw9900bHksOXXK";
        SpringApplication.run(Application.class, args);
    }
}
