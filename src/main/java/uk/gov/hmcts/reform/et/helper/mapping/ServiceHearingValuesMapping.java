package uk.gov.hmcts.reform.et.helper.mapping;

import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;


@Slf4j
public final class ServiceHearingValuesMapping {

    private ServiceHearingValuesMapping() {
    }

   // public static ServiceHearingValues mapServiceHearingValues(CaseDetails caseDetails) {
        public static ServiceHearingValues mapServiceHearingValues() {

            log.info("Mapping hearing values for Case id : {}, for use in generating Service Hearing Values");
              //   caseDetails.getId());
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
            .caseAdditionalSecurityFlag(false)
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
