package uk.gov.hmcts.reform.et.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseDetails;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.TooManyMethods"})
public class CaseService {

    public CaseDetails retrieveCase(String authorization, String caseId) {
        // TODO: Add ccd client
        log.info("Received request to fetch case details for: {} ", caseId);
        return new CaseDetails();
    }
}
