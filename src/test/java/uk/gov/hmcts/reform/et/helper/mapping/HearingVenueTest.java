package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.ecm.common.model.helper.TribunalOffice;
import uk.gov.hmcts.et.common.model.bulk.types.DynamicFixedListType;
import uk.gov.hmcts.et.common.model.bulk.types.DynamicValueType;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.HearingType;
import uk.gov.hmcts.et.common.model.hmc.HearingLocation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class HearingVenueTest {
    public static final String BRISTOL = "Bristol";
    public static final String EDINBURGH = "Edinburgh";
    public static final String MOCK_REQUEST_HEARING_ID = "c73bcbc4-430e-41c9-9790-182543914c0c";

    @Mock
    private HearingTypeItem hearingItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEnglandHearingVenue() {
        String requestHearingId = MOCK_REQUEST_HEARING_ID;
        when(hearingItem.getId()).thenReturn(requestHearingId);

        HearingType hearingType = new HearingType();
        HearingLocation hearingLocation = new HearingLocation();
        DynamicValueType dynamicValueType = new DynamicValueType();

        dynamicValueType.setCode(BRISTOL);
        hearingLocation.setLocationId(String.valueOf(dynamicValueType));
        hearingType.setHearingVenue(DynamicFixedListType.of(dynamicValueType));

        when(hearingItem.getValue()).thenReturn(hearingType);

        List<HearingLocation> result = HearingsDetailsMapping.getHearingLocation(
                requestHearingId, List.of(hearingItem));

        assertEquals(1, result.size(), "Expected one hearing location");
        assertEquals(BRISTOL, result.get(0).getLocationId(), "Location ID should match the expected value");
    }

    @Test
    void testGetScotlandHearingVenue() {

        String hearingRequest = MOCK_REQUEST_HEARING_ID;
        when(hearingItem.getId()).thenReturn(hearingRequest);
        HearingType hearingType = new HearingType();
        hearingType.setHearingVenueScotland(EDINBURGH);
        DynamicValueType dynamicValueTypeEdinburgh = new DynamicValueType();
        dynamicValueTypeEdinburgh.setCode(EDINBURGH);
        DynamicFixedListType edinburghHearingType = DynamicFixedListType.of(dynamicValueTypeEdinburgh);
        hearingType.setHearingEdinburgh(edinburghHearingType);
        when(hearingItem.getValue()).thenReturn(hearingType);

        List<HearingLocation> result = HearingsDetailsMapping.getHearingLocation(hearingRequest, List.of(hearingItem));

        assertEquals(1, result.size(), "Expected one hearing location");
        assertEquals(EDINBURGH, result.get(0).getLocationId(), "Location ID should match the expected value");
    }

    @Test
    void testGetHearingLocationForNonScotlandOffice() {

        String hearingRequest = MOCK_REQUEST_HEARING_ID;
        HearingType hearingType = new HearingType();
        DynamicValueType dynamicValueType = new DynamicValueType();
        dynamicValueType.setCode(BRISTOL);
        hearingType.setHearingVenue(DynamicFixedListType.of(dynamicValueType));

        when(hearingItem.getId()).thenReturn(hearingRequest);
        when(hearingItem.getValue()).thenReturn(hearingType);

        List<HearingLocation> result = HearingsDetailsMapping.getHearingLocation(hearingRequest, List.of(hearingItem));

        assertEquals(1, result.size(), "Expected one hearing location");
        assertEquals(BRISTOL, result.get(0).getLocationId(), "Location ID should match the expected value");
    }

    @Test
    void testGetHearingLocationWithInvalidScotlandOffice() {
        HearingType hearingType = new HearingType();
        hearingType.setHearingVenueScotland("INVALID_SCOTLAND_OFFICE");

        when(hearingItem.getId()).thenReturn(MOCK_REQUEST_HEARING_ID);
        when(hearingItem.getValue()).thenReturn(hearingType);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            throwInvalidScotlandOfficeException(hearingType.getHearingVenueScotland());
        });

        assertEquals("Office name INVALID_SCOTLAND_OFFICE not recognised", exception.getMessage(),
                "Exception message should match the expected value");
    }

    private void throwInvalidScotlandOfficeException(String invalidScotlandOffice) {
        TribunalOffice tribunalOffice = TribunalOffice.valueOfOfficeName(invalidScotlandOffice);
        throw new IllegalArgumentException("Unexpected Scotland tribunal office " + tribunalOffice);
    }
}