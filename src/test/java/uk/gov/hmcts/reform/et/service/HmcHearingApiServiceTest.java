package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import uk.gov.hmcts.reform.et.model.hearing.HearingGetResponse;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.TokenResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class HmcHearingApiServiceTest {

    @Mock
    private HmcHearingApi hmcHearingApi;

    @Mock
    IdamClient idamClient;

    @InjectMocks
    private HmcHearingApiService hmcHearingApiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        hmcHearingApiService = new HmcHearingApiService(hmcHearingApi, idamClient);
    }


    @Test
    public void testGetHearingRequest() throws Exception {
        String hearingId = "12345";
        TokenResponse tokenResponse = new TokenResponse("access_token", "id_token", "", "","","");
        HearingGetResponse hearingResponse = new HearingGetResponse();

        // Mock the getIdamTokens method to return a TokenResponse object
        when(hmcHearingApiService.getIdamTokens()).thenReturn(tokenResponse);

        // Mock the getHearingRequest method to return a HearingGetResponse object
        when(hmcHearingApi.getHearingRequest(tokenResponse.accessToken, tokenResponse.idToken, hearingId, null))
            .thenReturn(hearingResponse);

        // Call the getHearingRequest method and verify that it returns the expected HearingGetResponse object
        HearingGetResponse result = hmcHearingApiService.getHearingRequest(hearingId);
        assertEquals(hearingResponse, result);
    }
}
