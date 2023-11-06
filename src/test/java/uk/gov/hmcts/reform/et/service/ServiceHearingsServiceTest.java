package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.helper.mapping.CaseDataMapping;
import uk.gov.hmcts.reform.et.model.CaseTestData;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;
import uk.gov.hmcts.reform.et.model.service.hearingvalues.ServiceHearingValues;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class ServiceHearingsServiceTest {

    @Mock
    private CaseService caseService;

    @InjectMocks
    private ServiceHearingsService serviceHearingsService;

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
        CaseDetails mockCaseDetails = new CaseTestData().expectedDetails();
        CaseData caseData = CaseDataMapping.mapCaseData(mockCaseDetails.getData());

        try (MockedStatic<CaseDataMapping> caseDataMapperMock = mockStatic(CaseDataMapping.class)) {
            caseDataMapperMock.when(() -> CaseDataMapping.mapCaseData(mockCaseDetails.getData())).thenReturn(caseData);
        }

        ReflectionTestUtils.setField(serviceHearingsService, "hmctsServiceId", "BHA1");
        when(caseService.retrieveCase(authorization, caseId)).thenReturn(mockCaseDetails);
        ServiceHearingRequest request = new ServiceHearingRequest(caseId, MOCK_REQUEST_HEARING_ID);
        ServiceHearingValues actual = serviceHearingsService.getServiceHearingValues(authorization, request);
        ServiceHearingValues expected = new CaseTestData().expectedServiceHearingValues();

        assertEquals(expected.getCaseCategories(), actual.getCaseCategories());
        assertEquals(expected.getCaseFlags(), actual.getCaseFlags());
        assertEquals(expected.getHearingChannels(), actual.getHearingChannels());
        assertEquals(expected.getHearingLocations(), actual.getHearingLocations());
        assertEquals(expected.getHearingWindow(), actual.getHearingWindow());
        assertEquals(expected.getJudiciary(), actual.getJudiciary());
        assertEquals(expected.getParties(), actual.getParties());
        assertEquals(expected.getPanelRequirements(), actual.getPanelRequirements());
        assertEquals(expected.getScreenFlow(), actual.getScreenFlow());
        assertEquals(expected.getVocabulary(), actual.getVocabulary());

        assertEquals(expected, actual);
    }
}



