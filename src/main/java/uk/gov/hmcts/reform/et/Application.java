package uk.gov.hmcts.reform.et;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.jms.annotation.EnableJms;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.ccd.client.CaseAssignmentApi;
import uk.gov.hmcts.reform.ccd.client.CaseUserApi;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.idam.client.IdamApi;

@SpringBootApplication(
    scanBasePackages = {"uk.gov.hmcts.ccd.sdk", "uk.gov.hmcts.reform.et"}
)
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
@EnableFeignClients(
    clients = {
        IdamApi.class,
        ServiceAuthorisationApi.class,
        CaseUserApi.class,
        CoreCaseDataApi.class,
        CaseAssignmentApi.class,
    }
)
@EnableJms
@ImportAutoConfiguration({FeignAutoConfiguration.class})

public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
