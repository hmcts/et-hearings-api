package uk.gov.hmcts.reform.et.helper.mapping;

import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

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
        ReferenceDataServiceHolder referenceDataServiceHolder) {
        log.info("Mapping hearing values for Case id : {}, generating Service Hearing Values", caseDetails.getId());
        // ServiceHearingsValues is returned with caseType populated
        // (e.g. with ET_EnglandWales) when a case is successfully fetched from ccd.
        return ServiceHearingValues.builder()
            .publicCaseName(HearingsCaseMapping.getPublicCaseName(caseData))
                .caseDeepLink(HearingsCaseMapping.getCaseDeepLink(caseData))
            .tribunalAndOfficeLocation(HearingsDetailsMapping.getTribunalAndOfficeLocation())
            .caseRestrictedFlag(HearingsCaseMapping.getCaseRestrictedFlag(caseData))
            .receiptDate(HearingsCaseMapping.getCaseCreated(caseData))
            .caseNameHmctsInternal(HearingsCaseMapping.getCaseNameHmctsInternal(caseData))
            .autoListFlag(HearingsDetailsMapping.getAutoListFlag(caseData))
            .hearingType(HearingsDetailsMapping.getHearingDateType(hearingRequest, hearingCollection))
            .hearingEstLengthNumType(HearingsDetailsMapping.getHearingEstLengthNumType(caseData))
            .duration(HearingsDetailsMapping.getHearingDuration(caseDetails, hearingRequest, hearingCollection))
            .hearingPriorityType(HearingsDetailsMapping.getHearingPriorityType())
            .numberOfPhysicalAttendees(HearingsDetailsMapping.getNumberOfPhysicalAttendees())
            .hearingInWelshFlag(HearingsDetailsMapping.isHearingInWelshFlag())
            .caseAdditionalSecurityFlag(HearingsCaseMapping.getCaseAdditionalSecurityFlag(caseData))
            .privateHearingRequiredFlag(HearingsDetailsMapping.isPrivateHearingRequiredFlag(caseData))
            .leadJudgeContractType(HearingsDetailsMapping.getLeadJudgeContractType())
            .hearingIsLinkedFlag(HearingsDetailsMapping.isHearingIsLinkedFlag(caseData))
            .hmctsServiceId(referenceDataServiceHolder.getHmctsServiceId())
            .caseInterpreterRequiredFlag(HearingsCaseMapping.getCaseInterpreterRequiredFlag(caseData))
            .tribunalAndOfficeLocation(HearingsDetailsMapping.getTribunalAndOfficeLocation())
            .build();
    }
}
