package uk.gov.hmcts.reform.et.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.et.exception.GetHearingException;
import uk.gov.hmcts.reform.et.model.hearing.HearingGetResponse;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.TokenResponse;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class HmcHearingApiService {

    public final HmcHearingApi hmcHearingApi;

    public final IdamClient idamClient;

    @Value("${etcos.system.username}")
    private String systemUserName;

    @Value("${etcos.system.password}")
    private String systemUserPassword;

    public HearingGetResponse getHearingRequest(String hearingId) throws GetHearingException {
        log.debug("Sending Get Hearing Request for Hearing ID {}", hearingId);
        HearingGetResponse hearingResponse = hmcHearingApi.getHearingRequest(
                getIdamTokens().accessToken,
                getIdamTokens().idToken,
                hearingId,
            null
        );
        if (isNull(hearingResponse)) {
            throw new GetHearingException(String.format("Failed to retrieve hearing with Id: %s from HMC", hearingId));
        }
        return hearingResponse;
    }

    private TokenResponse getIdamTokens() {
        return idamClient.getAccessTokenResponse(systemUserName, systemUserPassword);
    }

}


