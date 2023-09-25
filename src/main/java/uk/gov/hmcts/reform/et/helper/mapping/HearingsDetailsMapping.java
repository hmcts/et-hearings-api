package uk.gov.hmcts.reform.et.helper.mapping;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.ecm.common.model.helper.TribunalOffice;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.et.common.model.hmc.HearingLocation;
import uk.gov.hmcts.et.common.model.hmc.HearingWindow;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.et.model.service.HearingDurationCalculator.calculateHearingDuration;
import static uk.gov.hmcts.reform.et.model.service.YesNo.isYes;


@RestController
@Slf4j
public final class HearingsDetailsMapping {

    public static final String STANDARD_PRIORITY = "Standard";

    public static final String DEFAULT_HEARING_LOCATION_TYPE = "Court";

    private static final int PHYSICAL_ATTENDEES = 0;

    private HearingsDetailsMapping() {
    }

    public static boolean getAutoListFlag(CaseData caseData) {
        return isYes(caseData.getAutoListFlag());
    }

    public static String getHearingPriorityType() {
        // TODO Future Work
        return STANDARD_PRIORITY;
    }

    public static Integer getHearingDuration(
            CaseDetails caseDetails, String hearingRequest, List<HearingTypeItem> hearingCollection) {
        return calculateHearingDuration(caseDetails, hearingRequest, hearingCollection);
    }

    public static String getHearingType(String hearingRequest, List<HearingTypeItem> hearingCollection) {
        String hearingType = null;
        for (HearingTypeItem hearingItem : hearingCollection) {
            if (hearingItem.getId().equals(hearingRequest)) {
                hearingType = hearingItem.getValue().getHearingType();
            }
        }
        return hearingType;
    }

    public static Integer getNumberOfPhysicalAttendees() {
        // TODO Future Work
        return PHYSICAL_ATTENDEES;
    }

    public static boolean isHearingInWelshFlag(CaseData caseData) {
        // TODO Future Work
        return isYes(caseData.getHearingInWelshFlag());
    }

    public static Boolean isPrivateHearingRequiredFlag(CaseData caseData) {
        return isYes(caseData.getPrivateHearingRequiredFlag());
    }

    public static String getLeadJudgeContractType(CaseData caseData) {
        return caseData.getLeadJudgeContractType();
    }

    public static Boolean isHearingIsLinkedFlag(CaseData caseData) {
        return Boolean.parseBoolean(caseData.getHearingIsLinkedFlag());
    }

    public static String getTribunalAndOfficeLocation() {
        return null;
    }

    public static HearingWindow getHearingWindow() {
        return new HearingWindow();
    }

    public static List<HearingLocation> getHearingLocation(
        String hearingRequest,
        List<HearingTypeItem> hearingCollection) {
        String hearingLocation = null;
        for (HearingTypeItem hearingItem : hearingCollection) {
            if (hearingItem.getId().equals(hearingRequest)) {
                if (StringUtils.isNotBlank(hearingItem.getValue().getHearingVenueScotland())) {
                    TribunalOffice tribunalOffice = TribunalOffice.valueOfOfficeName(
                            hearingItem.getValue().getHearingVenueScotland());
                    hearingLocation = switch (tribunalOffice) {
                        case GLASGOW -> hearingItem.getValue().getHearingGlasgow().getSelectedCode();
                        case ABERDEEN -> hearingItem.getValue().getHearingAberdeen().getSelectedCode();
                        case DUNDEE -> hearingItem.getValue().getHearingDundee().getSelectedCode();
                        case EDINBURGH -> hearingItem.getValue().getHearingEdinburgh().getSelectedCode();
                        default ->
                            throw new IllegalStateException("Unexpected Scotland tribunal office " + tribunalOffice);
                    };
                } else {
                    hearingLocation = hearingItem.getValue().getHearingVenue().getSelectedCode();
                }
            }
        }

        List<HearingLocation> hearingLocationList = new ArrayList<>();
        HearingLocation location = new HearingLocation();
        location.setLocationType(DEFAULT_HEARING_LOCATION_TYPE);
        location.setLocationId(hearingLocation);
        hearingLocationList.add(location);

        return hearingLocationList;
    }
}



