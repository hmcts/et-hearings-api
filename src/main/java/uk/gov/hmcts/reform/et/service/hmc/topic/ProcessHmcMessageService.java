package uk.gov.hmcts.reform.et.service.hmc.topic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.et.model.hmc.message.HmcMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessHmcMessageService {

    public void processEventMessage(HmcMessage hmcMessage){
            // Currently not implemented here
            // just created this class for unit tests of HmcHearingsEventTopicListenerTest
            // This class has also been added to sonarExclusion list temporarily

    }
}