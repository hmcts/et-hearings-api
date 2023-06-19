package uk.gov.hmcts.reform.et.model.service.hearingvalues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.et.utils.ResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;


class ServiceHearingValuesTest {

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void shouldSerializeToPoJoSuccessfully() throws Exception {

        String actualJson = ResourceLoader.loadJson("serviceHearingValues.json");
        ServiceHearingValues serializedObject = MAPPER.readValue(actualJson, ServiceHearingValues.class);
        String deserializedJson = MAPPER.writeValueAsString(serializedObject);

        // assert all values are preserved after serializing/deserializing ignoring node order
        assertThat(MAPPER.readValue(deserializedJson, ServiceHearingValues.class)).isEqualTo(serializedObject);
    }

    @Test
    void shouldHaveCorrectParameters() throws Exception {

        String actualJson = ResourceLoader.loadJson("serviceHearingValues.json");
        ServiceHearingValues serializedObject = MAPPER.readValue(actualJson, ServiceHearingValues.class);

        assertFalse(serializedObject.isAutoListFlag(),"auto list flag");
        assertThat(serializedObject.getPublicCaseName()).isEqualTo(" Johnny Claimant v Acme Redde Ltd");
        assertThat(serializedObject.getCaseDeepLink()).isEqualTo("/documents/deep/link");
        assertThat(serializedObject.getHmctsInternalCaseName()).isEqualTo("Johnny Claimant v Acme Redde Ltd");
        assertThat(serializedObject.getCaseManagementLocationCode()).isEqualTo("location123");

    }

}
