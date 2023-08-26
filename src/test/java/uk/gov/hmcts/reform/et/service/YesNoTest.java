package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.et.model.service.YesNo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.NO;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.YES;

class YesNoTest {

    @Test
    void testYesNoEnumValues() {
        YesNo yesEnum = YesNo.YES;
        YesNo noEnum = YesNo.NO;

        assertEquals(YES, yesEnum.getValue(), "YES enum value should match expected value");
        assertEquals(NO, noEnum.getValue(), "NO enum value should match expected value");
    }

    @Test
    void testIsYesForEnum() {
        assertTrue(YesNo.isYes(YesNo.YES), "isYes should return true for YES enum");
        assertFalse(YesNo.isYes(YesNo.NO), "isYes should return false for NO enum");
    }

    @Test
    void testIsYesForStringValue() {
        assertTrue(YesNo.isYes(YES), "isYes should return true for YES string");
        assertTrue(YesNo.isYes(YES), "isYes should return true for YES string");
        assertFalse(YesNo.isYes(NO), "isYes should return false for NO string");
        assertFalse(YesNo.isYes((String) null), "isYes should return false for null input");
    }

    private static boolean isYesEnum(YesNo yesNo) {
        return YesNo.isYes(yesNo);
    }

    @Test
    void testIsYesEnumMethod() {
        assertTrue(isYesEnum(YesNo.YES), "isYesEnum should return true for YES enum");
        assertFalse(isYesEnum(YesNo.NO), "isYesEnum should return false for NO enum");
    }
}
