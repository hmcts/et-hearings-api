package uk.gov.hmcts.reform.et.helper.mapping;

import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.et.common.model.ccd.items.RepresentedTypeRItem;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;
import uk.gov.hmcts.et.common.model.hmc.Judiciary;
import uk.gov.hmcts.et.common.model.hmc.PanelRequirements;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class ServiceHearingValuesMapping {


    private ServiceHearingValuesMapping() {
    }

    public static ServiceHearingValues mapServiceHearingValues(
            CaseDetails caseDetails,
            CaseData caseData,
            String hearingRequest,
            List<HearingTypeItem> hearingCollection,
            List<RespondentSumTypeItem> respondents,
            List<RepresentedTypeRItem> legalReps,
            ReferenceDataServiceHolder referenceDataServiceHolder) {
        log.info("Mapping hearing values for Case id : {}, generating Service Hearing Values", caseDetails.getId());
        // ServiceHearingsValues is returned with caseType populated
        // (e.g. with ET_EnglandWales) when a case is successfully fetched from ccd.
        return ServiceHearingValues.builder()
                .publicCaseName(HearingsCaseMapping.getPublicCaseName(caseData))
                .caseDeepLink(HearingsCaseMapping.getCaseDeepLink(caseData))
                .caseManagementLocationCode(HearingsDetailsMapping.getTribunalAndOfficeLocation())
                .caseRestrictedFlag(HearingsCaseMapping.getCaseRestrictedFlag(caseData))
                .caseSlaStartDate(HearingsCaseMapping.getCaseCreated(caseData))
                .hmctsInternalCaseName(HearingsCaseMapping.getCaseNameHmctsInternal(caseData))
                .autoListFlag(HearingsDetailsMapping.getAutoListFlag(caseData))
                .hearingType(HearingsDetailsMapping.getHearingType())
                .duration(HearingsDetailsMapping.getHearingDuration())
                .hearingPriorityType(HearingsDetailsMapping.getHearingPriorityType())
                .numberOfPhysicalAttendees(HearingsDetailsMapping.getNumberOfPhysicalAttendees())
                .hearingInWelshFlag(HearingsDetailsMapping.isHearingInWelshFlag(caseData))
                .caseAdditionalSecurityFlag(HearingsCaseMapping.getCaseAdditionalSecurityFlag(caseData))
                .privateHearingRequiredFlag(HearingsDetailsMapping.isPrivateHearingRequiredFlag(caseData))
                .leadJudgeContractType(HearingsDetailsMapping.getLeadJudgeContractType(caseData))
                .hearingIsLinkedFlag(HearingsDetailsMapping.isHearingIsLinkedFlag(caseData))
                .hmctsServiceId(referenceDataServiceHolder.getHmctsServiceId())
                .caseInterpreterRequiredFlag(HearingsCaseMapping.getCaseInterpreterRequiredFlag(caseData))
                .caseFlags(CaseFlagsMapping.getCaseFlags(caseData))
                .caseCategories(HearingsCaseMapping.getCaseCategories())
                .screenFlow(HearingsCaseMapping.getScreenFlow())
                .vocabulary(HearingsCaseMapping.getVocabulary())
                .hearingWindow(HearingsDetailsMapping.getHearingWindow())
                .hearingLocations(HearingsDetailsMapping.getHearingLocation())
                .parties(HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData, respondents, legalReps))
                .panelRequirements(new PanelRequirements())
                .judiciary(new Judiciary())
                .hearingChannels(new ArrayList<>())
                .build();
    }
}
