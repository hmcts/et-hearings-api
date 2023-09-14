package uk.gov.hmcts.reform.et.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.FlagDetailType;
import uk.gov.hmcts.et.common.model.ccd.items.GenericTypeItem;
import uk.gov.hmcts.et.common.model.ccd.items.ListTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.CaseFlagsType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.CLAIMANT_TITLE;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.RESPONDENT_TITLE;

class CaseFlagsHearingsUtilsTest {

    public static final String BANNING_ORDER_FLAG = "Banning order flag";
    public static final String UNACCEPTABLE_BEHAVIOUR_FLAG = "Unacceptable behaviour flag";
    public static final String WELSH_FORMS_FLAG = "Welsh forms flag";

    private CaseData caseData;

    @BeforeEach
    void setUp() {
        caseData = new CaseData();
    }

    @Test
    void testGetAllActiveRespondentFlags() {
        FlagDetailType respondentFlagDetail = createFlagDetail(BANNING_ORDER_FLAG);
        ListTypeItem<FlagDetailType> respondentFlagList = new ListTypeItem<>();
        GenericTypeItem<FlagDetailType> flagDetail = GenericTypeItem.<FlagDetailType>builder()
            .value(respondentFlagDetail)
            .build();
        respondentFlagList.add(flagDetail);
        CaseFlagsType respondentFlags = new CaseFlagsType(RESPONDENT_TITLE, RESPONDENT_TITLE, respondentFlagList);
        caseData.setRespondentFlags(respondentFlags);

        List<CaseFlagsType> activeFlags = CaseFlagsHearingsUtils.getAllActiveFlags(caseData);

        assertEquals(1, activeFlags.size(), "Expected one active flag");
        assertEquals(respondentFlags, activeFlags.get(0), "Respondent flags do not match the expected values");
    }

    @Test
    void testGetAllActiveMixedFlags() {
        FlagDetailType respondentFlag = createFlagDetail(WELSH_FORMS_FLAG);
        ListTypeItem<FlagDetailType> respondentFlagList = new ListTypeItem<>();
        GenericTypeItem<FlagDetailType> respondentFlagDetail = GenericTypeItem.<FlagDetailType>builder()
            .value(respondentFlag)
            .build();
        respondentFlagList.add(respondentFlagDetail);
        CaseFlagsType respondentFlags = new CaseFlagsType(RESPONDENT_TITLE, RESPONDENT_TITLE, respondentFlagList);

        FlagDetailType claimantFlag = createFlagDetail(UNACCEPTABLE_BEHAVIOUR_FLAG);
        ListTypeItem<FlagDetailType> claimantFlagList = new ListTypeItem<>();
        GenericTypeItem<FlagDetailType> claimantFlagDetail = GenericTypeItem.<FlagDetailType>builder()
            .value(claimantFlag)
            .build();
        claimantFlagList.add(claimantFlagDetail);
        CaseFlagsType claimantFlags = new CaseFlagsType(CLAIMANT_TITLE, CLAIMANT_TITLE, claimantFlagList);

        caseData.setRespondentFlags(respondentFlags);
        caseData.setClaimantFlags(claimantFlags);

        List<CaseFlagsType> activeFlags = CaseFlagsHearingsUtils.getAllActiveFlags(caseData);

        assertEquals(2, activeFlags.size(), "Expected two active flags");
        assertEquals(respondentFlags, activeFlags.get(0), "Respondent flags do not match the expected values");
        assertEquals(claimantFlags, activeFlags.get(1), "Claimant flags do not match the expected values");
    }

    private FlagDetailType createFlagDetail(String flagName) {
        FlagDetailType flagDetail = new FlagDetailType();
        flagDetail.setName(flagName);
        flagDetail.setFlagCode(flagName);
        flagDetail.setStatus("Active");
        return flagDetail;
    }
}

