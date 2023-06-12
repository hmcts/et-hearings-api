package uk.gov.hmcts.reform.et.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;
import static org.springframework.http.ResponseEntity.status;
import uk.gov.hmcts.reform.et.service.ServiceHearingsService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ServiceHearingsController {

    private final ServiceHearingsService serviceHearingsService;

    @PostMapping("/serviceHearingValues")
    public ResponseEntity<ServiceHearingValues> serviceHearingValues(
        @RequestHeader("Authorization") String authorization,
        @RequestBody ServiceHearingRequest request)
      {
        try {
            log.info("Retrieving case details using Case id : {}, for use in generating Service Hearing Values",
                     request.getCaseId());

            ServiceHearingValues model = serviceHearingsService.getServiceHearingValues(authorization, request);
            return status(HttpStatus.OK).body(model);
        } catch (Exception exc) {
            logException(exc, request.getCaseId());
            throw exc;
        }
    }
    private void logException(Exception exc, String caseId) {
            log.error("Error updating case id {}, {}", caseId, exc.toString());
    }
}
