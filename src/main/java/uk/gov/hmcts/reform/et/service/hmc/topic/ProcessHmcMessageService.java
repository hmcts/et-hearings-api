package uk.gov.hmcts.reform.et.service.hmc.topic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.et.exception.GetHearingException;
import uk.gov.hmcts.reform.et.model.hearing.HearingGetResponse;
import uk.gov.hmcts.reform.et.model.hmc.message.HmcMessage;
import uk.gov.hmcts.reform.et.model.hmc.reference.HmcStatus;
import uk.gov.hmcts.reform.et.service.HmcHearingApiService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessHmcMessageService {

    private final HmcHearingApiService hmcHearingApiService;

    public void processEventMessage(HmcMessage hmcMessage) throws GetHearingException {

        String hearingId = hmcMessage.getHearingId();

        HearingGetResponse hearingResponse = hmcHearingApiService.getHearingRequest(hearingId);

        HmcStatus hmcMessageStatus = hmcMessage.getHearingUpdate().getHmcStatus();

        log.info(
            "Hearing message {} processed for case reference {}", hearingResponse, hmcMessageStatus
        );

    }
}
