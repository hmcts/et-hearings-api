package uk.gov.hmcts.reform.et.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;
import uk.gov.hmcts.reform.et.service.ServiceHearingsService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("integration")
class ServiceHearingsControllerTest {

    private static final String CASE_ID = "123456abc";

    private static final String SERVICE_HEARING_VALUES_URL = "/serviceHearingValues";

    private static final String AUTH_TOKEN = "testToken";

    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public ServiceHearingsService serviceHearingsService;

    @DisplayName("When Authorization and Case ID valid "
        + "should return the case name with a with 200 response code")
    @Test
    void testPostRequestServiceHearingValues() throws Exception {

        ServiceHearingRequest request = ServiceHearingRequest.builder()
            .caseId(CASE_ID)
            .build();

        given(serviceHearingsService.getServiceHearingValues(AUTH_TOKEN, request))
            .willReturn(ServiceHearingValues.builder()
                            .build());

        MvcResult result = mockMvc.perform(post(SERVICE_HEARING_VALUES_URL)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN)
                            .content(asJsonString(request)))
            .andDo(print())
            .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat("Json body returned contains correct properties", content, CoreMatchers.allOf(
                       containsString("hmctsServiceID"),
                       containsString("publicCaseName"),
                       containsString("hmctsInternalCaseName")));

    }

    @DisplayName("When Case id not provided or invalid should return a with 404 response code")
    @Test
    void testPostRequestServiceHearingValuesInvalidCase() throws Exception {
        ServiceHearingRequest requestNoId = ServiceHearingRequest.builder()
            .caseId("")
            .build();

        given(serviceHearingsService.getServiceHearingValues(AUTH_TOKEN, requestNoId))
            .willThrow(GetCaseException.class);

        mockMvc.perform(post(SERVICE_HEARING_VALUES_URL)
                            .contentType(APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN)
                            .content(asJsonString(requestNoId)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }
}
