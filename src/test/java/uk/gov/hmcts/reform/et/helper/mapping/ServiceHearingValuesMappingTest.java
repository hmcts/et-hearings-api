package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;


class ServiceHearingValuesMappingTest {

    @Test
    void shouldReturnServiceHearingValues() throws GetCaseException {
        CaseDetails caseDetails = CaseDetails.builder()
            .caseTypeId("ET_England")
            .id(123_456_789L)
            .build();

        ServiceHearingValues serviceHearingValues = ServiceHearingValuesMapping.mapServiceHearingValues(caseDetails);
        assertEquals(serviceHearingValues.getCaseType(),"ET_England", "case type");
        assertFalse(serviceHearingValues.isAutoListFlag(), "is auto list flag");
        assertNull(serviceHearingValues.getPublicCaseName(), "get public case name");
        assertNull(serviceHearingValues.getCaseDeepLink(),"get case deep link");
        assertNull(serviceHearingValues.getHmctsInternalCaseName(),"hmcts internal case name");
        assertNull(serviceHearingValues.getCaseManagementLocationCode(),"case mgt location code");
        assertNull(serviceHearingValues.getCaseSlaStartDate(),"case SLA start date");
        assertFalse(serviceHearingValues.isCaseRestrictedFlag(),"case restricted flag");
        assertFalse(serviceHearingValues.isAutoListFlag(),"auot list flag");

        assertNull(serviceHearingValues.getHearingType(),"hearing type");
        assertNull(serviceHearingValues.getCaseCategories(),"case categories");
        assertNull(serviceHearingValues.getHearingWindow(),"hearing winodw");
        assertNull(serviceHearingValues.getDuration(),"duration");
        assertNull(serviceHearingValues.getHearingPriorityType(),"hearing priority type");
        assertNull(serviceHearingValues.getNumberOfPhysicalAttendees(),"no. of physical attendees");
        assertFalse(serviceHearingValues.isHearingInWelshFlag(),"hearing in Welsh flag");
        assertNull(serviceHearingValues.getHearingLocations(),"get locations flag");
        assertFalse(serviceHearingValues.getCaseAdditionalSecurityFlag(),"additional security flag");

        assertNull(serviceHearingValues.getFacilitiesRequired(),"facilities required");
        assertNull(serviceHearingValues.getListingComments(),"listing comments");
        assertNull(serviceHearingValues.getHearingRequester(),"hearing requester");
        assertFalse(serviceHearingValues.isPrivateHearingRequiredFlag(),"private hearing required");
        assertNull(serviceHearingValues.getLeadJudgeContractType(),"lead judge contract");
        assertNull(serviceHearingValues.getJudiciary(),"judiciary");
        assertFalse(serviceHearingValues.isHearingIsLinkedFlag(),"is hearing linked");
        assertNull(serviceHearingValues.getParties(),"parties");
        assertNull(serviceHearingValues.getCaseFlags(),"case flags");

        assertNull(serviceHearingValues.getHmctsServiceID(),"HMCTS service id");
        assertNull(serviceHearingValues.getHearingChannels(),"hearing channels");
        assertNull(serviceHearingValues.getScreenFlow(),"screen flow");
        assertNull(serviceHearingValues.getVocabulary(),"vocabulary");

    }
}
