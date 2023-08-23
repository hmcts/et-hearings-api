package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.model.CaseTestData;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

    @InjectMocks
    private CaseService caseService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private CoreCaseDataApi ccdApiClient;

    private final CaseTestData caseTestData;

    private static final String TEST_SERVICE_AUTH_TOKEN = "Bearer TestServiceAuth";

    CaseServiceTest() {
        caseTestData = new CaseTestData();
    }

    @Test
    void retrieveCaseShouldReturnCaseDetails() throws GetCaseException, IOException, URISyntaxException {

        when(authTokenGenerator.generate()).thenReturn("serviceAuthS2s");
        when(ccdApiClient.getCase(
            TEST_SERVICE_AUTH_TOKEN,
            "serviceAuthS2s",
            "12345"
        )).thenReturn(caseTestData.expectedDetails());

        CaseDetails actualCaseDetails = caseService.retrieveCase(TEST_SERVICE_AUTH_TOKEN, "12345");

        assertEquals(caseTestData.expectedDetails(), actualCaseDetails, "case details");
    }

    @Test
    void retrieveCaseShouldThrowGetCaseExceptionWhenNoCaseFound() throws GetCaseException {

        assertThatExceptionOfType(GetCaseException.class).isThrownBy(
            () -> caseService.retrieveCase("auth",""));
    }
}
