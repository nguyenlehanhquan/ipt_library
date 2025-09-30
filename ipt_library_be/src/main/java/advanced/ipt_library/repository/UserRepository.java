package advanced.ipt_library.repository;

import advanced.ipt_library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsernameIgnoreCase(String username);
    User findByUsername(String username);

}
