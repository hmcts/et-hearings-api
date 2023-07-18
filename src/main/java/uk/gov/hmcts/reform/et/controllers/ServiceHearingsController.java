package uk.gov.hmcts.reform.et.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;
import uk.gov.hmcts.reform.et.service.ServiceHearingsService;

import static org.springframework.http.ResponseEntity.status;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ServiceHearingsController {

    private final ServiceHearingsService serviceHearingsService;

    @PostMapping("/serviceHearingValues")
    public ResponseEntity<ServiceHearingValues> serviceHearingValues(
        @RequestHeader("Authorization") String authorization,
        @RequestBody ServiceHearingRequest request
    ) throws GetCaseException {
        try {
            log.info("Retrieving case details using Case id : {}, for use in generating Service Hearing Values",
                     request.getCaseId());
            ServiceHearingValues model = serviceHearingsService.getServiceHearingValues(authorization, request);
            return status(HttpStatus.OK).body(model);
        } catch (Exception ex) {
            logException(ex, request.getCaseId());
            throw ex;
        }
    }

    private void logException(Exception ex, String caseId) {
        log.error("Error updating case id {}, {}", caseId, ex.toString());
    }
}
