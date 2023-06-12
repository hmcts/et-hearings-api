package uk.gov.hmcts.reform.et.service;
import feign.FeignException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

/**
 * Provides read and write access to cases stored by ET.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.TooManyMethods"})
public class CaseService {

    private final AuthTokenGenerator authTokenGenerator;
    private final CoreCaseDataApi ccdApiClient;

    /**
     * Given a case id in the case request, this will retrieve the correct {@link CaseDetails}.
     *
     * @param caseId id of the case
     * @return the associated {@link CaseDetails} for the ID provided
     */
    @Retryable({FeignException.class, RuntimeException.class})
    public CaseDetails getUserCase(String authorization, String caseId) {
        return ccdApiClient.getCase(authorization, authTokenGenerator.generate(), caseId);
    }
}
