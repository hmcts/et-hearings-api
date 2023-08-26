package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.et.model.service.YesNo;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.YES;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.NO;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YesNoTest {

    @Test
    void testYesNoEnumValues() {
        assertEquals(YES, YesNo.YES.getValue());
        assertEquals(NO, YesNo.NO.getValue());
    }

    @Test
    void testIsYesForEnum() {
        assertTrue(YesNo.isYes(YesNo.YES));
        assertFalse(YesNo.isYes(YesNo.NO));
    }

    @Test
    void testIsYesForStringValue() {
        assertTrue(YesNo.isYes(YES));
        assertTrue(YesNo.isYes(YES));
        assertFalse(YesNo.isYes(NO));
        assertFalse(YesNo.isYes((String) null));
    }

    private static boolean isYesEnum(YesNo yesNo) {
        return YesNo.isYes(yesNo);
    }

    @Test
    void testIsYesEnumMethod() {
        assertTrue(isYesEnum(YesNo.YES));
        assertFalse(isYesEnum(YesNo.NO));
    }
}
