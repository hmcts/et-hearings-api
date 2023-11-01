package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.helper.mapping.CaseDataMapping;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class ServiceHearingsServiceTest {

    @Mock
    private CaseService caseService;

    @InjectMocks
    private ServiceHearingsService serviceHearingsService;

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private CoreCaseDataApi ccdApiClient;

    private static final String TEST_SERVICE_AUTH_TOKEN = "Bearer TestServiceAuth";
    public static final String MOCK_CASE_ID = "1646225213651590";
    public static final String MOCK_REQUEST_HEARING_ID = "c73bcbc4-430e-41c9-9790-182543914c0c";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void serviceHearingsServiceTest() throws IOException, URISyntaxException, GetCaseException {
        String authorization = "authorization";
        String caseId = MOCK_CASE_ID;
        ServiceHearingRequest request = new ServiceHearingRequest(caseId, MOCK_REQUEST_HEARING_ID);
        CaseDetails mockCaseDetails = new CaseTestData().expectedDetails();
        CaseData caseData = CaseDataMapping.mapCaseData(mockCaseDetails.getData());

        try (MockedStatic<CaseDataMapping> caseDataMapperMock = mockStatic(CaseDataMapping.class)) {
            caseDataMapperMock.when(() -> CaseDataMapping.mapCaseData(mockCaseDetails.getData()))
                    .thenReturn(caseData);

        }

        when(caseService.retrieveCase(authorization, caseId)).thenReturn(mockCaseDetails);
        ServiceHearingValues result = serviceHearingsService.getServiceHearingValues(authorization, request);

        // assertEquals(mockServiceHearingValues, result, "Service hearing values do not match.");
    }
}



