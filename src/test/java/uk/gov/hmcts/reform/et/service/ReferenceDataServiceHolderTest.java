package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReferenceDataServiceHolderTest {

    public static final String EMPLOYMENT_CATEGORY = "Employment";

    private ReferenceDataServiceHolder holder;

    @BeforeEach
    void setUp() {
        holder = new ReferenceDataServiceHolder();
    }

    @Test
    void testHmctsServiceId() {
        String expectedServiceId = "ServiceId";
        holder.setHmctsServiceId(expectedServiceId);
        String hmctsServiceId = holder.getHmctsServiceId();
        assertEquals(expectedServiceId, hmctsServiceId, "hmctsServiceId should match the expected value");
    }

    @Test
    void testCategoryType() {
        holder.setCategoryType(EMPLOYMENT_CATEGORY);
        String categoryType = holder.getCategoryType();
        assertEquals(EMPLOYMENT_CATEGORY, categoryType, "categoryType should match the default value");
    }

    @Test
    void testCategoryValue() {
        holder.setCategoryValue(EMPLOYMENT_CATEGORY);
        String categoryValue = holder.getCategoryValue();
        assertEquals(EMPLOYMENT_CATEGORY, categoryValue, "categoryValue should match the default value");
    }

    @Test
    void testCategoryParent() {
        holder.setCategoryParent(EMPLOYMENT_CATEGORY);
        String categoryParent = holder.getCategoryParent();
        assertEquals(EMPLOYMENT_CATEGORY, categoryParent, "categoryParent should match the default value");
    }
}
