package uk.gov.hmcts.reform.et.model.service.hearingvalues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceHearingValuesTest {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void shouldSerializeToPoJoSuccessfully() throws Exception {

        ServiceHearingValues serviceHearingValues = ServiceHearingValues.builder()
                .hmctsServiceId("BHA1")
                .hmctsInternalCaseName("Johnny Claimant v Acme Redde Ltd")
                .publicCaseName("Johnny Claimant v Acme Redde Ltd")
                .caseAdditionalSecurityFlag(false)
                .caseCategories(null)
                .caseDeepLink("/documents/deep/link")
                .caseRestrictedFlag(false)
                .caseSlaStartDate("2024-04-23T18:25:43.511Z")
                .autoListFlag(false)
                .hearingType("BHA1-SUB")
                .duration(45)
                .hearingPriorityType("Standard")
                .numberOfPhysicalAttendees(2)
                .hearingInWelshFlag(false)
                .privateHearingRequiredFlag(false)
                .caseInterpreterRequiredFlag(false)
                .leadJudgeContractType("")
                .hearingIsLinkedFlag(false)
                .caseType("case type")
                .build();

        String jsonServiceHearingValues = MAPPER.writeValueAsString(serviceHearingValues);
        //assert all values are preserved after serializing/deserializing ignoring node order
        assertThat(MAPPER.readValue(jsonServiceHearingValues, ServiceHearingValues.class))
                .isEqualTo(serviceHearingValues);
    }
}
