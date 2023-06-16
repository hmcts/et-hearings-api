package uk.gov.hmcts.reform.et.helper.mapping;

import uk.gov.hmcts.et.common.model.ccd.CaseDetails;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

public final class ServiceHearingValuesMapping {

    private ServiceHearingValuesMapping() {
    }

    public static ServiceHearingValues mapServiceHearingValues(CaseDetails caseDetails) {

        return ServiceHearingValues.builder()
            .publicCaseName(null)
            .caseDeepLink(null)
            .caseManagementLocationCode(null)
            .caseRestrictedFlag(false)
            .caseSlaStartDate(null)
            .hmctsInternalCaseName(null)
            .autoListFlag(false)
            .hearingType(null)
            .caseType(null)
            .caseCategories(null)
            .hearingWindow(null)
            .duration(null)
            .hearingPriorityType(null)
            .numberOfPhysicalAttendees(null)
            .hearingInWelshFlag(false)
            .hearingLocations(null)
            .caseAdditionalSecurityFlag(null)
            .facilitiesRequired(null)
            .listingComments(null)
            .hearingRequester(null)
            .privateHearingRequiredFlag(false)
            .leadJudgeContractType(null)
            .judiciary(null)
            .hearingIsLinkedFlag(false)
            .parties(null)
            .caseFlags(null)
            .hmctsServiceID(null)
            .hearingChannels(null)
            .screenFlow(null)
            .vocabulary(null)
            .caseInterpreterRequiredFlag(false)
            .build();
    }

}
