package uk.gov.hmcts.reform.et.helper.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.hmc.HearingLocation;
import uk.gov.hmcts.et.common.model.hmc.HearingWindow;

import java.util.ArrayList;
import java.util.List;

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

    public static Integer getHearingDuration() {
        return 0;
    }

    public static String getHearingType() {
        return "";
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
        if (caseData.getLeadJudgeContractType() == null) {
            return "";
        }
        return caseData.getLeadJudgeContractType();
    }

    public static Boolean isHearingIsLinkedFlag(CaseData caseData) {
        return isYes(caseData.getHearingIsLinkedFlag());
    }

    public static String getTribunalAndOfficeLocation() {
        return "";
    }

    public static HearingWindow getHearingWindow() {
        return new HearingWindow();
    }

    public static List<HearingLocation> getHearingLocation() {
        return new ArrayList<>();
    }
}



