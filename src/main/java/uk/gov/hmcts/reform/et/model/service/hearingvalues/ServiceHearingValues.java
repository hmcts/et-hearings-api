package uk.gov.hmcts.reform.et.model.service.hearingvalues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.et.common.model.hmc.CaseCategory;
import uk.gov.hmcts.et.common.model.hmc.CaseFlags;
import uk.gov.hmcts.et.common.model.hmc.HearingLocation;
import uk.gov.hmcts.et.common.model.hmc.HearingWindow;
import uk.gov.hmcts.et.common.model.hmc.Judiciary;
import uk.gov.hmcts.et.common.model.hmc.PanelRequirements;
import uk.gov.hmcts.et.common.model.hmc.PartyDetails;
import uk.gov.hmcts.et.common.model.hmc.ScreenNavigation;
import uk.gov.hmcts.et.common.model.hmc.Vocabulary;

import java.util.List;

@SuppressWarnings("PMD.TooManyFields")
@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class ServiceHearingValues {

    private String hmctsServiceID;

    private String hmctsInternalCaseName;

    private String publicCaseName;

    private Boolean caseAdditionalSecurityFlag;

    private List<CaseCategory> caseCategories;

    private String caseDeepLink;

    @JsonProperty("caserestrictedFlag")
    private boolean caseRestrictedFlag;

    private String externalCaseReference;

    private String caseManagementLocationCode;

    @JsonProperty("caseSLAStartDate")
    private String caseSlaStartDate;

    private boolean autoListFlag;

    private String hearingType;

    private HearingWindow hearingWindow;

    private Integer duration;

    private String hearingPriorityType;

    private Integer numberOfPhysicalAttendees;

    private boolean hearingInWelshFlag;

    private List<HearingLocation> hearingLocations;

    private List<String> facilitiesRequired;

    private String listingComments;

    private String hearingRequester;

    private boolean privateHearingRequiredFlag;

    private boolean caseInterpreterRequiredFlag;

    private PanelRequirements panelRequirements;

    private String leadJudgeContractType;

    private Judiciary judiciary;

    private boolean hearingIsLinkedFlag;

    private List<PartyDetails> parties;

    private CaseFlags caseFlags;

    private List<ScreenNavigation> screenFlow;

    private List<Vocabulary> vocabulary;

    private List<String>  hearingChannels;

    private String caseType;

    private List<String> hearingLevelParticipantAttendance;
}
