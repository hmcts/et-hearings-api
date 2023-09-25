package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.et.exception.GetHearingException;
import uk.gov.hmcts.reform.et.model.hearing.HearingGetResponse;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.TokenResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class HmcHearingApiServiceTest {

    private HmcHearingApiService service;

    @Mock
    private HmcHearingApi hmcHearingApi;

    @Mock
    private IdamClient idamClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new HmcHearingApiService(hmcHearingApi, idamClient);
    }

    @Test
    void getHearingRequest_shouldReturnHearingResponse_whenApiReturnsResponse() throws GetHearingException {
        // Arrange
        String hearingId = "123";
        String accessToken = "access_token";
        String idToken = "id_token";
        HearingGetResponse expectedResponse = new HearingGetResponse();
        String expiresIn = null;
        String refreshToken = null;
        String scope = null;
        String tokenType = null;
        when(idamClient.getAccessTokenResponse(anyString(), anyString())).thenReturn(new TokenResponse(accessToken, idToken, expiresIn, refreshToken, scope, tokenType));
        when(hmcHearingApi.getHearingRequest(accessToken, idToken, hearingId, null)).thenReturn(expectedResponse);

        // Act
        HearingGetResponse actualResponse = service.getHearingRequest(hearingId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getHearingRequest_shouldThrowException_whenApiReturnsNull() {
        // Arrange
        String hearingId = "123";
        String accessToken = "access_token";
        String idToken = "id_token";
        String expiresIn = null;
        String refreshToken = null;
        String scope = null;
        String tokenType = null;
        when(idamClient.getAccessTokenResponse(anyString(), anyString())).thenReturn(new TokenResponse(accessToken, idToken, expiresIn, refreshToken, scope, tokenType));
        when(hmcHearingApi.getHearingRequest(accessToken, idToken, hearingId, null)).thenReturn(null);

        // Act & Assert
        assertThrows(GetHearingException.class, () -> service.getHearingRequest(hearingId));
    }
}
