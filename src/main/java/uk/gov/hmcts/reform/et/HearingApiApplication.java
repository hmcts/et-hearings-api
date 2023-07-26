package uk.gov.hmcts.reform.et;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform"
}
)
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class HearingApiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HearingApiApplication.class, args);
    }
}
