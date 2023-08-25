package uk.gov.hmcts.reform.et.helper.mapping;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;

import java.util.List;
import java.util.Map;

/**
 * Converts case data that is stored in a format used in http calls and wraps it in a class for API use.
 */
@Slf4j
@Service
public final class EmployeeObjectMapper {

    private EmployeeObjectMapper() {
    }

    /**
     * Converts caseData to {@link CaseData} object.
     *
     * @param caseData to be converted
     * @return case data wrapped in {@link CaseData} format
     */

    private static CaseData getCaseData(Map<String, Object> caseData) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.convertValue(caseData, CaseData.class);
    }

    private static String getServiceHearingRequestData(String hearingRequestData) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.convertValue(hearingRequestData, String.class);
    }

    private static List<HearingTypeItem> getHearingCollectionData(List<HearingTypeItem> hearingCollection) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, HearingTypeItem.class);
        return mapper.convertValue(hearingCollection, type);
    }

    /**
     * Converts caseData to {@link CaseData} object.
     *
     * @param caseData to be converted
     * @return case data wrapped in {@link CaseData} format
     */
    public static CaseData mapRequestCaseDataToCaseData(Map<String, Object> caseData) {
        return getCaseData(caseData);
    }

    public static String mapServiceHearingRequestDataToCaseData(String hearingRequest) {
        return getServiceHearingRequestData(hearingRequest);
    }

    public static List<HearingTypeItem> mapHearingCollectionDataToCaseData(List<HearingTypeItem> hearingCollection) {
        return getHearingCollectionData(hearingCollection);
    }
}
