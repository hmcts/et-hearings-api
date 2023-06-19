package uk.gov.hmcts.reform.et.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.et.common.model.ccd.CaseDetails;
import uk.gov.hmcts.reform.et.exception.GetCaseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

    @InjectMocks
    private CaseService caseService;

    @Test
    void retrieveCaseShouldReturnCaseDetails() throws GetCaseException {

        CaseDetails expectedCaseDetails = new CaseDetails();
        CaseDetails caseDetails = caseService.retrieveCase("auth", "caseId");

        assertThat(expectedCaseDetails).isEqualTo(caseDetails);
    }

    @Test
    void retrieveCaseShouldThrowGetCaseExceptionWhenNoCaseFound() throws GetCaseException {

        assertThatExceptionOfType(GetCaseException.class).isThrownBy(
            () -> caseService.retrieveCase("auth",""));
    }
}
