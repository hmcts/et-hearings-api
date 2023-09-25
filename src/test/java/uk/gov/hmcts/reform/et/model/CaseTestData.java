package uk.gov.hmcts.reform.et.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.et.common.model.ccd.items.RepresentedTypeRItem;
import uk.gov.hmcts.et.common.model.ccd.items.RespondentSumTypeItem;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;
import uk.gov.hmcts.reform.et.utils.ResourceLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Data
public final class CaseTestData {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    public CaseDetails expectedDetails() throws IOException, URISyntaxException {
        String actualJson = ResourceLoader.loadJson(
                "responses/caseDetails.json"
        );
        return MAPPER.readValue(actualJson, CaseDetails.class);
    }

    public ServiceHearingValues expectedServiceHearingValues() throws IOException, URISyntaxException {
        String actualJson = ResourceLoader.loadJson(
                "responses/serviceHearingValues.json"
        );
        return MAPPER.readValue(actualJson, ServiceHearingValues.class);
    }

    public List<HearingTypeItem> getHearingCollectionAsList() throws IOException, URISyntaxException {
        CaseDetails caseDetails = expectedDetails();
        JsonNode hearingCollectionNode = MAPPER.valueToTree(caseDetails.getData().get("hearingCollection"));
        return MAPPER.convertValue(hearingCollectionNode, new TypeReference<>() {
        });
    }

    public List<RespondentSumTypeItem> getRespondentCollectionAsList() throws IOException, URISyntaxException {
        CaseDetails caseDetails = expectedDetails();
        JsonNode respondentCollectionNode = MAPPER.valueToTree(caseDetails.getData().get("respondentCollection"));
        return MAPPER.convertValue(respondentCollectionNode, new TypeReference<>() {
        });
    }

    public List<RepresentedTypeRItem> getRespondentRepCollectionAsList() throws IOException, URISyntaxException {
        CaseDetails caseDetails = expectedDetails();
        JsonNode respondentRepCollectionNode = MAPPER.valueToTree(caseDetails.getData().get("repCollection"));
        return MAPPER.convertValue(respondentRepCollectionNode, new TypeReference<>() {
        });
    }
}
