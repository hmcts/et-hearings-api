package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.HearingTypeItem;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.helper.mapping.EmployeeObjectMapper;
import uk.gov.hmcts.reform.et.helper.mapping.ServiceHearingValuesMapping;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    private ReferenceDataServiceHolder referenceDataServiceHolder;

    @Mock
    private CoreCaseDataApi ccdApiClient;

    private static final String TEST_SERVICE_AUTH_TOKEN = "Bearer TestServiceAuth";
    private static final String MOCK_CASE_ID = "1646225213651590";
    private static final String MOCK_REQUEST_HEARING_ID = "c73bcbc4-430e-41c9-9790-182543914c0c";

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
        CaseData caseData = EmployeeObjectMapper.mapRequestCaseDataToCaseData(mockCaseDetails.getData());
        List<HearingTypeItem> mockHearingCollection = new CaseTestData().getHearingCollectionAsList();
        String hearingId = EmployeeObjectMapper.mapServiceHearingRequestDataToCaseData(request.getHearingId());
        ServiceHearingValues mockServiceHearingValues = new CaseTestData().expectedServiceHearingValues();

        when(authTokenGenerator.generate()).thenReturn("serviceAuthS2s");
        when(ccdApiClient.getCase(
                TEST_SERVICE_AUTH_TOKEN,
                "serviceAuthS2s",
                MOCK_CASE_ID
        )).thenReturn(mockCaseDetails);

        try (MockedStatic<EmployeeObjectMapper> employeeObjectMapperMock = mockStatic(EmployeeObjectMapper.class)) {
            employeeObjectMapperMock.when(() -> EmployeeObjectMapper.mapRequestCaseDataToCaseData(
                    mockCaseDetails.getData())).thenReturn(caseData);
            employeeObjectMapperMock.when(() -> EmployeeObjectMapper.mapHearingCollectionDataToCaseData(any()))
                    .thenReturn(mockHearingCollection);
            employeeObjectMapperMock.when(() -> EmployeeObjectMapper.mapServiceHearingRequestDataToCaseData(
                            request.getHearingId()))
                    .thenReturn(hearingId);
        }

        try (MockedStatic<ServiceHearingValuesMapping> mappingMock = mockStatic(ServiceHearingValuesMapping.class)) {
            mappingMock.when(() -> ServiceHearingValuesMapping.mapServiceHearingValues(
                            mockCaseDetails,
                            caseData,
                            hearingId,
                            mockHearingCollection,
                            referenceDataServiceHolder))
                    .thenReturn(mockServiceHearingValues);
            when(caseService.retrieveCase(authorization, caseId)).thenReturn(mockCaseDetails);
            ServiceHearingValues result = serviceHearingsService.getServiceHearingValues(authorization, request);

            assertEquals(mockServiceHearingValues, result, "Service hearing values do not match.");
        }
    }
}



