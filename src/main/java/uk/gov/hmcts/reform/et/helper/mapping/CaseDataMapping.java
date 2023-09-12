package uk.gov.hmcts.reform.et.helper.mapping;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.et.common.model.ccd.items.RepresentedTypeRItem;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;

import java.util.List;
import java.util.Map;

/**
 * Converts case data that is stored in a format used in http calls and wraps it in a class for API use.
 */
@Slf4j
@Service
public final class CaseDataMapping {

    private CaseDataMapping() {
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

    private static List<RespondentSumTypeItem> getRespondentData(List<RespondentSumTypeItem> respondentDetails) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, RespondentSumTypeItem.class);
        return mapper.convertValue(respondentDetails, type);
    }

    private static List<RepresentedTypeRItem> getLegalRepresentatives(List<RepresentedTypeRItem> legalReps) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, RepresentedTypeRItem.class);
        return mapper.convertValue(legalReps, type);
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

    public static List<RespondentSumTypeItem> mapRespondentDetailsToCaseData(
            List<RespondentSumTypeItem> respondentDetails) {
        return getRespondentData(respondentDetails);
    }

    public static List<RepresentedTypeRItem> mapLegalRepsToCaseData(List<RepresentedTypeRItem> legalReps) {
        return getLegalRepresentatives(legalReps);
    }
}
