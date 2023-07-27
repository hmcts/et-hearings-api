package uk.gov.hmcts.reform.et;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.openfeign.*;

@SpringBootApplication
@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.et"})
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class

public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
