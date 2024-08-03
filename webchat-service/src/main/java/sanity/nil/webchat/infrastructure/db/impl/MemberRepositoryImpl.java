package sanity.nil.webchat.infrastructure.db.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sanity.nil.webchat.application.interfaces.repository.MemberRepository;
import sanity.nil.webchat.infrastructure.db.model.MemberModel;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberDAO memberDAO;

    @Override
    public UUID save(MemberModel member) {
        return memberDAO.save(member).getMemberID();
    }

    @Override
    public List<MemberModel> getAllByChatID(UUID chat) {
        return memberDAO.getAllByChatID(chat);
    }

//    @Repository
//    private interface MemberDAO extends JpaRepository<MemberModel, UUID> {
//    }
}
