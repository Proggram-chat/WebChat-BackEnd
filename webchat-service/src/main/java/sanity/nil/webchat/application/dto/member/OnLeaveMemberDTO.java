package sanity.nil.webchat.application.dto.member;

import sanity.nil.webchat.application.consts.PayloadType;
import sanity.nil.webchat.infrastructure.channels.dto.CentrifugoPayloadBase;

import java.util.UUID;

public class OnLeaveMemberDTO extends CentrifugoPayloadBase {

    public UUID memberID;
    public String memberName;

    public OnLeaveMemberDTO(UUID memberID, String memberName) {
        super(PayloadType.USER_LEFT);
        this.memberID = memberID;
        this.memberName = memberName;
    }
}
