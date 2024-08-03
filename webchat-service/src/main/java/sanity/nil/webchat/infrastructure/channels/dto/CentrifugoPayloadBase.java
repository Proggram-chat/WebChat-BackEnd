package sanity.nil.webchat.infrastructure.channels.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import sanity.nil.webchat.application.consts.PayloadType;

@AllArgsConstructor
@NoArgsConstructor
public class CentrifugoPayloadBase {

    public PayloadType payloadType;
}
