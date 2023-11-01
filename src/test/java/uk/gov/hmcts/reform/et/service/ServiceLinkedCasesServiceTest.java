package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.ListTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.CaseLink;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.helper.mapping.CaseDataMapping;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class ServiceLinkedCasesServiceTest {

    @Mock
    private CaseService caseService;

    @InjectMocks
    private ServiceLinkedCasesService serviceLinkedCasesService;

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
    void serviceLinkedCasesServiceTest() throws IOException, URISyntaxException, GetCaseException {
        CaseDetails mockCaseDetails = new CaseTestData().expectedDetails();
        CaseData caseData = CaseDataMapping.mapCaseData(mockCaseDetails.getData());

        when(authTokenGenerator.generate()).thenReturn("serviceAuthS2s");
        when(ccdApiClient.getCase(
                TEST_SERVICE_AUTH_TOKEN,
                "serviceAuthS2s",
                MOCK_CASE_ID
        )).thenReturn(mockCaseDetails);

        try (MockedStatic<CaseDataMapping> caseDataMapperMock = mockStatic(CaseDataMapping.class)) {
            caseDataMapperMock.when(() -> CaseDataMapping.mapCaseData(
                    mockCaseDetails.getData())).thenReturn(caseData);
        }

        String caseId = MOCK_CASE_ID;
        String authorization = "authorization";
        when(caseService.retrieveCase(authorization, caseId)).thenReturn(mockCaseDetails);
        ServiceHearingRequest request = new ServiceHearingRequest(caseId, MOCK_REQUEST_HEARING_ID);
        ListTypeItem<CaseLink> result = serviceLinkedCasesService.getServiceLinkedCases(authorization, request);

        assertEquals(0, result.size());
    }
}



