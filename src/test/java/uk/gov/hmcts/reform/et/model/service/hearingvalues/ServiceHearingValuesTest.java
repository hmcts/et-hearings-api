package uk.gov.hmcts.reform.et.model.service.hearingvalues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.hmc.CaseFlags;
import uk.gov.hmcts.et.common.model.hmc.HearingLocation;
import uk.gov.hmcts.et.common.model.hmc.HearingWindow;
import uk.gov.hmcts.et.common.model.hmc.IndividualDetails;
import uk.gov.hmcts.et.common.model.hmc.Judiciary;
import uk.gov.hmcts.et.common.model.hmc.Navigation;
import uk.gov.hmcts.et.common.model.hmc.PanelComposition;
import uk.gov.hmcts.et.common.model.hmc.PanelPreference;
import uk.gov.hmcts.et.common.model.hmc.PanelRequirements;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;
import uk.gov.hmcts.et.common.model.hmc.PartyFlags;
import uk.gov.hmcts.et.common.model.hmc.ScreenNavigation;
import uk.gov.hmcts.et.common.model.hmc.Vocabulary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ServiceHearingValuesTest {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void shouldSerializeToPoJoSuccessfully() throws Exception {

        ServiceHearingValues serviceHearingValues =  ServiceHearingValues.builder()
            .hmctsServiceID("BHA1")
            .hmctsInternalCaseName("Johnny Claimant v Acme Redde Ltd")
            .publicCaseName("Johnny Claimant v Acme Redde Ltd")
            .caseAdditionalSecurityFlag(false)
            .caseCategories(null)
            .caseDeepLink("/documents/deep/link")
            .caseRestrictedFlag(false)
            .externalCaseReference("abc123")
            .caseManagementLocationCode("location123")
            .caseSlaStartDate("2024-04-23T18:25:43.511Z")
            .autoListFlag(false)
            .hearingType("BHA1-SUB")
            .hearingWindow(HearingWindow.builder()
                               .firstDateTimeMustBe(LocalDateTime.of(2023, 10, 28, 14, 33, 48, 6))
                               .dateRangeEnd(LocalDate.of(2023, 10, 28))
                               .dateRangeStart(LocalDate.of(2023, 10, 28)).build())
            .duration(45)
            .hearingPriorityType("standard")
            .numberOfPhysicalAttendees(2)
            .hearingInWelshFlag(false)
            .hearingLocations(
                List.of(HearingLocation.builder()
                    .locationId("123456abcdef")
                    .locationType("type").build())
            )
            .facilitiesRequired(
                List.of("room with windows")
            )
            .listingComments("Additional instructions")
            .hearingRequester("Judge")
            .privateHearingRequiredFlag(false)
            .caseInterpreterRequiredFlag(false)
            .panelRequirements(
                PanelRequirements.builder()
                    .roleType(List.of("tribunalJudge", "deputyTribunalJudge", "regionalTribunalJudge"))
                    .panelSpecialisms(List.of("DisabilityQualifiedPanelMember",
                                              "EyeSurgeon",
                                              "GeneralPractitioner",
                                              "FinanciallyQualifiedPanelMember",
                                              "RegionalMedicalMember"))
                    .build())
            .leadJudgeContractType("")
            .judiciary(Judiciary.builder()
                           .roleType(List.of(""))
                           .authorisationTypes(List.of(""))
                           .authorisationSubType(List.of(""))
                           .judiciaryPreferences(List.of(
                               PanelPreference.builder()
                                   .memberID("p100")
                                   .memberType("JUDGE")
                                   .requirementType("EXCLUDE").build()
                           ))
                           .judiciarySpecialisms(List.of(""))
                           .panelComposition(
                               List.of(
                                   PanelComposition.builder()
                                       .memberType("Case worker").count(2)
                                           .build())
                           )
                           .build())
            .hearingIsLinkedFlag(false)
            .parties(List.of(PartyDetails.builder()
                                 .partyID("P1")
                                 .partyType("IND")
                                 .partyName("Jane Smith")
                                 .individualDetails(IndividualDetails.builder()
                                                        .firstName("Jane")
                                                        .lastName("Smith")
                                                        .vulnerableFlag(false)
                                                        .build())
                                 .unavailabilityRanges(null)
                                 .build()))
            .caseFlags(CaseFlags
                           .builder()
                           .flags(List.of(
                               PartyFlags.builder()
                                   .partyName("Jane Smith")
                                   .flagParentId("RA0008")
                                   .flagDescription("Sign language interpreter required")
                                   .flagStatus("ACTIVE")
                                   .build(),
                               PartyFlags.builder()
                                   .partyName("Jane dOE")
                                   .flagParentId("RA0038")
                                   .flagDescription("Larger font size")
                                   .flagStatus("ACTIVE")
                                   .build()
                           ))
                           .flagAmendUrl("/flag/amend")
                           .build())
            .screenFlow(List.of(
                ScreenNavigation.builder()
                    .screenName("hearing-requirements")
                    .navigation(List.of(Navigation.builder()
                                            .resultValue("hearing-facilities")
                                            .build()))
                    .build(),
                ScreenNavigation.builder()
                    .screenName("hearing-facilities")
                    .navigation(List.of(Navigation.builder()
                                            .resultValue("hearing-stage")
                                            .build()))
                    .build(),
                ScreenNavigation.builder()
                    .screenName("hearing-stage")
                    .navigation(List.of(Navigation.builder()
                                            .resultValue("hearing-attendance")
                                            .build()))
                    .build(),
                ScreenNavigation.builder()
                    .screenName("hearing-attendance")
                    .navigation(List.of(Navigation.builder()
                                            .resultValue("hearing-venue")
                                            .build()))
                    .build()
            ))
            .vocabulary(List.of(Vocabulary.builder().word1("word up").build()))
            .hearingChannels(List.of("abc"))
            .caseType("case type")
            .hearingLevelParticipantAttendance(List.of("abc"))
            .build();

        String jsonServiceHearingValues = MAPPER.writeValueAsString(serviceHearingValues);
        //assert all values are preserved after serializing/deserializing ignoring node order
        assertThat(MAPPER.readValue(jsonServiceHearingValues, ServiceHearingValues.class))
            .isEqualTo(serviceHearingValues);

    }

}
