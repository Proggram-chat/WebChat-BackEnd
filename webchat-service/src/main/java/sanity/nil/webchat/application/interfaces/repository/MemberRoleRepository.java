package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.infrastructure.db.postgres.model.MemberRoleModel;

import java.util.Optional;
import java.util.UUID;

public interface MemberRoleRepository {

    String addRoleType(UUID chatID, String roleType);
    Optional<MemberRoleModel> getByChatAndRoleType(UUID chatID, String roleType);
}
