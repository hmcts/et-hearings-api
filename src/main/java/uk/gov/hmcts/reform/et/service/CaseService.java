package uk.gov.hmcts.reform.et.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseDetails;

/**
 * Provides read and write access to cases stored by ET.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.TooManyMethods"})
public class CaseService {

    public CaseDetails getUserCase(String authorization, String caseId) {
        log.info("Fetching case details for: {} ", caseId);
        return new CaseDetails();
    }
}
