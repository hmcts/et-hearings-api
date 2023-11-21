package uk.gov.hmcts.reform.et.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;
import uk.gov.hmcts.reform.et.utils.ResourceLoader;

import java.io.IOException;
import java.net.URISyntaxException;

@Data
public final class CaseTestData {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    public CaseDetails expectedDetails() throws IOException, URISyntaxException {
        return getResource("responses/caseDetails.json", CaseDetails.class);
    }

    public ServiceHearingValues expectedServiceHearingValues() throws IOException, URISyntaxException {
        return getResource("responses/serviceHearingValues.json", ServiceHearingValues.class);
    }

    public <T> T getResource(String location, Class<T> type) throws IOException, URISyntaxException {
        String actualJson = ResourceLoader.loadJson(location);
        return MAPPER.readValue(actualJson, type);
    }
}
