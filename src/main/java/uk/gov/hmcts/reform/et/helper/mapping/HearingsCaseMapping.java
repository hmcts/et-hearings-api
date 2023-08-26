package uk.gov.hmcts.reform.et.helper.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.et.common.model.ccd.CaseData;

import static uk.gov.hmcts.reform.et.model.service.YesNo.isYes;


@RestController
@Slf4j
public final class HearingsCaseMapping {


    private HearingsCaseMapping() {
    }

    public static String getCaseDeepLink(CaseData caseData) {
        return caseData.getCaseDeepLink();
    }

    public static String getCaseNameHmctsInternal(CaseData caseData) {
        return caseData.getCaseNameHmctsInternal();
    }

    public static String getPublicCaseName(CaseData caseData) {
        return caseData.getPublicCaseName();
    }

    public static Boolean getCaseRestrictedFlag(CaseData caseData) {
        return isYes(caseData.getCaseRestrictedFlag());
    }

    public static Boolean getCaseAdditionalSecurityFlag(CaseData caseData) {
        return isYes(caseData.getCaseAdditionalSecurityFlag());
    }

    public static Boolean getCaseInterpreterRequiredFlag(CaseData caseData) {
        return isYes(caseData.getCaseInterpreterRequiredFlag());
    }

    public static String getCaseCreated(CaseData caseData) {
        return caseData.getReceiptDate();
    }

}
