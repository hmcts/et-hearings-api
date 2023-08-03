package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

    @InjectMocks
    private CaseService caseService;

//    @Test
//    void retrieveCaseShouldReturnCaseDetails() throws GetCaseException {
//
//        CaseDetails expectedCaseDetails = new CaseDetails();
//        CaseDetails caseDetails = caseService.retrieveCase("auth", "caseId");
//
//        assertThat(expectedCaseDetails).isEqualTo(caseDetails);
//    }

//    @Test
//    void retrieveCaseShouldThrowGetCaseExceptionWhenNoCaseFound() throws GetCaseException {
//
//        assertThatExceptionOfType(GetCaseException.class).isThrownBy(
//            () -> caseService.retrieveCase("auth",""));
//    }
}
