package sanity.nil.webchat.infrastructure.db.postgres.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sanity.nil.webchat.infrastructure.db.postgres.model.FunctionModel;

public interface FunctionDAO extends JpaRepository<FunctionModel, Integer> {
}
