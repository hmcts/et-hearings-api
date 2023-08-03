package uk.gov.hmcts.reform.et.service;

import lombok.*;
import lombok.extern.slf4j.*;
import org.apache.commons.lang3.*;
import org.springframework.stereotype.*;
import uk.gov.hmcts.reform.authorisation.generators.*;
import uk.gov.hmcts.reform.ccd.client.model.*;
import uk.gov.hmcts.reform.et.exception.*;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaseService {

    private final AuthTokenGenerator authTokenGenerator;
    private final CoreCaseDataApi ccdApiClient;
    public String retrieveCase(String authorization, String caseId) throws GetCaseException {
        // CCD client to be added

        if (StringUtils.isEmpty(caseId)) {
            String cause = String.format("The Case id was invalid");
            GetCaseException exc = new GetCaseException(cause);
            log.error(cause, exc);
            throw exc;
        }
        log.info("Received request to fetch case details for: {} ", caseId);
   //     CaseDetails case = ccdApiClient.getCase(authorization, authTokenGenerator.generate(), caseId);
      //  return case;
     //  return new CaseDetails();
        return "test";
    }
}
