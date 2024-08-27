package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.infrastructure.db.postgres.model.MemberModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {
    UUID save(MemberModel member);
    List<MemberModel> getAllByChatID(UUID chatID);
    Optional<MemberModel> getByMemberID(UUID memberID);
    List<UUID> getMemberIdsByChatID(UUID chatID);
    void removeMemberFromChat(UUID memberID, UUID chatID);
    void addMemberToChat(UUID memberID, UUID chatID);
}
