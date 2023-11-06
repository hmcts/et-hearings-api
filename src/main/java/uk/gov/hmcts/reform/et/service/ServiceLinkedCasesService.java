package uk.gov.hmcts.reform.et.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.ListTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.CaseLink;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.helper.mapping.CaseDataMapping;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceLinkedCasesService {
    private final CaseService caseService;

    /**
     * Gets linked cases for a case as required for ExUI to display part of the Request Hearings journey.
     * @param authorization Bearer token used to look up the case
     * @param request Request object containing the case ID
     * @throws GetCaseException When the case cannot be fetched from CCD.
     */
    public ListTypeItem<CaseLink> getServiceLinkedCases(String authorization, ServiceHearingRequest request)
            throws GetCaseException {
        CaseDetails caseDetails = caseService.retrieveCase(authorization, request.getCaseId());
        CaseData caseData = CaseDataMapping.mapCaseData(caseDetails.getData());
        if (caseData.getCaseLinks() == null) {
            return new ListTypeItem<>();
        }
        return caseData.getCaseLinks();
    }
}
