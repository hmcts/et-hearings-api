package uk.gov.hmcts.reform.et.helper.mapping;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.ccd.items.FlagDetailType;
import uk.gov.hmcts.et.common.model.ccd.items.GenericTypeItem;
import uk.gov.hmcts.et.common.model.ccd.items.ListTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.CaseFlagsType;
import uk.gov.hmcts.et.common.model.hmc.CaseFlags;
import uk.gov.hmcts.et.common.model.hmc.PartyFlags;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.et.model.CaseTestData;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.CLAIMANT_TITLE;
import static uk.gov.hmcts.ecm.common.model.helper.Constants.RESPONDENT_TITLE;

class CaseFlagsMappingTest {

    public static final String URGENT_FLAG = "Urgent flag";
    public static final String LITIGATION_FRIEND_FLAG = "Litigation friend flag";
    public static final String ACTIVE = "Active";

    @Test
    void testGetCaseFlags() throws IOException, URISyntaxException {
        CaseDetails mockCaseDetails = new CaseTestData().expectedDetails();
        CaseData caseData = CaseDataMapping.mapCaseData(mockCaseDetails.getData());

        CaseFlagsType flag1 = new CaseFlagsType(RESPONDENT_TITLE, RESPONDENT_TITLE, createFlagDetails(URGENT_FLAG));
        CaseFlagsType flag2 = new CaseFlagsType(
                CLAIMANT_TITLE, RESPONDENT_TITLE, createFlagDetails(LITIGATION_FRIEND_FLAG));

        caseData.setRespondentFlags(flag1);
        caseData.setClaimantFlags(flag2);

        CaseFlags caseFlags = CaseFlagsMapping.getCaseFlags(caseData);

        assertEquals(2, caseFlags.getFlags().size(), "The number of flags should be 2");

        PartyFlags partyFlag1 = caseFlags.getFlags().get(0);
        assertEquals(flag1.getPartyName(), partyFlag1.getPartyName(), "Party names should match for flag 1");
        assertEquals("", partyFlag1.getFlagParentId(), "Flag parent ID for flag 1 should be empty");
        assertEquals(flag1.getDetails().get(0).getValue().getFlagCode(), partyFlag1.getFlagId(),
                "Flag IDs should match for flag 1");
        assertEquals(flag1.getDetails().get(0).getValue().getName(), partyFlag1.getFlagDescription(),
                "Flag descriptions should match for flag 1");
        assertEquals(ACTIVE, partyFlag1.getFlagStatus(), "Flag status should be 'Active' for flag 1");

        PartyFlags partyFlag2 = caseFlags.getFlags().get(1);
        assertEquals(flag2.getPartyName(), partyFlag2.getPartyName(), "Party names should match for flag 2");
        assertEquals("", partyFlag2.getFlagParentId(), "Flag parent ID for flag 2 should be empty");
        assertEquals(flag2.getDetails().get(0).getValue().getFlagCode(), partyFlag2.getFlagId(),
                "Flag IDs should match for flag 2");
        assertEquals(flag2.getDetails().get(0).getValue().getName(), partyFlag2.getFlagDescription(),
                "Flag descriptions should match for flag 2");
        assertEquals(ACTIVE, partyFlag2.getFlagStatus(), "Flag status should be 'Active' for flag 2");
    }

    private ListTypeItem<FlagDetailType> createFlagDetails(String flagName) {
        FlagDetailType flagDetail = new FlagDetailType();
        flagDetail.setName(flagName);
        flagDetail.setFlagCode(flagName);
        flagDetail.setStatus(ACTIVE);
        flagDetail.setOtherDescription(flagName);

        GenericTypeItem<FlagDetailType> genericTypeItem = GenericTypeItem.from(flagDetail);

        ListTypeItem<FlagDetailType> flagDetails = new ListTypeItem<>();
        flagDetails.add(genericTypeItem);

        return flagDetails;
    }
}
