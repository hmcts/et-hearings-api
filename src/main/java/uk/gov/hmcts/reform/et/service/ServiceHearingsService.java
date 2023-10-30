package uk.gov.hmcts.reform.et.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.RepresentedTypeRItem;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.helper.mapping.CaseDataMapping;
import uk.gov.hmcts.reform.et.helper.mapping.ServiceHearingValuesMapping;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceHearingsService {

    private final CaseService caseService;

    private final ReferenceDataServiceHolder referenceDataServiceHolder;

    public ServiceHearingValues getServiceHearingValues(
        String authorization,
        ServiceHearingRequest request
    ) throws GetCaseException {
        CaseDetails caseDetails = caseService.retrieveCase(authorization, request.getCaseId());
        CaseData caseData = CaseDataMapping.mapRequestCaseDataToCaseData(caseDetails.getData());
        List<RespondentSumTypeItem> respondents = CaseDataMapping.mapRespondentDetailsToCaseData(
            caseData.getRespondentCollection());
        List<RepresentedTypeRItem> legalReps = CaseDataMapping.mapLegalRepsToCaseData(caseData.getRepCollection());

        return ServiceHearingValuesMapping.mapServiceHearingValues(caseDetails,
                                                                   caseData,
                                                                   respondents,
                                                                   legalReps,
                                                                   referenceDataServiceHolder);
    }
}
