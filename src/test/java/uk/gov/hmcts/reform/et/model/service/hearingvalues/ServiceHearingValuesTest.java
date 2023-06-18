package uk.gov.hmcts.reform.et.model.service.hearingvalues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.et.utils.ResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;


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

}
