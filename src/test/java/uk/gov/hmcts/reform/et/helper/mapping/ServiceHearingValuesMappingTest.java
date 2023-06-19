package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseDetails;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;


class ServiceHearingValuesMappingTest {
    @Test
    void shouldReturnServiceHearingValues() {
        // given
        CaseDetails caseDetails = new CaseDetails();
        // when
        final ServiceHearingValues serviceHearingValues = ServiceHearingValuesMapping.mapServiceHearingValues(caseDetails);
        // then
        assertFalse(serviceHearingValues.isAutoListFlag());
        assertNull(serviceHearingValues.getPublicCaseName());
        assertNull(serviceHearingValues.getPublicCaseName());
        assertNull(serviceHearingValues.getCaseDeepLink());
        assertNull(serviceHearingValues.getHmctsInternalCaseName());
        assertNull(serviceHearingValues.getCaseManagementLocationCode());
        assertNull(serviceHearingValues.getCaseSlaStartDate());
        assertFalse(serviceHearingValues.isCaseRestrictedFlag());
        assertFalse(serviceHearingValues.isAutoListFlag());

        assertNull(serviceHearingValues.getHearingType());
        assertNull(serviceHearingValues.getCaseType());
        assertNull(serviceHearingValues.getCaseCategories());
        assertNull(serviceHearingValues.getHearingWindow());
        assertNull(serviceHearingValues.getDuration());
        assertNull(serviceHearingValues.getHearingPriorityType());
        assertNull(serviceHearingValues.getNumberOfPhysicalAttendees());
        assertFalse(serviceHearingValues.isHearingInWelshFlag());
        assertNull(serviceHearingValues.getHearingLocations());
        assertFalse(serviceHearingValues.getCaseAdditionalSecurityFlag());

        assertNull(serviceHearingValues.getFacilitiesRequired());
        assertNull(serviceHearingValues.getListingComments());
        assertNull(serviceHearingValues.getHearingRequester());
        assertFalse(serviceHearingValues.isPrivateHearingRequiredFlag());
        assertNull(serviceHearingValues.getLeadJudgeContractType());
        assertNull(serviceHearingValues.getJudiciary());
        assertFalse(serviceHearingValues.isHearingIsLinkedFlag());
        assertNull(serviceHearingValues.getParties());
        assertNull(serviceHearingValues.getCaseFlags());

        assertNull(serviceHearingValues.getHmctsServiceID());
        assertNull(serviceHearingValues.getHearingChannels());
        assertNull(serviceHearingValues.getScreenFlow());
        assertNull(serviceHearingValues.getVocabulary());

    }
}
