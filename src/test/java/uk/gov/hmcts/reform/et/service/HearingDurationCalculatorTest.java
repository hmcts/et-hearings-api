package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.HearingType;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.HearingDurationCalculator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HearingDurationCalculatorTest {

    public static final String HEARING_REQUEST_ID = "hearing_request_id";
    public static final String DAYS = "Days";
    public static final String HOURS = "Hours";
    public static final String MINUTES = "Minutes";

    @Test
    void calculateHearingDurationDays() throws IOException, URISyntaxException {
        final CaseDetails caseDetails = new CaseTestData().expectedDetails();
        final List<HearingTypeItem> hearingCollection = new ArrayList<>();

        final HearingTypeItem hearingItem = new HearingTypeItem();
        hearingItem.setId(HEARING_REQUEST_ID);

        final HearingType hearingValue = new HearingType();
        hearingValue.setHearingEstLengthNum("2");
        hearingValue.setHearingEstLengthNumType(DAYS);
        hearingItem.setValue(hearingValue);
        hearingCollection.add(hearingItem);

        final int result = HearingDurationCalculator.calculateHearingDuration(
                caseDetails, HEARING_REQUEST_ID, hearingCollection);

        assertEquals(2 * 360, result, "Expected hearing duration in days");
    }

    @Test
    void calculateHearingDurationHours() throws IOException, URISyntaxException {
        final CaseDetails caseDetails = new CaseTestData().expectedDetails();
        final List<HearingTypeItem> hearingCollection = new ArrayList<>();

        final HearingTypeItem hearingItem = new HearingTypeItem();
        hearingItem.setId(HEARING_REQUEST_ID);

        HearingType hearingValue = new HearingType();
        hearingValue.setHearingEstLengthNum("3");
        hearingValue.setHearingEstLengthNumType(HOURS);
        hearingItem.setValue(hearingValue);
        hearingCollection.add(hearingItem);

        final int result = HearingDurationCalculator.calculateHearingDuration(
                caseDetails, HEARING_REQUEST_ID, hearingCollection);

        assertEquals(3 * 60, result, "Expected hearing duration in hours");
    }

    @Test
    void calculateHearingDurationMinutes() throws IOException, URISyntaxException {
        final CaseDetails caseDetails = new CaseTestData().expectedDetails();
        final List<HearingTypeItem> hearingCollection = new ArrayList<>();

        final HearingTypeItem hearingItem = new HearingTypeItem();
        hearingItem.setId(HEARING_REQUEST_ID);

        HearingType hearingValue = new HearingType();
        hearingValue.setHearingEstLengthNum("30");
        hearingValue.setHearingEstLengthNumType(MINUTES);
        hearingItem.setValue(hearingValue);
        hearingCollection.add(hearingItem);

        final int result = HearingDurationCalculator.calculateHearingDuration(
                caseDetails, HEARING_REQUEST_ID, hearingCollection);

        assertEquals(30, result, "Expected hearing duration in minutes");
    }

    @Test
    void calculateHearingDurationInvalidType() throws IOException, URISyntaxException {
        final CaseDetails caseDetails = new CaseTestData().expectedDetails();
        final List<HearingTypeItem> hearingCollection = new ArrayList<>();

        final HearingTypeItem hearingItem = new HearingTypeItem();
        hearingItem.setId(HEARING_REQUEST_ID);

        HearingType hearingValue = new HearingType();
        hearingValue.setHearingEstLengthNum("InvalidType");
        hearingValue.setHearingEstLengthNumType("5");
        hearingItem.setValue(hearingValue);
        hearingCollection.add(hearingItem);

        assertThrows(NumberFormatException.class,
                () -> HearingDurationCalculator.calculateHearingDuration(
                        caseDetails, HEARING_REQUEST_ID, hearingCollection),
                "Expected NumberFormatException for invalid hearing length");
    }

    @Test
    void calculateHearingDurationNoMatch() throws IOException, URISyntaxException {
        final CaseDetails caseDetails = new CaseTestData().expectedDetails();
        final String hearingRequestId = "";
        final List<HearingTypeItem> hearingCollection = new ArrayList<>();

        final HearingTypeItem hearingItem = new HearingTypeItem();
        hearingItem.setId(HEARING_REQUEST_ID);

        HearingType hearingValue = new HearingType();
        hearingValue.setHearingEstLengthNum("2");
        hearingValue.setHearingEstLengthNumType(DAYS);
        hearingItem.setValue(hearingValue);
        hearingCollection.add(hearingItem);

        assertThrows(IllegalArgumentException.class,
                () -> HearingDurationCalculator.calculateHearingDuration(
                        caseDetails, hearingRequestId, hearingCollection),
                "Expected IllegalArgumentException for no match");
    }
}
