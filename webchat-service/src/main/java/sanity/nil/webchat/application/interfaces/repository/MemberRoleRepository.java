package sanity.nil.webchat.application.interfaces.repository;

import sanity.nil.webchat.infrastructure.db.postgres.model.FunctionModel;
import sanity.nil.webchat.infrastructure.db.postgres.model.MemberRoleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRoleRepository {

    UUID addRoleType(UUID chatID, String roleType, String functions);
    Optional<MemberRoleModel> getByChatAndRoleType(UUID chatID, String roleType);
    List<FunctionModel> getAllFunctions();
}
