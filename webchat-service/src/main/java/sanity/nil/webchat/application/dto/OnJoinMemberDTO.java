package sanity.nil.webchat.application.dto;

import sanity.nil.webchat.application.consts.PayloadType;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoPayloadBase;

import java.util.UUID;

public class OnJoinMemberDTO extends CentrifugoPayloadBase {

    public UUID memberID;
    public String memberName;

    public OnJoinMemberDTO(UUID memberID, String memberName) {
        super(PayloadType.USER_JOINED);
        this.memberID = memberID;
        this.memberName = memberName;
    }
}
