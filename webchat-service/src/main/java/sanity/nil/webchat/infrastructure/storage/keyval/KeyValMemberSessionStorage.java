package sanity.nil.webchat.infrastructure.storage.keyval;

import sanity.nil.webchat.application.interfaces.storage.MemberSession;
import sanity.nil.webchat.application.interfaces.storage.MemberSessionStorage;

import java.util.*;
import java.util.stream.Collectors;

//@Component
public class KeyValMemberSessionStorage implements MemberSessionStorage {

    private List<WebsocketMemberSession> storage = Collections.synchronizedList(new ArrayList<>());

    @Override
    public boolean saveIfNotExists(MemberSession memberSession) {
        WebsocketMemberSession websocketMemberSession = (WebsocketMemberSession) memberSession;
        if (!storage.contains(websocketMemberSession)) {
            storage.add(websocketMemberSession);
            return true;
        }
        return false;
    }

    @Override
    public MemberSession getSessionByID(String id) {
        return storage.stream()
                .findFirst()
                .filter(e -> e.getId().equals(id))
                .orElseThrow(NoSuchElementException::new);
    }

    public List<MemberSession> getSessionsByMemberIDs(List<UUID> ids) {
        return storage.stream()
                .filter(e -> ids.contains(e.getMemberId()))
                .collect(Collectors.toList());
    }

    public void deleteBySessionID(String sessionID) {
        Optional<WebsocketMemberSession> sessionToDelete = storage.stream().filter(e -> e.getId().equals(sessionID)).findFirst();
        sessionToDelete.ifPresent(websocketMemberSession -> storage.remove(websocketMemberSession));
    }
}
