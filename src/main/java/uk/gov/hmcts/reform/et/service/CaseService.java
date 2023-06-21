package uk.gov.hmcts.reform.et.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaseService {

    public CaseDetails retrieveCase(String authorization, String caseId) throws GetCaseException {
        // CCD client to be added

        if (StringUtils.isEmpty(caseId)) {
            String cause = String.format("The Case id was invalid");
            GetCaseException exc = new GetCaseException(cause);
            log.error(cause, exc);
            throw exc;
        }
        log.info("Received request to fetch case details for: {} ", caseId);
        return new CaseDetails();
    }
}
