package csie.bdsa.lms.authservice.repository;

import csie.bdsa.lms.authservice.model.User;
import csie.bdsa.lms.shared.repository.BaseRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    @Override
    @Query("SELECT x FROM #{#entityName} x WHERE x.deleted = false AND (CAST(x.id AS string) LIKE :search OR x.username LIKE :search)")
    Page<User> findContaining(Pageable pageable, String search);

    Optional<User> findByUsername(String username);
}
