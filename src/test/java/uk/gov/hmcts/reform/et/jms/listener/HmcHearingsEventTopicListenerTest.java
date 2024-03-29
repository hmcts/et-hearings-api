package uk.gov.hmcts.reform.et.jms.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import org.apache.qpid.jms.message.JmsBytesMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.et.exception.HmcEventProcessingException;
import uk.gov.hmcts.reform.et.exception.MessageProcessingException;
import uk.gov.hmcts.reform.et.jms.listner.HmcHearingsEventTopicListener;
import uk.gov.hmcts.reform.et.model.hmc.message.HearingUpdate;
import uk.gov.hmcts.reform.et.model.hmc.message.HmcMessage;
import uk.gov.hmcts.reform.et.service.hmc.topic.ProcessHmcMessageService;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static uk.gov.hmcts.reform.et.model.hmc.reference.HmcStatus.ADJOURNED;


@ExtendWith(MockitoExtension.class)
class HmcHearingsEventTopicListenerTest {

    public static final String SERVICE_CODE = "BHA1";

    private HmcHearingsEventTopicListener hmcHearingsEventTopicListener;

    @Mock
    private ProcessHmcMessageService processHmcMessageService;

    @Mock
    private JmsBytesMessage bytesMessage;

    @Mock
    private ObjectMapper mockObjectMapper;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeEach
    void setup() {
        hmcHearingsEventTopicListener = new HmcHearingsEventTopicListener(SERVICE_CODE, processHmcMessageService);
        ReflectionTestUtils.setField(hmcHearingsEventTopicListener, "objectMapper", mockObjectMapper);
        ReflectionTestUtils.setField(hmcHearingsEventTopicListener, "hmctsServiceId", SERVICE_CODE);
    }

    @Test
    @DisplayName("Messages should not be processed if their service code does not match the service.")
    void serviceCodeNotApplicable() throws Exception {

        HmcMessage hmcMessage = createHmcMessage("BBA4");

        byte[] messageBytes = OBJECT_MAPPER.writeValueAsString(hmcMessage).getBytes(StandardCharsets.UTF_8);

        given(bytesMessage.getBodyLength()).willReturn((long) messageBytes.length);
        given(mockObjectMapper.readValue(any(String.class), eq(HmcMessage.class))).willReturn(hmcMessage);

        hmcHearingsEventTopicListener.onMessage(bytesMessage);

        verify(processHmcMessageService, never()).processEventMessage(any(HmcMessage.class));
    }

    @Test
    @DisplayName("Messages should be processed if their service code matches the service.")
    void serviceCodeApplicable() throws Exception {

        HmcMessage hmcMessage = createHmcMessage(SERVICE_CODE);

        byte[] messageBytes = OBJECT_MAPPER.writeValueAsString(hmcMessage).getBytes(StandardCharsets.UTF_8);

        given(bytesMessage.getBodyLength()).willReturn((long) messageBytes.length);
        given(mockObjectMapper.readValue(any(String.class), eq(HmcMessage.class))).willReturn(hmcMessage);

        hmcHearingsEventTopicListener.onMessage(bytesMessage);

        verify(processHmcMessageService).processEventMessage(any(HmcMessage.class));
    }

    @Test
    @DisplayName("A HmcEventProcessingException should be thrown if a JsonProcessing exception is encountered.")
    void testOnMessageJsonProcessingException() throws JsonProcessingException, JMSException {

        HmcMessage hmcMessage = createHmcMessage(SERVICE_CODE);

        byte[] messageBytes = OBJECT_MAPPER.writeValueAsString(hmcMessage).getBytes(StandardCharsets.UTF_8);

        given(bytesMessage.getBodyLength()).willReturn((long) messageBytes.length);
        given(mockObjectMapper.readValue(any(String.class), eq(HmcMessage.class)))
            .willThrow(JsonProcessingException.class);

        assertThatExceptionOfType(HmcEventProcessingException.class)
            .isThrownBy(() -> hmcHearingsEventTopicListener.onMessage(bytesMessage))
            .withCauseInstanceOf(JsonProcessingException.class);
    }

    @Test
    @DisplayName("A HmcEventProcessingException exception should be thrown an exception is encountered.")
    void testOnMessageHmcEventProcessingException() throws Exception {

        HmcMessage hmcMessage = createHmcMessage(SERVICE_CODE);

        byte[] messageBytes = OBJECT_MAPPER.writeValueAsString(hmcMessage).getBytes(StandardCharsets.UTF_8);

        given(bytesMessage.getBodyLength()).willReturn((long) messageBytes.length);
        given(mockObjectMapper.readValue(any(String.class), eq(HmcMessage.class))).willReturn(hmcMessage);

        doThrow(MessageProcessingException.class)
            .when(processHmcMessageService)
            .processEventMessage(hmcMessage);

        assertThatExceptionOfType(HmcEventProcessingException.class)
            .isThrownBy(() -> hmcHearingsEventTopicListener.onMessage(bytesMessage))
            .withCauseInstanceOf(MessageProcessingException.class);
    }

    private HmcMessage createHmcMessage(String messageServiceCode) {
        return HmcMessage.builder()
            .hmctsServiceCode(messageServiceCode)
            .caseId(1234L)
            .hearingId("testId")
            .hearingUpdate(HearingUpdate.builder()
                               .hmcStatus(ADJOURNED)
                               .build())
            .build();
    }
}

