package uk.gov.hmcts.reform.et.helper.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

import java.util.List;

import static uk.gov.hmcts.reform.et.model.service.HearingDurationCalculator.calculateHearingDuration;
import static uk.gov.hmcts.reform.et.model.service.YesNo.isYes;


@RestController
@Slf4j
public final class HearingsDetailsMapping {

    public static final String STANDARD_PRIORITY = "Standard";

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

    public static String getHearingEstLengthNumType(CaseData caseData) {
        return caseData.getHearingEstLengthNumType();
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

    public static String getTribunalAndOfficeLocation(CaseData caseData) {
        return caseData.getTribunalAndOfficeLocation();
    }

}



