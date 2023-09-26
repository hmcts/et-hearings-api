package uk.gov.hmcts.reform.et.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.et.model.hearing.HearingGetResponse;
import uk.gov.hmcts.reform.idam.client.models.TokenResponse;

@ExtendWith(MockitoExtension.class)
public class HmcHearingApiServiceTest {

    @Mock
    private HmcHearingApi hmcHearingApi;

    @InjectMocks
    private HmcHearingApiService hmcHearingApiService;

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
