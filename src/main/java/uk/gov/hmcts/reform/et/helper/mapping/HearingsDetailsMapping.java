package uk.gov.hmcts.reform.et.helper.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.types.CaseLocation;
import uk.gov.hmcts.et.common.model.hmc.HearingLocation;
import uk.gov.hmcts.et.common.model.hmc.HearingWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.YES;

@RestController
@Slf4j
public final class HearingsDetailsMapping {
    public static final String STANDARD_PRIORITY = "Standard";

    private static final int PHYSICAL_ATTENDEES = 0;

    private HearingsDetailsMapping() {

    }

    public static boolean getAutoListFlag(CaseData caseData) {
        return YES.equals(caseData.getAutoListFlag());
    }

    public static String getHearingPriorityType() {
        // TODO Future Work
        return STANDARD_PRIORITY;
    }

    public static Integer getNumberOfPhysicalAttendees() {
        // TODO Future Work
        return PHYSICAL_ATTENDEES;
    }

    public static boolean isHearingInWelshFlag(CaseData caseData) {
        // TODO Future Work
        return YES.equals(caseData.getHearingInWelshFlag());
    }

    public static Boolean isPrivateHearingRequiredFlag(CaseData caseData) {
        return YES.equals(caseData.getPrivateHearingRequiredFlag());
    }

    public static String getLeadJudgeContractType(CaseData caseData) {
        return defaultString(caseData.getLeadJudgeContractType(), "");
    }

    public static Boolean isHearingIsLinkedFlag(CaseData caseData) {
        return YES.equals(caseData.getHearingIsLinkedFlag());
    }

    public static String getTribunalAndOfficeLocation(CaseData caseData) {
        return defaultString(caseData.getCaseManagementLocationCode(),
                Optional.ofNullable(caseData.getCaseManagementLocation())
                        .map(CaseLocation::getBaseLocation)
                        .orElse(" ")
        );
    }

    public static HearingWindow getHearingWindow() {
        return new HearingWindow();
    }

    public static List<HearingLocation> getHearingLocation() {
        return new ArrayList<>();
    }
}



