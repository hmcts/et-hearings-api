package uk.gov.hmcts.reform.et.helper.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(caseData);
            log.info(json);
        } catch (JsonProcessingException e) {
            log.info("Failed to generate JSON for case data because " + e.getMessage());
        }

        return ServiceHearingValues.builder()
                .autoListFlag(HearingsDetailsMapping.getAutoListFlag(caseData))
                .caseAdditionalSecurityFlag(HearingsCaseMapping.getCaseAdditionalSecurityFlag(caseData))
                .caseCategories(HearingsCaseMapping.getCaseCategories())
                .caseDeepLink(HearingsCaseMapping.getCaseDeepLink(caseData))
                .caseFlags(CaseFlagsMapping.getCaseFlags(caseData))
                .caseInterpreterRequiredFlag(HearingsCaseMapping.getCaseInterpreterRequiredFlag(caseData))
                .caseManagementLocationCode(HearingsDetailsMapping.getTribunalAndOfficeLocation(caseData))
                .caseRestrictedFlag(HearingsCaseMapping.getCaseRestrictedFlag(caseData))
                .caseSlaStartDate(HearingsCaseMapping.getCaseCreated(caseData))
                .duration(HearingsDetailsMapping.getHearingDuration())
                .hearingChannels(new ArrayList<>())
                .hearingInWelshFlag(HearingsDetailsMapping.isHearingInWelshFlag(caseData))
                .hearingIsLinkedFlag(HearingsDetailsMapping.isHearingIsLinkedFlag(caseData))
                .hearingLocations(HearingsDetailsMapping.getHearingLocation())
                .hearingPriorityType(HearingsDetailsMapping.getHearingPriorityType())
                .hearingType(null)
                .hearingWindow(HearingsDetailsMapping.getHearingWindow())
                .hmctsInternalCaseName(HearingsCaseMapping.getCaseNameHmctsInternal(caseData))
                .hmctsServiceID("BBA3")
                .judiciary(new Judiciary())
                .leadJudgeContractType(HearingsDetailsMapping.getLeadJudgeContractType(caseData))
                .numberOfPhysicalAttendees(HearingsDetailsMapping.getNumberOfPhysicalAttendees())
                .parties(HearingsPartyMapping.buildPartyObjectForHearingPayload(caseData, respondents, legalReps))
                .panelRequirements(PanelRequirements.builder().panelPreferences(new ArrayList<>()).build())
                .privateHearingRequiredFlag(HearingsDetailsMapping.isPrivateHearingRequiredFlag(caseData))
                .publicCaseName(HearingsCaseMapping.getPublicCaseName(caseData))
                .screenFlow(null)
                .vocabulary(HearingsCaseMapping.getVocabulary())
                .build();
    }
}
