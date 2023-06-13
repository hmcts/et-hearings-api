package uk.gov.hmcts.reform.et.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseDetails;
import uk.gov.hmcts.reform.et.helper.mapping.ServiceHearingValuesMapping;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceHearingsService {

    private final CaseService caseService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ServiceHearingValues getServiceHearingValues(
        String authorization,
        ServiceHearingRequest request
    ) {
        CaseDetails caseDetails = caseService.getUserCase(authorization, request.getCaseId());
        ServiceHearingValues hearingValues = ServiceHearingValuesMapping.mapServiceHearingValues(caseDetails);
        return hearingValues;
    }
}
