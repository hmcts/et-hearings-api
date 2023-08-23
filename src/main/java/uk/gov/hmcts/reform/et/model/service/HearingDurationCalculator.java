package uk.gov.hmcts.reform.et.model.service;

import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

import java.util.List;


public class HearingDurationCalculator {

    private HearingDurationCalculator() {
    }

    public static int calculateHearingDuration(
        CaseDetails caseDetails, String hearingRequest, List<HearingTypeItem> hearingCollection) {
        int hearingEstLengthNum;
        String hearingEstLengthNumType;

        for (HearingTypeItem hearingItem : hearingCollection) {
            if (hearingItem.getId().equals(hearingRequest)) {
                hearingEstLengthNum = Integer.parseInt(hearingItem.getValue().getHearingEstLengthNum());
                hearingEstLengthNumType = hearingItem.getValue().getHearingEstLengthNumType();

                return switch (hearingEstLengthNumType) {
                    case "Days" -> hearingEstLengthNum * 360;
                    case "Hours" -> hearingEstLengthNum * 60;
                    case "Minutes" -> hearingEstLengthNum;
                    default -> throw new NumberFormatException(String.format(
                        "Unexpected hearing duration for case id %s: '%s'",
                        caseDetails.getId(),
                        hearingEstLengthNum
                    ));
                };
            }
        }

        // Handle the case when no match is found
        throw new IllegalArgumentException(String.format(
            "No hearing with id '%s' found for case id %s",
            hearingRequest,
            caseDetails.getId()
        ));
    }
}
