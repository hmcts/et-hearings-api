package uk.gov.hmcts.reform.et.helper.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.et.common.model.ccd.CaseData;
import uk.gov.hmcts.et.common.model.hmc.CaseCategory;
import uk.gov.hmcts.et.common.model.hmc.Navigation;
import uk.gov.hmcts.et.common.model.hmc.ScreenNavigation;
import uk.gov.hmcts.et.common.model.hmc.Vocabulary;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.et.model.service.ReferenceDataServiceHolder.DEFAULT_CATEGORY;
import static uk.gov.hmcts.reform.et.model.service.YesNo.isYes;

@RestController
@Slf4j
public final class HearingsCaseMapping {

    private HearingsCaseMapping() {
    }

    public static String getCaseDeepLink(CaseData caseData) {
        if (caseData.getCaseDeepLink() == null) {
            return "https://www.google.com/FOR_TESTING_ONLY";
        }
        return caseData.getCaseDeepLink();
    }

    public static String getCaseNameHmctsInternal(CaseData caseData) {
        return caseData.getCaseNameHmctsInternal();
    }

    public static String getPublicCaseName(CaseData caseData) {
        if (caseData.getPublicCaseName() == null) {
            return "TODO";
        }
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

    public static List<CaseCategory> getCaseCategories() {

        CaseCategory caseCategory = new CaseCategory();
        caseCategory.setCategoryType(DEFAULT_CATEGORY);
        caseCategory.setCategoryValue(DEFAULT_CATEGORY);
        caseCategory.setCategoryParent(DEFAULT_CATEGORY);

        List<CaseCategory> resultList = new ArrayList<>();
        resultList.add(caseCategory);

        return resultList;
    }

    public static List<Vocabulary> getVocabulary() {
        return new ArrayList<>();
    }

    public static List<ScreenNavigation> getScreenFlow() {
        return List.of(
                buildScreenNavigation("hearing-requirements", "hearing-facilities"),
                buildScreenNavigation("hearing-facilities", "hearing-stage"),
                buildScreenNavigation("hearing-stage", "hearing-attendance"),
                buildScreenNavigation("hearing-attendance", "hearing-venue"),
                buildScreenNavigation("hearing-venue", "hearing-judge"),
                buildScreenNavigation("hearing-judge", "hearing-timing"),
                buildScreenNavigation("hearing-timing", "hearing-link"),
                buildScreenNavigation("hearing-link", "hearing-additional-instructions"),
                buildScreenNavigation("hearing-additional-instructions", "hearing-create-edit-summary")
        );
    }

    private static ScreenNavigation buildScreenNavigation(String name, String nextName) {
        return ScreenNavigation.builder()
                .navigation(List.of(Navigation.builder().resultValue(nextName).build()))
                .screenName(name)
                .build();
    }
}
