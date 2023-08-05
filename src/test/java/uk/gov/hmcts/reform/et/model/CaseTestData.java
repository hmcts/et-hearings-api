package uk.gov.hmcts.reform.et.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.utils.ResourceLoader;

import java.io.IOException;
import java.net.URISyntaxException;

@Data
public final class CaseTestData {

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public CaseDetails expectedDetails() throws IOException, URISyntaxException {
        String actualJson = ResourceLoader.loadJson(
            "responses/caseDetails.json"
        );
        CaseDetails serializedObject = mapper.readValue(actualJson, CaseDetails.class);
        return serializedObject;
    }
}
