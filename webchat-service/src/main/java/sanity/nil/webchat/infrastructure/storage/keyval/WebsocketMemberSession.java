package sanity.nil.webchat.infrastructure.storage.keyval;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.socket.WebSocketSession;
import sanity.nil.webchat.application.interfaces.storage.MemberSession;

import java.util.UUID;

@Getter
@Setter
public class WebsocketMemberSession implements MemberSession {

    private String id;
    private UUID memberId;
    private WebSocketSession webSocketSession;
    private boolean active;

    public WebsocketMemberSession(String id, UUID memberID,
                                  WebSocketSession webSocketSession, boolean active) {
        this.id = id;
        this.memberId = memberID;
        this.webSocketSession = webSocketSession;
        this.active = active;
    }
}
