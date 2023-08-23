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
    private static final String NULL = "Null";

    private static final boolean HEARING_IN_WELSH_FLAG = false;

    private static final int PHYSICAL_ATTENDEES = 0;

    private static final String LEAD_JUDGE_CONTRACT_TYPE = "Null";


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

    public static String getHearingDateType(String hearingRequest, List<HearingTypeItem> hearingCollection) {
        String hearingDateType = null;
        for (HearingTypeItem hearingItem : hearingCollection) {
            if (hearingItem.getId().equals(hearingRequest)) {
                hearingDateType = hearingItem.getValue().getHearingType();
            }
        }
        return hearingDateType;
    }

    public static Integer getNumberOfPhysicalAttendees() {
        // TODO Future Work
        return PHYSICAL_ATTENDEES;
    }

    public static boolean isHearingInWelshFlag() {
        // TODO Future Work
        return HEARING_IN_WELSH_FLAG;
    }

    public static Boolean isPrivateHearingRequiredFlag(CaseData caseData) {
        return isYes(caseData.getPrivateHearingRequiredFlag());
    }

    public static String getLeadJudgeContractType() {
        // TODO Future Work
        return LEAD_JUDGE_CONTRACT_TYPE;
    }

    public static Boolean isHearingIsLinkedFlag(CaseData caseData) {
        return Boolean.parseBoolean(caseData.getHearingIsLinkedFlag());
    }

    public static String getTribunalAndOfficeLocation() {
        return NULL;
    }

}



