package uk.gov.hmcts.reform.et.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.hmc.Judiciary;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.helper.mapping.CaseDataMapping;
import uk.gov.hmcts.reform.et.helper.mapping.CaseFlagsMapping;
import uk.gov.hmcts.reform.et.helper.mapping.HearingsCaseMapping;
import uk.gov.hmcts.reform.et.helper.mapping.HearingsDetailsMapping;
import uk.gov.hmcts.reform.et.helper.mapping.HearingsPartyMapping;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceHearingsService {

    private final CaseService caseService;

    @Value("${et.hmctsServiceId}")
    private String hmctsServiceId;

    @Value("${case-details-url.exui}")
    private String exuiUrl;

    /**
     * Gets ServiceHearingValues required for ExUI to display the Hearings tab.
     * @param authorization Bearer token used to look up the case
     * @param request Request object containing the case ID
     * @throws GetCaseException When the case cannot be fetched from CCD.
     */
    public ServiceHearingValues getServiceHearingValues(String authorization, ServiceHearingRequest request)
            throws GetCaseException {
        CaseDetails caseDetails = caseService.retrieveCase(authorization, request.getCaseId());
        CaseData caseData = CaseDataMapping.mapCaseData(caseDetails.getData());

        log.info("Mapping hearing values for Case id : {}, generating Service Hearing Values", caseDetails.getId());
        return mapServiceHearingValues(caseDetails, caseData);
    }

    private ServiceHearingValues mapServiceHearingValues(CaseDetails caseDetails, CaseData caseData) {
        return ServiceHearingValues.builder()
                .autoListFlag(HearingsDetailsMapping.getAutoListFlag(caseData))
                .caseAdditionalSecurityFlag(HearingsCaseMapping.getCaseAdditionalSecurityFlag(caseData))
                .caseCategories(HearingsCaseMapping.getCaseCategories())
                .caseDeepLink(HearingsCaseMapping.getCaseDeepLink(exuiUrl, caseDetails.getId().toString()))
                .caseFlags(CaseFlagsMapping.getCaseFlags(caseData))
                .caseInterpreterRequiredFlag(HearingsCaseMapping.getCaseInterpreterRequiredFlag(caseData))
                .caseManagementLocationCode(HearingsDetailsMapping.getTribunalAndOfficeLocation(caseData))
                .caseRestrictedFlag(HearingsCaseMapping.getCaseRestrictedFlag(caseData))
                .caseSlaStartDate(HearingsCaseMapping.getCaseCreated(caseData))
                .caseType(caseDetails.getCaseTypeId())
                .duration(0)
                .hearingChannels(new ArrayList<>())
                .hearingInWelshFlag(HearingsDetailsMapping.isHearingInWelshFlag(caseData))
                .hearingIsLinkedFlag(HearingsDetailsMapping.isHearingIsLinkedFlag(caseData))
                .hearingLocations(HearingsDetailsMapping.getHearingLocation())
                .hearingPriorityType(HearingsDetailsMapping.getHearingPriorityType())
                .hearingType(null)
                .hearingWindow(HearingsDetailsMapping.getHearingWindow())
                .hmctsInternalCaseName(HearingsCaseMapping.getCaseNameHmctsInternal(caseData))
                .hmctsServiceID(hmctsServiceId)
                .judiciary(new Judiciary())
                .leadJudgeContractType(HearingsDetailsMapping.getLeadJudgeContractType(caseData))
                .numberOfPhysicalAttendees(HearingsDetailsMapping.getNumberOfPhysicalAttendees())
                .parties(HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData))
                .panelRequirements(null)
                .privateHearingRequiredFlag(HearingsDetailsMapping.isPrivateHearingRequiredFlag(caseData))
                .publicCaseName(HearingsCaseMapping.getPublicCaseName(caseData))
                .screenFlow(HearingsCaseMapping.getScreenFlow())
                .vocabulary(HearingsCaseMapping.getVocabulary())
                .build();
    }
}
