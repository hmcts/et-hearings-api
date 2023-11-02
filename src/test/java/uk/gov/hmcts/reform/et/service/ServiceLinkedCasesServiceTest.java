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

    public static final String MOCK_CASE_ID = "1646225213651590";
    public static final String MOCK_REQUEST_HEARING_ID = "c73bcbc4-430e-41c9-9790-182543914c0c";

    CaseDetails mockCaseDetails;
    String caseId = MOCK_CASE_ID;
    String authorization = "authorization";

    @BeforeEach
    public void setUp() throws IOException, URISyntaxException, GetCaseException {
        MockitoAnnotations.openMocks(this);

        mockCaseDetails = new CaseTestData().expectedDetails();
        CaseData caseData = CaseDataMapping.mapCaseData(mockCaseDetails.getData());

        try (MockedStatic<CaseDataMapping> caseDataMapperMock = mockStatic(CaseDataMapping.class)) {
            caseDataMapperMock.when(() -> CaseDataMapping.mapCaseData(mockCaseDetails.getData())).thenReturn(caseData);
        }

        when(caseService.retrieveCase(authorization, caseId)).thenReturn(mockCaseDetails);
    }

    @Test
    void getServiceLinkedCases_noLinks() throws GetCaseException {
        ServiceHearingRequest request = new ServiceHearingRequest(caseId, MOCK_REQUEST_HEARING_ID);
        ListTypeItem<CaseLink> result = serviceLinkedCasesService.getServiceLinkedCases(authorization, request);

        assertEquals(0, result.size());
    }

    @Test
    void getServiceLinkedCases_withLinks() throws GetCaseException {
        mockCaseDetails.getData().put("caseLinks", ListTypeItem.from(CaseLink.builder()
                .caseReference("1698860164619605")
                .caseType("ET_Scotland")
                .build()
        ));

        ServiceHearingRequest request = new ServiceHearingRequest(caseId, MOCK_REQUEST_HEARING_ID);
        ListTypeItem<CaseLink> result = serviceLinkedCasesService.getServiceLinkedCases(authorization, request);

        assertEquals(1, result.size());
    }
}



