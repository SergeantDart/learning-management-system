package csie.bdsa.lms.authservice.repository;

import csie.bdsa.lms.authservice.model.Role;
import csie.bdsa.lms.shared.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {

    @Override
    @Query("SELECT x FROM #{#entityName} x WHERE x.deleted = false AND (CAST(x.id AS string) LIKE :search OR x.authority LIKE :search)")
    Page<Role> findContaining(Pageable pageable, String search);
}
