package uk.gov.hmcts.reform.et.model.hmc.reference;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntityRoleCode {
    CLAIMANT("CLAI", "Claimant"),
    RESPONDENT("RESP", "Respondent"),
    LEGAL_REPRESENTATIVE("LGRP", "Legal Representative");

    @JsonValue
    private final String hmcReference;
    private final String parentRole;

}
