package uk.gov.hmcts.reform.et.helper.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.Et1CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;

import java.util.List;
import java.util.Map;

/**
 * Converts case data that is stored in a format used in http calls and wraps it in a class for API use.
 */
@Slf4j
@Service
public class EmployeeObjectMapper {

    /**
     * Converts caseData string to {@link Et1CaseData} object.
     *
     * @param caseData which would be in json format
     * @return @link Et1CaseData format of input json object
     */
    public Et1CaseData getEmploymentCaseData(String caseData) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Et1CaseData data = null;
        try {
            data = mapper.readValue(caseData, Et1CaseData.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse the input json request body,", e);
        }
        return data;
    }

    /**
     * Converts caseData to {@link Et1CaseData} object.
     *
     * @param caseData to be converted
     * @return case data wrapped in {@link Et1CaseData} format
     */
    public Et1CaseData getEmploymentCaseData(Map<String, Object> caseData) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.convertValue(caseData, Et1CaseData.class);
    }

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
