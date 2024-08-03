package sanity.nil.webchat.application.interfaces.storage;

import java.util.List;
import java.util.UUID;

public interface MemberSessionStorage {

    boolean saveIfNotExists(MemberSession memberSession);
    MemberSession getSessionByID(String id);

    List<MemberSession> getSessionsByMemberIDs(List<UUID> ids);
    void deleteBySessionID(String sessionID);
}
