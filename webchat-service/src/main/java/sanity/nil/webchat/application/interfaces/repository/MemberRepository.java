package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.infrastructure.db.model.MemberModel;

import java.util.List;
import java.util.UUID;

public interface MemberRepository {
    UUID save(MemberModel member);
    List<MemberModel> getAllByChatID(UUID chatID);
}
