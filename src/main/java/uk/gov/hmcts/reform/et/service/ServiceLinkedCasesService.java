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

    public ListTypeItem<CaseLink> getServiceLinkedCases(String authorization, ServiceHearingRequest request)
            throws GetCaseException {
        CaseDetails caseDetails = caseService.retrieveCase(authorization, request.getCaseId());
        CaseData caseData = CaseDataMapping.mapRequestCaseDataToCaseData(caseDetails.getData());
        if (caseData.getCaseLinks() == null) {
            return new ListTypeItem<>();
        }
        return caseData.getCaseLinks();
    }
}
