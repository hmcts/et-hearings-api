package uk.gov.hmcts.reform.et.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.helper.mapping.ServiceHearingValuesMapping;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceHearingsService {

    private final CaseService caseService;

    public ServiceHearingValues getServiceHearingValues(
        String authorization,
        ServiceHearingRequest request
    ) throws GetCaseException {
        CaseDetails caseDetails = caseService.retrieveCase(authorization, request.getCaseId());

        return ServiceHearingValuesMapping.mapServiceHearingValues(caseDetails);
    }
}
