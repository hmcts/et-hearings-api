package uk.gov.hmcts.reform.et.helper.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.et.common.model.ccd.CaseData;

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
    public static CaseData mapCaseData(Map<String, Object> caseData) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.convertValue(caseData, CaseData.class);
    }
}
