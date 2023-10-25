package uk.gov.hmcts.reform.et.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.et.common.model.ccd.items.ListTypeItem;
import uk.gov.hmcts.et.common.model.ccd.types.CaseLink;
import uk.gov.hmcts.reform.et.exception.GetCaseException;
import uk.gov.hmcts.reform.et.model.service.ServiceHearingRequest;
import uk.gov.hmcts.reform.et.service.ServiceLinkedCasesService;

import static org.springframework.http.ResponseEntity.status;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ServiceLinkedCasesController {

    private final ServiceLinkedCasesService serviceLinkedCasesService;

    @PostMapping("/serviceLinkedCases")
    public ResponseEntity<ListTypeItem<CaseLink>> serviceLinkedCases(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ServiceHearingRequest request
    ) throws GetCaseException {
        try {
            log.info("[ServiceLinkedCases] Retrieving case details using Case id : {}", request.getCaseId());
            ListTypeItem<CaseLink> model = serviceLinkedCasesService.getServiceLinkedCases(authorization, request);
            return status(HttpStatus.OK).body(model);
        } catch (Exception ex) {
            log.error("Error getting linked cases from case id {}, {}", request.getCaseId(), ex.toString());
            throw ex;
        }
    }
}
