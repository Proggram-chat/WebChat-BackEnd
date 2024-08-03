package sanity.nil.webchat.application.interfaces.storage;

import java.util.UUID;

public interface MemberSession {

    void setId(String id);
    UUID getMemberId();
    boolean isActive();
}
